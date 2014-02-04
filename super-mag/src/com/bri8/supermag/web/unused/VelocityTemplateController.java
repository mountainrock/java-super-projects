package com.bri8.supermag.web.unused;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.parser.node.ASTprocess;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;


@Controller("velocityTemplateController")
public class VelocityTemplateController implements ServletContextAware {
	VelocityEngine ve;
	String viewName = "velocity-manage";

	ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@RequestMapping(value = "/view-template", method = RequestMethod.GET)
	protected ModelAndView show(@RequestParam("template") String velocityTemplate) throws Exception {
		//FIXME: fix how to get webapp context in GAE
		if (ve == null) {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			ve = (VelocityEngine) webApplicationContext.getBean("velocityEngine");
		}
		String data = getTemplate(velocityTemplate);
		VelocityCommand form = new VelocityCommand();
		data = HtmlUtils.htmlEscape(data);
		form.setVelocity(data);
		form.setTemplateName(velocityTemplate);
		ModelAndView mv = new ModelAndView(viewName);
		if (data == null) {
			form.setResult("Template not found : " + velocityTemplate);
		}
		return mv;
	}

	private String getTemplate(String velocityTemplate) throws Exception {
		Template template = ve.getTemplate(velocityTemplate);
		ASTprocess data2 = (ASTprocess) template.getData();
		return data2.literal();
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ModelAndView mv = new ModelAndView("velocity-edit");
		VelocityCommand form = (VelocityCommand) command;
		String result = doIt(form.getTemplateName(), form.getVelocity());
		form.setResult(result);
		mv.getModel().put("form", command);
		return mv;
	}

	private String doIt(String velocityTemplate, String content) throws IOException, Exception {
		Template template = ve.getTemplate(velocityTemplate);
		if (template == null) {
			return "Template not found : " + velocityTemplate;
		}
		return "";
	}

}
