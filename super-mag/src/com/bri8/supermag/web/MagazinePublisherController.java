package com.bri8.supermag.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.bri8.supermag.util.WebConstants;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

@Controller("magazineController")
public class MagazinePublisherController extends BaseController {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	GcsService gcsService = GcsServiceFactory.createGcsService();
	
	private static Log logger = LogFactory.getLog(MagazinePublisherController.class);

	@RequestMapping(value = { "/publisher/dashboard" }, method = RequestMethod.GET)
	protected ModelAndView showPublisherDashboard(HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/publisher" );
			return null;
		}
		ModelAndView mv = getDefaultModelAndView("user/publisher-dashboard");

		return mv;
	}
	
	// step 1
	@RequestMapping(value = { "/magazine/showAdd" }, method = { RequestMethod.GET, RequestMethod.POST })
	protected ModelAndView showAddMagazine(@RequestParam(value = "magazineId", required = false) Long magazineId, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "issueId", required = false) Long issueId) throws Exception {
		ModelAndView mv = getDefaultModelAndView("magazine/showAdd");
		if (magazineId != null) { // load for edit
			Magazine magazine = magazineService.getMagazine(magazineId);
			mv.addObject("magazine", magazine);
			if(issueId!=null){
				mv.addObject("issue", magazineService.getIssue(issueId));
			}
			if ("new".equalsIgnoreCase(status)) {
				mv.addObject("message", "New magazine created sucessfully : " + magazine.getMagazineId());
			} else if ("update".equalsIgnoreCase(status)) {
				mv.addObject("message", "Updated magazine sucessfully : " + magazine.getMagazineId());
			} else if ("failed".equalsIgnoreCase(status)) {
				mv.addObject("error", "Failed to save magazine!");
			}
		}
		return mv;
	}

	@RequestMapping(value = { "/magazine/create" }, method = RequestMethod.POST)
	public ModelAndView createOrNext(@RequestParam("action") String action, Magazine magazine, @RequestParam(value = "issueId", required = false) Long issueId,  HttpServletRequest request) {

		ModelAndView mv = null;
		if ("save".equalsIgnoreCase(action)) {
			User user = getUser(request);
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
			mv = new ModelAndView(String.format("redirect:/magazine/showAddIssue/%s?issueId=%s", magazine.getMagazineId(), issueId ==null? "" : issueId));
		}

		return mv;
	}

	// step 2
	@RequestMapping(value = { "/magazine/showAddIssue/{magazineId}" }, method = RequestMethod.GET)
	protected ModelAndView showAddIssue(@PathVariable("magazineId") Long magazineId, @RequestParam(value = "issueId", required = false) Long issueId,
			@RequestParam(value = "status", required = false) String status) throws Exception {
		ModelAndView mv = getDefaultModelAndView("magazine/issue/showAddIssue");
		if (issueId != null) { // load for edit issue
			Issue issue = magazineService.getIssue(issueId);
			mv.addObject("issue", issue);

			if ("new".equalsIgnoreCase(status)) {
				mv.addObject("message", "New issue created sucessfully : " + issue.getIssueId());
			} else if ("update".equalsIgnoreCase(status)) {
				mv.addObject("message", "Updated issue sucessfully : " + issue.getIssueId());
			} else if ("failed".equalsIgnoreCase(status)) {
				mv.addObject("error", "Failed to save issue!");
			}
		}
		mv.addObject("magazineId", magazineId);
		return mv;
	}

	@RequestMapping(value = { "/magazine/createIssue" }, method = RequestMethod.POST)
	public ModelAndView createIssue(@RequestParam("action") String action, Issue issue, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mv = null;
		String status = "";
		if ("save".equalsIgnoreCase(action)) {
			if (issue.getIssueId() == null) {
				issue.setStatus(IssueStatus.Created.name());
				issue.setCoverPageNumber(1);
				issue.setPreviewPages("1,3,5");
				magazineService.createIssue(issue);
				status = "new";
			} else {
				magazineService.updateIssue(issue);
				status = "update";
			}

			mv = new ModelAndView(String.format("redirect:/magazine/showAddIssue/%s?issueId=%s", issue.getMagazineId(), issue.getIssueId()));
			if (issue != null && issue.getMagazineId() != null) {
				mv.addObject("status", status);
			} else {
				mv.addObject("status", "failed");
			}
		} else if ("next".equalsIgnoreCase(action)) {
			mv = new ModelAndView(String.format("redirect:/magazine/showUploadIssue/%s/%s", issue.getMagazineId(), issue.getIssueId()));
		}

		return mv;
	}

	// step 3
	@RequestMapping(value = { "/magazine/showUploadIssue/{magazineId}/{issueId}" }, method = RequestMethod.GET)
	protected ModelAndView showUploadIssue(@RequestParam(value="status", required=false) String status, @PathVariable("magazineId") Long magazineId, @PathVariable("issueId") Long issueId, HttpServletRequest req) throws Exception {
		ModelAndView mv = getDefaultModelAndView("magazine/issue/showUploadIssue");

		String blobUploadPath = String.format("/magazine/updateIssueImageBlobKey?magazineId=%s&issueId=%s", magazineId, issueId);
		String uploadUrl = blobstoreService.createUploadUrl(blobUploadPath);
		Issue issue = magazineService.getIssue(issueId);
		
		if ("save".equalsIgnoreCase(status)) {
			mv.addObject("message", "Updated issue sucessfully : " + issue.getIssueId());
		}
		mv.addObject("issue", issue);
		mv.addObject("uploadIssueUrl", uploadUrl);
		List<IssuePage> issuePages = magazineService.getIssuePages(issueId);
		mv.addObject("issuePages", issuePages);

		return mv;
	}

	@RequestMapping(value = { "/magazine/saveUploadIssue" }, method = RequestMethod.POST)
	protected ModelAndView saveUploadIssue( @RequestParam("action") String action, Issue issue, HttpServletRequest req) throws Exception {
		ModelAndView mv=null;
		Issue issueFromStore = magazineService.getIssue(issue.getIssueId());
		if ("save".equalsIgnoreCase(action)) {
			issueFromStore.setCoverPageNumber(issue.getCoverPageNumber());
			issueFromStore.setPreviewPages(issue.getPreviewPages());
			issueFromStore.setStatus(IssueStatus.Uploaded.name());
			
			magazineService.updateIssue(issueFromStore);
			 mv = new ModelAndView(String.format("redirect:/magazine/showUploadIssue/%s/%s?status=save",issueFromStore.getMagazineId(), issueFromStore.getIssueId()));
		} else if ("next".equalsIgnoreCase(action)) {
			 mv = new ModelAndView(String.format("redirect:/magazine/showPublish/%s/%s",issueFromStore.getMagazineId(), issueFromStore.getIssueId()));
		}

		return mv;
	}

	//step 4
	@RequestMapping(value = { "/magazine/showPublish/{magazineId}/{issueId}" }, method = RequestMethod.GET)
	protected ModelAndView publishView(@RequestParam(value="status", required=false) String status, @PathVariable("magazineId") Long magazineId, @PathVariable("issueId") Long issueId, HttpServletRequest req) throws Exception {
		ModelAndView mv = getDefaultModelAndView("magazine/publish/showPublish");
		Issue issueFromStore = magazineService.getIssue(issueId);
		Magazine magazine = magazineService.getMagazine(issueFromStore.getMagazineId());
		if ("publish".equalsIgnoreCase(status)) {
			mv.addObject("message", String.format("Published sucessfully(id: %s, status : %s). The issue will be reviewed by admin and made live on %s : ", issueFromStore.getIssueId(), issueFromStore.getStatus(), issueFromStore.getPublishingDate()));
		}
		mv.addObject("issue", issueFromStore);
		mv.addObject("magazine", magazine);
		IssuePage issueCoverPage = magazineService.getIssueFrontPageByIssueId(issueFromStore);
		mv.addObject("issuePage", issueCoverPage);

		return mv;
	}
	
	@RequestMapping(value = { "/magazine/publish/{magazineId}/{issueId}" }, method = RequestMethod.POST)
	protected ModelAndView publish(@PathVariable("magazineId") Long magazineId, @PathVariable("issueId") Long issueId, HttpServletRequest req) throws Exception {
		magazineService.publish(issueId, getUser(req).getUserId());
		ModelAndView  mv = new ModelAndView(String.format("redirect:/magazine/showPublish/%s/%s?status=publish",magazineId, issueId));
		return mv;
	}
	
	
	@RequestMapping(value = { "/magazine/getBlob" }, method = RequestMethod.GET)
	protected void getBlob(@RequestParam("fileName") String fileName, HttpServletResponse res) throws Exception {
		BlobKey blobKey = blobstoreService.createGsBlobKey(String.format("/gs/%s/%s", WebConstants.BUCKETNAME , fileName));
		res.setContentType("image/jpg");
		blobstoreService.serve(blobKey, res);
	}

	// preview
	@RequestMapping(value = { "/magazine/preview/{issueId}" }, method = RequestMethod.GET)
	protected ModelAndView previewIssue(@PathVariable("issueId") Long issueId, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<IssuePage> issuePages = magazineService.getIssuePages(issueId);
		ModelAndView mv = getDefaultModelAndViewNoLayout("magazine/issue/preview");
		mv.addObject("issuePages", issuePages);
		return mv;

	}

	@RequestMapping(value = { "/magazine/list" }, method = RequestMethod.GET)
	protected ModelAndView list(HttpServletRequest request) throws Exception {
		User user = getUser(request);
		List<MagazineIssues> magazines = magazineService.listMagazineIssues(user.getUserId());
		ModelAndView mv = getDefaultModelAndView("magazine/list");
		mv.addObject("magazines", magazines);

		return mv;
	}


	@RequestMapping(value = { "/magazine/deleteIssuePage/{issuePageId}" }, method = RequestMethod.GET)
	protected ModelAndView deleteIssuePage(@PathVariable("issuePageId") Long issuePageId) {
		// delete entity
		IssuePage issuePage = magazineService.deleteIssuePage(issuePageId);
		// cleanup gcs
		try {
			gcsService.delete(new GcsFilename(WebConstants.BUCKETNAME, issuePage.getFileName()));
			gcsService.delete(new GcsFilename(WebConstants.BUCKETNAME, issuePage.getFileNameThumbnail()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ModelAndView mv = getDefaultModelAndViewNoLayout("result");
		mv.addObject("result", new ArrayList<String>().add("Deleted " + issuePageId));
		return mv;
	}

}
