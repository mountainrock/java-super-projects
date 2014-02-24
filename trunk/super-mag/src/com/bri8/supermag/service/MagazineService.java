package com.bri8.supermag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.IssueDAO;
import com.bri8.supermag.dao.MagazineDAO;
import com.bri8.supermag.dao.UserDAO;
import com.bri8.supermag.model.Magazine;

@Component
public class MagazineService {

	@Autowired MagazineDAO magazineDao;
	@Autowired IssueDAO issueDao;
	@Autowired UserDAO userDao;

	public void createMagazine(Magazine magazine) {
		 magazineDao.create(magazine);
	}

	public List<Magazine> listMagazine(Long userId) {
		if(userId!=null){
			return magazineDao.read(Magazine.class, "userId =="+ userId, " order by magazineId DESC", 10000);
		}
		return new ArrayList<Magazine>();
	}
	
}
