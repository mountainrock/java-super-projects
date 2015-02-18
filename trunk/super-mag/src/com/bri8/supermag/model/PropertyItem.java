package com.bri8.supermag.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class PropertyItem extends BaseModel {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long propertyId;
	
	@Persistent
	String group; // eg: language, currency, categories
	
	@Persistent
	String name; // eg: kannada
	
	@Persistent
	String value; // optional eg: 1

	public PropertyItem(String group, String name) {
		this.group = group;
		this.name = name;
	}

	public PropertyItem(String group, String name, String value) {
		this.group = group;
		this.name = name;
		this.value = value;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

}
