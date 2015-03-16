package com.bri8.supermag.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.builder.ToStringBuilder;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Purchase extends BaseModel {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long purchaseId;

	@Persistent
	Long userId;
	@Persistent
	Long magazineId;
	@Persistent
	Long issueId;
	@Persistent
	String subscriptionType; // single/monthly/weekly etc
	@Persistent
	String issueName;
	@Persistent
	String currency;
	@Persistent
	Float price;

	@NotPersistent
	IssuePage issuePage;
	
	// audit
	@Persistent
	Date createdDate;
	@Persistent
	Date modifiedDate;
	@Persistent
	String modifiedBy;

	public Long getPurchaseId() {
		return purchaseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMagazineId() {
		return magazineId;
	}

	public void setMagazineId(Long magazineId) {
		this.magazineId = magazineId;
	}

	public Long getIssueId() {
		return issueId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
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
	

	public IssuePage getIssuePage() {
		return issuePage;
	}

	public void setIssuePage(IssuePage issuePage) {
		this.issuePage = issuePage;
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
