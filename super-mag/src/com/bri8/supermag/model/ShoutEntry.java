package com.bri8.supermag.model;

import java.util.Date;
import javax.jdo.annotations.*;

import org.apache.commons.lang.builder.ToStringBuilder;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ShoutEntry {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long id;

	@Persistent
	Date date;

	@Persistent
	String content;

	@Persistent
	String shouter;

	@Persistent
	String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getShouter() {
		return shouter;
	}

	public void setShouter(String shouter) {
		this.shouter = shouter;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
