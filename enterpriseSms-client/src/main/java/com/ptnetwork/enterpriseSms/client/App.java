package com.ptnetwork.enterpriseSms.client;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.core.Config;
import com.ptnetwork.enterpriseSms.client.core.Kernel;
import com.ptnetwork.enterpriseSms.client.event.EventManager;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;
import com.ptnetwork.enterpriseSms.client.queue.QueueManager;
import com.ptnetwork.enterpriseSms.client.scheduler.TaskScheduler;
import com.ptnetwork.enterpriseSms.client.tools.PropertiesHelper;

/**
 * Hello world!
 *
 */
public class App 
{
	private static Log log = LogFactory.getLog(App.class);

	
	private boolean starting = false;
	private boolean stopping = false;
	
	private CatalinaShutdownHook shutdownHook;
	private Kernel kernel;
	

	public void start() {
		//System.out.println("Catalina this is " + this.toString());
		long t1 = System.currentTimeMillis();
		DaoManager.getInstance().init();
		CacheManager.getInstance().init();
		//初始化内核模块
		this.kernel = Kernel.getInstance();
		//首先要初始化配置参数
		Config config = this.initConfig();
		this.kernel.setDeliverListener(QueueManager.getInstance());
		if (config != null) {
			this.kernel.setConfig(config);
			this.kernel.init();
		}
		EventManager.getInstance().init();
		TaskScheduler.getInstance().init();
		
		if (shutdownHook == null) {
			shutdownHook = new CatalinaShutdownHook();
		}
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		long t2 = System.currentTimeMillis();
		log.info("Server startup in " + (t2 - t1) + " ms");
	}

	public void stop() {
		//System.out.println("Catalina this is " + this.toString());
		this.kernel.stop();
		Runtime.getRuntime().removeShutdownHook(shutdownHook);
		
	}
	
	/**
     * The application main program.
     *
     * @param args Command line arguments
     */
    public static void main(String args[]) {
    	args = new String[] {"start"};
    	System.setProperty("catalina.home", "D:\\work\\ddo\\enterpriseSms-client");
        (new App()).process(args);
    }


    /**
     * The instance main program.
     *
     * @param args Command line arguments
     */
    public void process(String args[]) {

        //setAwait(true);
        //setCatalinaHome();
        try {
            if (arguments(args)) {
                if (starting) {
                    load(args);
                    start();
                } else if (stopping) {
                    //stopServer();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    
    /* 
     * Load using arguments
     */
    public void load(String args[]) {

        try {
            if (arguments(args)) {
            	load();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    
    private void load() {
    	long t1 = System.currentTimeMillis();
//    	server.init();
        long t2 = System.currentTimeMillis();
        log.info("Initialization processed in " + (t2 - t1) + " ms");
    }
    
   
    
    
    /**
     * Process the specified command line arguments, and return
     * <code>true</code> if we should continue processing; otherwise
     * return <code>false</code>.
     *
     * @param args Command line arguments to process
     */
    private boolean arguments(String args[]) {

        if (args.length < 1) {
            usage();
            return (false);
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-config")) {
                
            } else if (args[i].equals("-help")) {
                usage();
                return (false);
            } else if (args[i].equals("start")) {
                starting = true;
                stopping = false;
            } else if (args[i].equals("stop")) {
                starting = false;
                stopping = true;
            } else {
                usage();
                return (false);
            }
        }

        return (true);

    }
    
    /**
     * Print usage information for this application.
     */
    private void usage() {

        System.out.println
            ("usage: java com.ptnetwork.enterpriseSms.client.App"
             + " [ -config {pathname} ]"
             + " { start | stop }");

    }
    
    private class CatalinaShutdownHook extends Thread {
    	public void run() {
    			App.this.stop();
    	}
    }
    
    private Config initConfig() {
    	Config config = null;
    	PropertiesHelper propsHelper = PropertiesHelper.getInstance();
    	Properties props = propsHelper.loadProps("config/sp.properties", "sp.properties");
    	if (props != null) {
    		config = new Config();
    		config.setIsmgIp(props.getProperty("ismgIp"));
    		config.setIsmgPort(Integer.parseInt(props.getProperty("ismgPort")));
    		config.setSpId(props.getProperty("spId"));
    		config.setSharedSecret(props.getProperty("sharedSecret"));
    		config.setSpCode(props.getProperty("spCode"));
    		config.setConnectCount(Integer.parseInt(props.getProperty("connectCount")));
    		config.setActiveTesCount(Integer.parseInt(props.getProperty("activeTesCount")));
    		config.setActiveTestInterval(Integer.parseInt(props.getProperty("activeTestInterval")));
    		config.setActiveTestTimeout(Integer.parseInt(props.getProperty("activeTestTimeout")));
    	} else {
    		log.error("the sp.properties is not exist!!!");
    	}
    	return config;
    }
}
