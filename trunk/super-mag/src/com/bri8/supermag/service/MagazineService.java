package com.bri8.supermag.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.IssueDAO;
import com.bri8.supermag.dao.IssuePageDAO;
import com.bri8.supermag.dao.MagazineDAO;
import com.bri8.supermag.dao.UserDAO;
import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.IssuePage;
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

	// publisher APIs begin
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
	
	public IssuePage getIssueFrontPageByIssueId(Issue issue) {
		Integer coverPageNumber = issue.getCoverPageNumber()==null ? 1 : issue.getCoverPageNumber();
		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("issueId", issue.getIssueId());
		paramValues.put("pageNumber", coverPageNumber);
		
		String filter="issueId == " + issue.getIssueId()+" and pageNumber == "+coverPageNumber;
		return issuePageDao.readWithFilter(IssuePage.class, filter, paramValues).get(0);
	}

	public IssuePage deleteIssuePage(Long issuePageId) {
		return issuePageDao.delete(issuePageId, IssuePage.class);
	}

	// publisher APIs end

	// main page API begin
	public List<MagazineIssues> listMainPageMagazineIssues(String magazineGroup) {
		List<MagazineIssues> magazineIssuesList = new ArrayList<MagazineIssues>();
		if ("all".equals(magazineGroup)) {
			List<Magazine> magList = magazineDao.read(Magazine.class, " order by magazineId DESC", 10);
			for (Magazine magazine : magList) {
				MagazineIssues magazineIssues = new MagazineIssues();
				// TODO: filter by status = live. Get only 1 top record
				List<Issue> issues = issueDao.read(Issue.class, "magazineId == " + magazine.getMagazineId(), "order by issueId DESC", 1);
				if (!issues.isEmpty()) {
					Long issueId = issues.get(0).getIssueId();
					// TODO: get pageNumber from Issue configuration set by publisher.
					List<IssuePage> issuePages = issuePageDao.read(IssuePage.class, String.format("issueId == %s ", issueId), "order by pageNumber ASC", 1);

					magazineIssues.setMagazine(magazine);
					magazineIssues.setIssues(issues);
					magazineIssues.setIssuePages(issuePages);
					
					magazineIssuesList.add(magazineIssues);
				}
			}

		} else {
			// load group specific magazines. eg: only magazines from india today group
		}
		return magazineIssuesList;
	}
	// main page API end

	public Magazine getMagazine(Long magazineId) {
		return magazineDao.read(magazineId, Magazine.class);
	}

}
