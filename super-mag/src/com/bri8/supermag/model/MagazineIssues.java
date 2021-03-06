package com.bri8.supermag.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class MagazineIssues {

	Magazine magazine;
	List<Issue> issues = new ArrayList<Issue>();
	List<IssuePage> issuePages = new ArrayList<IssuePage>();

	public Magazine getMagazine() {
		return magazine;
	}

	public void setMagazine(Magazine magazine) {
		this.magazine = magazine;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public List<IssuePage> getIssuePages() {
		return issuePages;
	}

	public void setIssuePages(List<IssuePage> issuePages) {
		this.issuePages = issuePages;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
