package com.bri8.supermag.util;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ListHolder {
	private List list;

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
