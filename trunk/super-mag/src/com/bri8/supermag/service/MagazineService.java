package com.bri8.supermag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.IssueDAO;
import com.bri8.supermag.dao.IssuePageDAO;
import com.bri8.supermag.dao.MagazineDAO;
import com.bri8.supermag.dao.UserDAO;
import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.IssuePage;
import com.bri8.supermag.model.IssueStatus;
import com.bri8.supermag.model.Magazine;
import com.bri8.supermag.model.MagazineIssues;

@Component("magazineService")
public class MagazineService {

	@Autowired
	MagazineDAO magazineDao;
	@Autowired
	IssueDAO issueDao;
	
	@Autowired
	IssuePageDAO issuePageDao;
	
	@Autowired
	UserDAO userDao;

	public void createMagazine(Magazine magazine) {
		magazineDao.create(magazine);
	}

	public List<MagazineIssues> listMagazineIssues(Long userId) {
		List<MagazineIssues> magazineIssuesList = new ArrayList<MagazineIssues>();
		if (userId != null) {
			List<Magazine> magList = magazineDao.read(Magazine.class, "userId ==" + userId, " order by magazineId DESC", 10000);
			for (Magazine magazine : magList) {
				MagazineIssues magazineIssues = new MagazineIssues();
				List<Issue> issues = issueDao.read(Issue.class, "magazineId == " + magazine.getMagazineId(), "order by issueId DESC");
				magazineIssues.setMagazine(magazine);
				magazineIssues.setIssues(issues);
				magazineIssuesList.add(magazineIssues);
			}

		}
		return magazineIssuesList;
	}

	public List<Issue> listMagazineIssuesToGeneratePdfImage() {
		List<Issue> issuesToGeneratePdfImage = issueDao.read(Issue.class, "status == " + IssueStatus.Uploaded.name(), "order by issueId DESC");
		return issuesToGeneratePdfImage;
	}

	public void createIssue(Issue issue) {
		issueDao.create(issue);
	}

	public Issue getIssue(Long issueId) {
		return issueDao.read(issueId, Issue.class);
	}

	public void addIssueImageBlobKey(IssuePage issuePage) {
		
		issuePageDao.create(issuePage);
	}
	
	public List<IssuePage> getIssuePages(Long issueId) {
		return issuePageDao.read(IssuePage.class, "issueId == " + issueId, "order by pageNumber ASC");
	}

	public IssuePage getIssuePage(Long issuePageId) {
		return issuePageDao.read(issuePageId, IssuePage.class);
	}

	public IssuePage deleteIssuePage(Long issuePageId) {
		return issuePageDao.delete(issuePageId, IssuePage.class);
	}

}
