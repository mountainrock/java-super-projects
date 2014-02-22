package com.bri8.supermag.model;

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
	 @Persistent String  categoryId;
	 @Persistent String  ageRating;
	 @Persistent String  keywords;
	 @Persistent String  webUrl;
	 @Persistent String  countryBlock;
	 @Persistent String    countryPublishFrom;
	 @Persistent String  pricePerIssueIn$;
	 @Persistent String  issues3months;
	 @Persistent String  issuePrice3months;
	 @Persistent String issues6months;
	 @Persistent String  issuePrice6months;
	 @Persistent String issues12months;
	 @Persistent String   issuePrice12months;
	 @Persistent String  magazineFrequency;
	 @Persistent String  language;
	 
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
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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
	public String getPricePerIssueIn$() {
		return pricePerIssueIn$;
	}
	public void setPricePerIssueIn$(String pricePerIssueIn$) {
		this.pricePerIssueIn$ = pricePerIssueIn$;
	}
	public String getIssues3months() {
		return issues3months;
	}
	public void setIssues3months(String issues3months) {
		this.issues3months = issues3months;
	}
	public String getIssuePrice3months() {
		return issuePrice3months;
	}
	public void setIssuePrice3months(String issuePrice3months) {
		this.issuePrice3months = issuePrice3months;
	}
	public String getIssues6months() {
		return issues6months;
	}
	public void setIssues6months(String issues6months) {
		this.issues6months = issues6months;
	}
	public String getIssuePrice6months() {
		return issuePrice6months;
	}
	public void setIssuePrice6months(String issuePrice6months) {
		this.issuePrice6months = issuePrice6months;
	}
	public String getIssues12months() {
		return issues12months;
	}
	public void setIssues12months(String issues12months) {
		this.issues12months = issues12months;
	}
	public String getIssuePrice12months() {
		return issuePrice12months;
	}
	public void setIssuePrice12months(String issuePrice12months) {
		this.issuePrice12months = issuePrice12months;
	}
	public String getMagazineFrequency() {
		return magazineFrequency;
	}
	public void setMagazineFrequency(String magazineFrequency) {
		this.magazineFrequency = magazineFrequency;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
