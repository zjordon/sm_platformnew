/**
 * @(#) Bootstrap.java 2006-2-28
 * 
 * Copyright 2006 ptnetwork
 */
package com.ptnetwork.enterpriseSms.client.startup;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jasonzhang
 *
 */
public final class Bootstrap {
	private static Log log = LogFactory.getLog(Bootstrap.class);

	private static Bootstrap daemon = null;
	private Object catalinaDaemon = null;
	private ClassLoader commonLoader = null;
	private ClassLoader catalinaLoader = null;
	
	private static final String CATALINA_HOME_TOKEN = "${catalina.home}";
	
	private ClassLoader createClassLoader(String name, ClassLoader parent) throws Exception {
		String value = CatalinaProperties.getProperty(name + ".loader");
        if ((value == null) || (value.equals("")))
            return parent;

        ArrayList<File> unpackedList = new ArrayList<File>();
        ArrayList<File> packedList = new ArrayList<File>();
        ArrayList<URL> urlList = new ArrayList<URL>();

        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        while (tokenizer.hasMoreElements()) {
            String repository = tokenizer.nextToken();

            // Local repository
            boolean packed = false;
            if (repository.startsWith(CATALINA_HOME_TOKEN)) {
                repository = getCatalinaHome() 
                    + repository.substring(CATALINA_HOME_TOKEN.length());
            }

            // Check for a JAR URL repository
            try {
                urlList.add(new URL(repository));
                continue;
            } catch (MalformedURLException e) {
                // Ignore
            }

            if (repository.endsWith("*.jar")) {
                packed = true;
                repository = repository.substring
                    (0, repository.length() - "*.jar".length());
            }
            if (packed) {
                packedList.add(new File(repository));
            } else {
                unpackedList.add(new File(repository));
            }
        }

        File[] unpacked = (File[]) unpackedList.toArray(new File[0]);
        File[] packed = (File[]) packedList.toArray(new File[0]);
        URL[] urls = (URL[]) urlList.toArray(new URL[0]);

        ClassLoader classLoader = ClassLoaderFactory.createClassLoader
            (unpacked, packed, urls, parent);

        return classLoader;
	}
	
	/**
     * Start the Catalina daemon.
     */
    public void start() throws Exception {
        if(catalinaDaemon==null) {
        	init();
        }
        Method method = catalinaDaemon.getClass().getMethod("start", new Class[]{});
        method.invoke(catalinaDaemon, new Object[]{});

    }
    
    public void stop() throws Exception {
    	Method method = catalinaDaemon.getClass().getMethod("stop", new Class[]{});
        method.invoke(catalinaDaemon, new Object[]{});
    }
	
	/**
     * Initialize daemon.
     */
    public void init()
        throws Exception
    {
        initClassLoaders();
        Thread.currentThread().setContextClassLoader(catalinaLoader);
        //SecurityClassLoad.securityClassLoad(catalinaLoader);
        // Load our startup class and call its process() method
        Class startupClass =
            catalinaLoader.loadClass
            ("com.ptnetwork.enterpriseSms.client.App");
        Object startupInstance = startupClass.newInstance();
        // Set the shared extensions class loader
        /*
        String methodName = "setParentClassLoader";
        Class paramTypes[] = new Class[1];
        paramTypes[0] = Class.forName("java.lang.ClassLoader");
        Object paramValues[] = new Object[1];
        paramValues[0] = sharedLoader;
        Method method =
            startupInstance.getClass().getMethod(methodName, paramTypes);
        method.invoke(startupInstance, paramValues);
        */

        catalinaDaemon = startupInstance;

    }
    
    private void initClassLoaders() {
        try {
            commonLoader = createClassLoader("common", null);
            catalinaLoader = createClassLoader("server", commonLoader);
        } catch (Throwable t) {
            //log.error("Class loader creation threw exception", t);
        	//t.printStackTrace();
            System.exit(1);
        }
    }
	
	/**
     * Get the value of the catalina.home environment variable.
     */
    public static String getCatalinaHome() {
        return System.getProperty("catalina.home");
    }
    
    public static void main(String[] args) {
//    	args = new String[] {"start"};
//    	System.setProperty("catalina.home", "D:\\SmgpAppService");
    	
    	if (daemon == null) {
    		daemon = new Bootstrap();
    		try {
				daemon.init();
			} catch (Throwable t) {
				log.error(t);
				//t.printStackTrace();
				return;
			}
    	}
    	
    	String commond = "start";
    	if (args.length != 0) {
    		commond = args[args.length - 1];
    	}
    	try {
    		if ("start".equals(commond)) {
    			log.info("start the server");
        		daemon.load(args);
        		daemon.start();
        	} else if ("stop".equals(commond)) {
        		log.info("stop the server");
        		daemon.stop();
        	} else {
        		//log it
        		log.warn("the command is not validate:" + commond);
        	}
    	} catch (Throwable t) {
    		log.error(t);
    		//t.printStackTrace();
    	}
    }
    
    /**
     * Load daemon.
     */
    private void load(String[] arguments) throws Exception {

        // Call the load() method
        String methodName = "load";
        Object param[];
        Class paramTypes[];
        if (arguments==null || arguments.length==0) {
            paramTypes = null;
            param = null;
        } else {
            paramTypes = new Class[1];
            paramTypes[0] = arguments.getClass();
            param = new Object[1];
            param[0] = arguments;
        }
        Method method = 
            catalinaDaemon.getClass().getMethod(methodName, paramTypes);
        //if (log.isDebugEnabled())
         //   log.debug("Calling startup class " + method);
        method.invoke(catalinaDaemon, param);

    }
    
}
