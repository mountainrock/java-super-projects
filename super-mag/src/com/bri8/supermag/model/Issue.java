package com.bri8.supermag.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.builder.ToStringBuilder;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Issue extends BaseModel {
	 @PrimaryKey
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	 Long issueId;
	 
	 @Persistent Long  magazineId;
	 @Persistent String  issueName;
	 @Persistent String  description;
	 @Persistent String  status;// (preview, review, live)
	 @Persistent Boolean  specialIssue;
	 @Persistent String  publishingDate;
	 @Persistent String  previewPages;
	 @Persistent Float  singleIssuePrice;
	 @Persistent String  blobKey;
	 
	public Long getIssueId() {
		return issueId;
	}
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}
	public Long getMagazineId() {
		return magazineId;
	}
	public void setMagazineId(Long magazineId) {
		this.magazineId = magazineId;
	}
	public String getIssueName() {
		return issueName;
	}
	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getSpecialIssue() {
		return specialIssue;
	}
	public void setSpecialIssue(Boolean specialIssue) {
		this.specialIssue = specialIssue;
	}
	public String getPublishingDate() {
		return publishingDate;
	}
	public void setPublishingDate(String publishingDate) {
		this.publishingDate = publishingDate;
	}
	public String getPreviewPages() {
		return previewPages;
	}
	public void setPreviewPages(String previewPages) {
		this.previewPages = previewPages;
	}
	public Float getSingleIssuePrice() {
		return singleIssuePrice;
	}
	public void setSingleIssuePrice(Float singleIssuePrice) {
		this.singleIssuePrice = singleIssuePrice;
	}
	public String getBlobKey() {
		return blobKey;
	}
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
