package com.bri8.supermag.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.service.SearchService;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

@Controller
public class SearchController extends BaseController{
	@Autowired SearchService searchService;
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public ModelAndView search(@RequestParam("q")String query )
	{
		Results<ScoredDocument> resultIssue = searchService.searchIssue(query);
		
		ModelAndView mv = getDefaultModelAndView("search/show");
		mv.addObject("result", resultIssue);
		return mv;
	}

}
