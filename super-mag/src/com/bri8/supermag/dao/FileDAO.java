package com.bri8.supermag.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bri8.supermag.model.File;

@Component
public class FileDAO extends BaseDAO<File> {

	private static Log logger = LogFactory.getLog(FileDAO.class);


}