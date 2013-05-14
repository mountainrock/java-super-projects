package search.solr;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class SolrInsertIndexPostRequest {
	public static void main(String[] args) throws Exception
	{

		String strToAdd = "<add>" + "<doc >" + "<field name=\"id\">05993</field>" + "<field name=\"text\">testing2</field>" + "</doc>" + "</add>";
		HttpClient client = new HttpClient();

		PostMethod postMethod = new PostMethod("http://localhost:8080/solr/update/");
		postMethod.setRequestHeader("Content-type", "text/xml");
		strToAdd = "<commit/>";
		postMethod.setRequestBody(strToAdd);
		client.executeMethod(postMethod);

	}
}
