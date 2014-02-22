package com.bri8.supermag.model;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Persistent;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BaseModel implements Serializable{

	 //audit
	 @Persistent Date  createdDate;
	 @Persistent Date  modifiedDate;
	 @Persistent String  modifiedBy;
	 
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	 
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	 
	
}
