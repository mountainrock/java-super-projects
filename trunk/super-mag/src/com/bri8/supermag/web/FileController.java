package com.bri8.supermag.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.dao.FileDAO;
import com.bri8.supermag.model.File;
import com.bri8.supermag.util.ValidateUser;
import com.bri8.supermag.util.WebUtil;

/**
 * 
 * Upload a file. Files can be rated by the user. Duplicate rating is checked using ip.
 */
@Controller
public class FileController {

	private static Log logger = LogFactory.getLog(FileController.class);
	@Autowired
	FileDAO fileDAO;

	Set<String> imagetypes = new HashSet<String>();
	@Autowired
	ValidateUser validateUser;

	@RequestMapping(value = { "/file/view" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView view() {
		ModelAndView mv = new ModelAndView("file/upload");
		return mv;
	}

	@RequestMapping(value = { "/file/upload" }, method = RequestMethod.POST)
	protected ModelAndView upload(HttpServletRequest request) throws Exception {

		Map<String, Object> attributes = new HashMap<String, Object>();
		String text = (String) request.getAttribute("text");
		File file = WebUtil.getInstance().getFile(request);
		fileDAO.create(file);
		attributes.put("message", "Thank you! You have now uploaded your file " + file.getName() + " and can view <a href=\"/file/" + file.getId() + "\">here</a>");
		ModelAndView layoutMandV = new ModelAndView();
		layoutMandV.addAllObjects(attributes);
		layoutMandV.setViewName("forward:/file/view");
		return layoutMandV;
	}

	@RequestMapping(value = { "/file/allCategory" }, method = RequestMethod.GET)
	// eg: /file/allCategory
	public ModelAndView showCategories(@RequestParam(value = "size", required = false) Integer size) {
		ModelAndView mv = new ModelAndView("file/list-field");
		mv.addObject("field", "category");
		List<File> files = fileDAO.read(File.class, size == null ? 1000 : size);
		getDistinctFiles(files, "category");
		mv.addObject("files", files);
		return mv;
	}

	@RequestMapping(value = { "/file/byCategoryGetFile/{category}" }, method = RequestMethod.GET)
	// eg: /file/byCategoryGetFile/[image]
	public ModelAndView byCategoryGetFile(@RequestParam(value = "size", required = false) Integer size, @PathVariable("category") String category) {
		ModelAndView mv = new ModelAndView("file/list");
		mv.addObject("files", fileDAO.read(File.class, "category =='" + category + "'", "order by rating desc", size == null ? 1000 : size));
		return mv;
	}

	@RequestMapping(value = { "/file/byCategoryGetField/{category}/{field}" }, method = RequestMethod.GET)
	// eg: /file/byCategoryGetField/[image]/[id]
	public ModelAndView byCategoryGetField(@RequestParam(value = "size", required = false) Integer size, @PathVariable("category") String category, @PathVariable("field") String field) {
		ModelAndView mv = new ModelAndView("file/list-field");
		mv.addObject("field", field);

		List<File> files = fileDAO.read(File.class, "category =='" + category + "'", "order by rating desc", size == null ? 1000 : size);
		getDistinctFiles(files, field);
		mv.addObject("files", files);
		return mv;
	}

	@RequestMapping(value = { "/file/byCategoryAndTagsGetField/{category}/{tags}/{field}" }, method = RequestMethod.GET)
	// eg: /file/byCategoryAndTagsGetField/[image]/[nature,blah]/[id]
	public ModelAndView byCategoryAndTagsGetField(@RequestParam(value = "size", required = false) Integer size, @PathVariable("category") String category, @PathVariable("tags") String tags,
			@PathVariable("field") String field) {
		ModelAndView mv = new ModelAndView("file/list-field");
		mv.addObject("field", field);
		String where = ":p.contains(tag) && category =='" + category + "'";
		List<String> tagList = new ArrayList<String>();
		String[] tagsArray = tags.split(",");
		for (String tagStr : tagsArray) {
			tagList.add(tagStr);
		}

		mv.addObject("files", fileDAO.read(File.class, where, tagList, "order by rating desc", size == null ? 1000 : size));
		return mv;
	}

	@RequestMapping(value = { "/file/{id}" }, method = RequestMethod.GET)
	public void read(@RequestParam(value = "type", required = false) String mimeType, @PathVariable("id") Long id, HttpServletResponse response) {
		File file = fileDAO.read(id, File.class);
		if (imagetypes.isEmpty()) {
			imagetypes.add("jpeg");
			imagetypes.add("png");
			imagetypes.add("jpg");
			imagetypes.add("tiff");
		}
		try {
			String fileName = file.getName().toLowerCase();
			String type = StringUtils.substringAfter(fileName, ".");
			String fileType = type != null ? StringUtils.lowerCase(type) : null;
			
			if ((file.getCategory() != null && file.getCategory().contains("image")) || imagetypes.contains(fileType)) {
				response.setHeader("Cache-Control", "max-age=3600");
				response.setContentType("image/" + file.getType());
				Calendar calendarInstance = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				calendarInstance.add(Calendar.HOUR_OF_DAY, 24 * 5);// add 24 hour from now x 5 days
				response.setDateHeader("Expires", calendarInstance.getTimeInMillis());
			} else if ("html".contains(fileType) || "htm".contains(fileType)) {
				response.setHeader("Cache-Control", "max-age=3600");
				response.setContentType("text/html");
			} else {
				response.setContentType("plain/text");
				response.setHeader("Content-Disposition", "inline; filename=" + file.getName() + "-file.txt");
			}

			if (mimeType != null) {
				response.setContentType(type); //Content-Type: application/json, text/javascript for jsonp etc
			}
			response.setHeader("Access-Control-Allow-Origin", "*"); // this is to avoid cross domain issues

			response.getOutputStream().write(file.getContent().getBytes());
			response.flushBuffer();
		} catch (IOException e) {
			logger.error(e, e);
		}
	}

	@RequestMapping(value = { "/file/{category}/random" }, method = RequestMethod.GET)
	public ModelAndView showRandom(@PathVariable("category") String category) {
		ModelAndView mv = new ModelAndView("file");
		List<File> files = null;
		if (files == null) {
			files = fileDAO.read(File.class, "category =='" + category + "'", "", 1000);
		}
		int random = RandomUtils.nextInt() % files.size();
		mv.addObject("file", files.get(random));
		return mv;
	}

	@RequestMapping(value = { "/file/edit" }, method = RequestMethod.POST)
	public ModelAndView update(File file) {
		fileDAO.update(file, File.class);
		ModelAndView mv = new ModelAndView("redirect:/file/admin");
		mv.addObject("message", "Updated!!");
		mv.addObject("files", fileDAO.read(File.class, 1000));
		return mv;
	}

	@RequestMapping(value = { "/file/edit/{fileId}" }, method = RequestMethod.GET)
	public ModelAndView updateFileView(@RequestParam(value = "password", required = false) String password, @PathVariable("fileId") long fileId, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("file/upload");
		if (!validateUser.validate(ValidateUser.USER_ADMIN, password, request.getSession(true))) {
			mv.setViewName("forward:/file/view");
			mv.addObject("message", "Invalid admin password");
			return mv;
		}
		File file = fileDAO.read(fileId, File.class);
		mv.addObject("file", file);
		mv.addObject("action", "edit");
		return mv;
	}

	@RequestMapping(value = { "/file/save/{fileId}" }, method = RequestMethod.POST)
	protected ModelAndView updateFile(HttpServletRequest request) throws Exception {

		Map<String, Object> attributes = new HashMap<String, Object>();
		File file = WebUtil.getInstance().getFile(request);
		fileDAO.update(file, file.getId(), File.class);

		attributes.put("message", "Thank you! You have now edited your file " + file.getName() + " and can view <a href=\"/file/" + file.getId() + "\">here</a>");
		ModelAndView layoutMandV = new ModelAndView();
		layoutMandV.addAllObjects(attributes);
		layoutMandV.setViewName("forward:/file/view");
		return layoutMandV;
	}

	@RequestMapping(value = { "/file/rate/{fileId}/{rating}" }, method = RequestMethod.GET)
	public ModelAndView updateRating(@PathVariable("rating") int rating, @PathVariable("fileId") long fileId, HttpServletRequest request) {
		File file = fileDAO.read(fileId, File.class);

		int totalRating = WebUtil.getInstance().computeTotalRating(rating, file.getRating());
		String ip = request.getRemoteHost();
		if (file.getIps() != null && !file.getIps().contains(ip)) {
			file.setRating(totalRating);
			file.setUsersRated(file.getUsersRated() > 0 ? file.getUsersRated() : file.getUsersRated() + 1);
			file.setIps(file.getIps() + ":" + ip);
		} else if (file.getIps() == null) {
			file.setRating(totalRating);
			file.setUsersRated(file.getUsersRated() > 0 ? file.getUsersRated() : file.getUsersRated() + 1);
			file.setIps(ip);
		}

		fileDAO.update(file, file.getId(), File.class);
		ModelAndView mv = new ModelAndView("redirect:/file/" + fileId);
		return mv;
	}

	private void getDistinctFiles(List<File> files, String field) {
		List<String> values = new ArrayList<String>();
		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
			File file = iterator.next();
			String fieldValue = null;
			try {
				fieldValue = BeanUtils.getProperty(file, field);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (values.contains(fieldValue)) {
				iterator.remove();
			} else {
				values.add(file.getCategory());
			}
		}
	}
}
