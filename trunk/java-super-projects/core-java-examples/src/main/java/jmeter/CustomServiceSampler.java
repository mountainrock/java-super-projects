package jmeter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
/**
 * Using jmeter 2.3.4
 * @author Sandeep
 *
 */
public class CustomServiceSampler implements JavaSamplerClient {

	private static final String DEFAULT_SERVICE_URI = "http://localhost:8080/test-service/";
	public static final String DEFAULT_test_TEMPLATE_PATH = "c:/load-test/test-sample.xml";
	public static final String DEFAULT_DATA_ENCODING = "text/xml;charset=utf-8";

	private static Log logger = LogFactory.getLog(CustomServiceSampler.class);

	@Override
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("SERVICE_URI", DEFAULT_SERVICE_URI);
        params.addArgument("test_TEMPLATE_PATH", DEFAULT_test_TEMPLATE_PATH);
        params.addArgument("DATA_ENCODING", DEFAULT_DATA_ENCODING);
		return params;
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();
		
		HttpClient client = new HttpClient();
		String url = context.getParameter("SERVICE_URI", DEFAULT_SERVICE_URI);
		String templatePath= context.getParameter("test_TEMPLATE_PATH", DEFAULT_test_TEMPLATE_PATH);
		String contentType= context.getParameter("DATA_ENCODING", DEFAULT_DATA_ENCODING);
		
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Content-Type", contentType);
		try {
			String template = FileUtils.readFileToString(new File(templatePath));
			String testId = getUniqueId();
			result.setSampleLabel("Post test - " + testId);
			logger.info("Sending POST request to "+url +" with testId :"+testId);
			String testXml = template.replaceAll("\\{ID\\}", testId);
			testXml = testXml.replaceAll("\\{DATE\\}", new Date() + "");

			RequestEntity requestEntity = new StringRequestEntity(testXml);
			method.setRequestEntity(requestEntity);

			result.setRequestHeaders(ToStringBuilder.reflectionToString(method.getRequestHeaders()));
			result.setSamplerData(testXml);
			
			result.sampleStart();
			client.executeMethod(method);
			String response = new String(method.getResponseBody());
			result.sampleEnd();
			int code = method.getStatusCode();
			logger.info("Recived response .. status code "+code +" response xml :"+response);
			boolean success= code ==201 ? true:false;
			result.setSuccessful(success);
			result.setResponseData(response);
			result.setResponseCode(code+"");
			result.setResponseMessage(response);
			
			
		} catch (Exception e) {
			logger.error(e,e);
			result.sampleEnd();
			result.setResponseMessage(e.getMessage());
			result.setSuccessful(false);
		}
		return result;
	}

	protected String getUniqueId() {
		try {
			Thread.sleep(10); //just to get a unique milli sec.
		} catch (InterruptedException e) {
			logger.warn(e,e);
		}
		String now = System.currentTimeMillis()+"";
		String testId = now.substring(now.length()-10, now.length());
		return testId;
	}

	@Override
	public void setupTest(JavaSamplerContext arg0) {
	}

	@Override
	public void teardownTest(JavaSamplerContext arg0) {
	}
}
