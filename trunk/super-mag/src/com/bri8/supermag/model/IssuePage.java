package com.bri8.supermag.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Model for magazine page stored as image blob in blobstore. referenced by blobkey
 */
@PersistenceCapable(detachable = "true")
public class IssuePage extends BaseModel {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long issuePageId;

	@Persistent @Unique
	Long issueId;

	@Persistent
	String blobKey;

	@Persistent
	String blobKeyThumbnail;
	
	@Persistent
	String fileType;
	
	@Persistent
	String fileName;

	@Persistent
	Integer version;

	@Persistent
	Date updatedDate;

	@Persistent
	Boolean isActive;


	@Persistent
	Integer pageNumber;

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getIssuePageId() {
		return issuePageId;
	}

	public void setIssuePageId(Long issuePageId) {
		this.issuePageId = issuePageId;
	}

	public Long getIssueId() {
		return issueId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public String getBlobKey() {
		return blobKey;
	}

	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	

	public String getBlobKeyThumbnail() {
		return blobKeyThumbnail;
	}

	public void setBlobKeyThumbnail(String blobKeyThumbnail) {
		this.blobKeyThumbnail = blobKeyThumbnail;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
