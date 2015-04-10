package com.bri8.supermag.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
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

	@NotPersistent
	String publishingDateStr;
	
	@Persistent
	String previewPages;
	@Persistent
	Integer coverPageNumber;

	//audit
	 @Persistent Date  createdDate;
	 @Persistent Date  modifiedDate; 
	 @Persistent String  modifiedBy;

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

	public Integer getCoverPageNumber() {
		return coverPageNumber;
	}

	public void setCoverPageNumber(Integer coverPageNumber) {
		this.coverPageNumber = coverPageNumber;
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

	
	public String getPublishingDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(publishingDate);
	}

	public void setPublishingDateStr(String publishingDateStr) {
		this.publishingDateStr = publishingDateStr;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date date = sdf.parse(publishingDateStr);
			setPublishingDate(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
