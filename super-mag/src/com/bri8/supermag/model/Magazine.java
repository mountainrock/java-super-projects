package com.bri8.supermag.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.builder.ToStringBuilder;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Magazine extends BaseModel{
	 @PrimaryKey
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	 Long magazineId;

	 @Persistent Long  userId;
	 @Persistent String  magazineName;
	 @Persistent String  publishingCompany;
	 @Persistent String  description;
	 @Persistent String  category1;
	 @Persistent String  category2;
	 @Persistent String  ageRating;
	 @Persistent String  keywords;
	 @Persistent String  webUrl;
	 @Persistent String  countryBlock;
	 @Persistent String  countryPublishFrom;
	 
	 @Persistent Float  pricePerIssue;
	 @Persistent String  currency;
	 @Persistent Integer  issues3months;
	 @Persistent Float  issuePrice3months;
	 @Persistent Integer issues6months;
	 @Persistent Float  issuePrice6months;
	 @Persistent Integer issues12months;
	 @Persistent Float   issuePrice12months;
	 @Persistent String  frequency;
	 @Persistent String  language;
	 
	//audit
	 @Persistent Date  createdDate;
	 @Persistent Date  modifiedDate; 
	 @Persistent String  modifiedBy;
	 
	public Long getMagazineId() {
		return magazineId;
	}
	public void setMagazineId(Long magazineId) {
		this.magazineId = magazineId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMagazineName() {
		return magazineName;
	}
	public void setMagazineName(String magazineName) {
		this.magazineName = magazineName;
	}
	public String getPublishingCompany() {
		return publishingCompany;
	}
	public void setPublishingCompany(String publishingCompany) {
		this.publishingCompany = publishingCompany;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory1() {
		return category1;
	}
	public void setCategory1(String category1) {
		this.category1 = category1;
	}
	public String getCategory2() {
		return category2;
	}
	public void setCategory2(String category2) {
		this.category2 = category2;
	}
	public String getAgeRating() {
		return ageRating;
	}
	public void setAgeRating(String ageRating) {
		this.ageRating = ageRating;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getCountryBlock() {
		return countryBlock;
	}
	public void setCountryBlock(String countryBlock) {
		this.countryBlock = countryBlock;
	}
	public String getCountryPublishFrom() {
		return countryPublishFrom;
	}
	public void setCountryPublishFrom(String countryPublishFrom) {
		this.countryPublishFrom = countryPublishFrom;
	}
	
	public Float getPricePerIssue() {
		return pricePerIssue;
	}
	public void setPricePerIssue(Float pricePerIssue) {
		this.pricePerIssue = pricePerIssue;
	}
	public Integer getIssues3months() {
		return issues3months;
	}
	public void setIssues3months(Integer issues3months) {
		this.issues3months = issues3months;
	}
	public Float getIssuePrice3months() {
		return issuePrice3months;
	}
	public void setIssuePrice3months(Float issuePrice3months) {
		this.issuePrice3months = issuePrice3months;
	}
	public Integer getIssues6months() {
		return issues6months;
	}
	public void setIssues6months(Integer issues6months) {
		this.issues6months = issues6months;
	}
	public Float getIssuePrice6months() {
		return issuePrice6months;
	}
	public void setIssuePrice6months(Float issuePrice6months) {
		this.issuePrice6months = issuePrice6months;
	}
	public Integer getIssues12months() {
		return issues12months;
	}
	public void setIssues12months(Integer issues12months) {
		this.issues12months = issues12months;
	}
	public Float getIssuePrice12months() {
		return issuePrice12months;
	}
	public void setIssuePrice12months(Float issuePrice12months) {
		this.issuePrice12months = issuePrice12months;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
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
