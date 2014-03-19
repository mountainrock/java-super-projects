package com.bri8.supermag.service;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.IssueDAO;
import com.bri8.supermag.dao.MagazineDAO;
import com.bri8.supermag.dao.PMF;
import com.bri8.supermag.dao.UserDAO;
import com.bri8.supermag.model.Issue;
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

	public void updateIssuePdfBlobKey(Long issueId, String blobKey) {

		PersistenceManager persistenceManager = PMF.get().getPersistenceManager();
		Issue issue = issueDao.read(issueId, Issue.class, persistenceManager);
		Issue detachCopy = persistenceManager.detachCopy(issue);
		detachCopy.setPdfBlobKey(blobKey);
		issueDao.update(detachCopy, Issue.class);
	}

}
