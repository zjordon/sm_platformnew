/**
 * @(#) Header.java 2006-3-1
 * 
 * Copyright 2006 ptnetwork
 */
package com.ptnetwork.enterpriseSms.client.domain;

import java.io.Serializable;

/**
 * @author jasonzhang
 *
 */
public abstract class Header implements Serializable {

	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public boolean equals(Object o) {
		if (this == o)
			return true;
			
		if (o == null)
			return false;
			
		if (!(o instanceof Header))
			return false;
			
		return id == ((Header) o).getId();
	}
	
	public int hashCode() {
		return super.hashCode();
	}
	
	
}
