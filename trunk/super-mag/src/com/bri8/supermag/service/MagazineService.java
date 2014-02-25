package com.bri8.supermag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.IssueDAO;
import com.bri8.supermag.dao.MagazineDAO;
import com.bri8.supermag.dao.UserDAO;
import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.Magazine;

@Component
public class MagazineService {

	@Autowired MagazineDAO magazineDao;
	@Autowired IssueDAO issueDao;
	@Autowired UserDAO userDao;

	public void createMagazine(Magazine magazine) {
		 magazineDao.create(magazine);
	}

	public List<Magazine> listMagazine(Long userId, Boolean loadIssues) {
		if(userId!=null){
			 List<Magazine> magList = magazineDao.read(Magazine.class, "userId =="+ userId, " order by magazineId DESC", 10000);
			if(loadIssues){
				for (Magazine magazine : magList) {
					List<Issue> issues = issueDao.read(Issue.class, "magazineId == "+ magazine.getMagazineId(), "order by issueId DESC");
					magazine.getIssues().addAll(issues);
				}
			}
		}
		return new ArrayList<Magazine>();
	}
	
	public void createIssue(Issue issue) {
		issueDao.create(issue);
	}

	
}
