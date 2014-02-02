package com.bri8.supermag.web.command;

import javax.jdo.annotations.Persistent;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ShoutBoxCommand {
	String content;

	String shouter;

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
