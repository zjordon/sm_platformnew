/**
 * Project Name:enterpriseSms-client
 * File Name:SubmitInterface.java
 * Package Name:com.ptnetwork.enterpriseSms.client.net
 * Date:2013-1-27上午11:36:33
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.net;

import java.util.List;

import com.ptnetwork.enterpriseSms.client.domain.Submit;
import com.ptnetwork.enterpriseSms.client.exception.SubmitException;
import com.ptnetwork.enterpriseSms.client.result.SubmitResult;

/**
 * ClassName:SubmitInterface <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-27 上午11:36:33 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public interface SubmitInterface {

	SubmitResult submit(Submit submit) throws SubmitException;
	
	SubmitResult batchSubmit(List<Submit> submits) throws SubmitException;
}

