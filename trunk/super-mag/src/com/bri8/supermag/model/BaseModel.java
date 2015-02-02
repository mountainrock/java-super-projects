package com.bri8.supermag.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
public class BaseModel implements Serializable{
 
	 
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	 
	
}
