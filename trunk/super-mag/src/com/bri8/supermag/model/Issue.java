package com.bri8.supermag.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.builder.ToStringBuilder;

@PersistenceCapable(detachable = "true")
public class Issue extends BaseModel {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long issueId;

	@Persistent
	Long magazineId;
	@Persistent
	String issueName;
	@Persistent
	String description;
	@Persistent
	String status;// (uploaded, pdf2ImageGenerated, reviewComplete, live)
	@Persistent
	Boolean specialIssue;
	@Persistent
	Date publishingDate;
	@Persistent
	String previewPages;
	@Persistent
	Integer coverPageNumber;

	@Persistent
	Float singleIssuePrice;

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

	public Date getPublishingDate() {
		return publishingDate;
	}

	public void setPublishingDate(Date publishingDate) {
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

	public Integer getCoverPageNumber() {
		return coverPageNumber;
	}

	public void setCoverPageNumber(Integer coverPageNumber) {
		this.coverPageNumber = coverPageNumber;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
