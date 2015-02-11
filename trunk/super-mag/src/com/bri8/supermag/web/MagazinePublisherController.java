package com.bri8.supermag.web;

import static com.bri8.supermag.util.WebConstants.HTTP_SESSION_KEY_USER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.IssuePage;
import com.bri8.supermag.model.IssueStatus;
import com.bri8.supermag.model.Magazine;
import com.bri8.supermag.model.MagazineIssues;
import com.bri8.supermag.model.User;
import com.bri8.supermag.service.MagazineService;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@Controller("magazineController")
public class MagazinePublisherController extends BaseController {
	@Autowired
	MagazineService magazineService;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	private static Log logger = LogFactory.getLog(MagazinePublisherController.class);

	@RequestMapping(value = { "/magazine/showAdd" }, method = { RequestMethod.GET, RequestMethod.POST })
	protected ModelAndView showAddMagazine(@RequestParam(value = "magazineId", required = false) Long magazineId, @RequestParam(value = "status", required = false) String status) throws Exception {
		ModelAndView mv = getDefaultModelAndView("magazine/showAdd");
		if (magazineId != null) {
			Magazine magazine = magazineService.getMagazine(magazineId);
			mv.addObject("magazine", magazine);

			if ("new".equalsIgnoreCase(status)) {
				mv.addObject("message", "New magazine created sucessfully : " + magazine.getMagazineId());
			} else if ("failed".equalsIgnoreCase(status)) {
				mv.addObject("error", "Failed to save magazine!!");
			} else if ("update".equalsIgnoreCase(status)) {
				mv.addObject("message", "Updated magazine sucessfully : " + magazine.getMagazineId());
			}
		}
		return mv;
	}

	@RequestMapping(value = { "/magazine/create" }, method = RequestMethod.POST)
	public ModelAndView create(@RequestParam("action") String action, Magazine magazine, HttpServletRequest request) {

		ModelAndView mv = null;
		if ("save".equalsIgnoreCase(action)) {
			User user = (User) request.getSession().getAttribute(HTTP_SESSION_KEY_USER);
			magazine.setUserId(user.getUserId());
			String status = "";
			if (magazine.getMagazineId() == null) {
				magazineService.createMagazine(magazine);
				status = "new";
			} else {
				magazineService.updateMagazine(magazine);
				status = "update";
			}
			mv = new ModelAndView("redirect:/magazine/showAdd?magazineId=" + magazine.getMagazineId());
			if (magazine != null && magazine.getMagazineId() != null) {
				mv.addObject("status", status);
			} else {
				mv.addObject("status", "failed");
			}
		} else if ("next".equalsIgnoreCase(action)) {
			mv = new ModelAndView("redirect:/magazine/showAddIssue/" + magazine.getMagazineId());
		}

		return mv;
	}

	@RequestMapping(value = { "/magazine/list" }, method = RequestMethod.GET)
	protected ModelAndView list(HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(HTTP_SESSION_KEY_USER);
		List<MagazineIssues> magazines = magazineService.listMagazineIssues(user.getUserId());
		ModelAndView mv = getDefaultModelAndView("magazine/list");
		mv.addObject("magazines", magazines);

		return mv;
	}

	@RequestMapping(value = { "/magazine/showAddIssue/{magazineId}" }, method = RequestMethod.GET)
	protected ModelAndView showAddIssue(@PathVariable("magazineId") Long magazineId) throws Exception {
		ModelAndView mv = getDefaultModelAndView("magazine/issue/showAddIssue");
		mv.addObject("magazineId", magazineId);
		return mv;
	}

	@RequestMapping(value = { "/magazine/createIssue" }, method = RequestMethod.POST)
	public ModelAndView createIssue(Issue issue, HttpServletRequest request, HttpServletResponse response) throws IOException {
		issue.setStatus(IssueStatus.Uploaded.name());
		magazineService.createIssue(issue);
		ModelAndView mv = getDefaultModelAndView("magazine/showAdd");
		if (issue != null && issue.getIssueId() != null) {
			mv.addObject("message", "Issue created sucessfully : " + issue.getIssueId());
			mv.addObject("issue", issue);
			response.sendRedirect(String.format("/magazine/showUploadIssue/%s/%s", issue.getMagazineId(), issue.getIssueId()));
		} else {
			mv.addObject("error", "Failed to create issue!!");
		}

		return mv;
	}

	@RequestMapping(value = { "/magazine/showUploadIssue/{magazineId}/{issueId}" }, method = RequestMethod.GET)
	protected ModelAndView showUploadIssue(@PathVariable("magazineId") Long magazineId, @PathVariable("issueId") Long issueId, HttpServletRequest req) throws Exception {
		ModelAndView mv = getDefaultModelAndView("magazine/issue/showUploadIssue");

		String blobUploadPath = String.format("/magazine/updateIssueImageBlobKey?magazineId=%s&issueId=%s", magazineId, issueId);
		String uploadUrl = blobstoreService.createUploadUrl(blobUploadPath);

		mv.addObject("magazineId", magazineId);
		mv.addObject("issueId", issueId);
		mv.addObject("uploadIssueUrl", uploadUrl);
		List<IssuePage> issuePages = magazineService.getIssuePages(issueId);
		mv.addObject("issuePages", issuePages);

		return mv;
	}

	@RequestMapping(value = { "/magazine/getBlob" }, method = RequestMethod.GET)
	protected void getBlob(@RequestParam("blobKey") String blobKeyStr, HttpServletResponse res) throws Exception {
		blobstoreService.serve(new BlobKey(blobKeyStr), res);
	}

	// preview
	@RequestMapping(value = { "/magazine/preview/{issueId}" }, method = RequestMethod.GET)
	protected ModelAndView previewIssue(@PathVariable("issueId") Long issueId, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<IssuePage> issuePages = magazineService.getIssuePages(issueId);
		ModelAndView mv = getDefaultModelAndViewNoLayout("magazine/issue/preview");
		mv.addObject("issuePages", issuePages);
		return mv;

	}

	@RequestMapping(value = { "/magazine/deleteIssuePage/{issuePageId}" }, method = RequestMethod.GET)
	protected ModelAndView deleteIssuePage(@PathVariable("issuePageId") Long issuePageId) {
		// delete entity
		IssuePage issuePage = magazineService.deleteIssuePage(issuePageId);
		// cleanup blobstore
		BlobKey blobKeyMainImg = new BlobKey(issuePage.getBlobKey());
		BlobKey blobKeyThumbImg = new BlobKey(issuePage.getBlobKeyThumbnail());
		blobstoreService.delete(blobKeyMainImg, blobKeyThumbImg);
		ModelAndView mv = getDefaultModelAndViewNoLayout("result");
		mv.addObject("result", new ArrayList<String>().add("Deleted " + issuePageId));
		return mv;
	}

}
