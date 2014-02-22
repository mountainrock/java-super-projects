package com.bri8.supermag.model;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import org.apache.commons.lang.builder.ToStringBuilder;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User extends BaseModel{

	 @PrimaryKey
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	 Long userId;
 
	 //unique email id
	 @Persistent @Unique 
	 String  email;
	 
	 @Persistent String  password;
	 @Persistent String  firstName;
	 @Persistent String  lastName;
	 @Persistent String  company;
	 @Persistent String  designation;
	 @Persistent String  phoneNo;
	 @Persistent String  mobileNo;
	 @Persistent String  website;
	 @Persistent String  address1;
	 @Persistent String  address2;
	 @Persistent String   country;
	 @Persistent String   state;
	 @Persistent String  city;
	 @Persistent String  zipCode;
	 @Persistent String  howDidYouHear;
	 
	 //subscriber/publisher/admin
	 @Persistent String  userType;
	
	 public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getDesignation() {
		return designation;
	}


	public void setDesignation(String designation) {
		this.designation = designation;
	}


	public String getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}


	public String getAddress1() {
		return address1;
	}


	public void setAddress1(String address1) {
		this.address1 = address1;
	}


	public String getAddress2() {
		return address2;
	}


	public void setAddress2(String address2) {
		this.address2 = address2;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getHowDidYouHear() {
		return howDidYouHear;
	}


	public void setHowDidYouHear(String howDidYouHear) {
		this.howDidYouHear = howDidYouHear;
	}


	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}


	@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}

}
