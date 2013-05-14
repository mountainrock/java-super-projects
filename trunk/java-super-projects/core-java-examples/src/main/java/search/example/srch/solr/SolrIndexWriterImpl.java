package search.example.srch.solr;

import search.example.srch.*;
import search.example.util.*;
import java.util.Collection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SolrIndexWriterImpl implements IndexWriter {

	private static final String _COMMIT_XML = "<commit />";

	private static Log _log = LogFactory.getLog(SolrIndexWriterImpl.class);

	private String serverURL;

	public void addDocument(Document doc) throws SearchException
	{

		org.dom4j.Document xml = DocumentHelper.createDocument();

		Element addEl = xml.addElement("add");

		addFieldEls(addEl, doc);

		try {
			write(xml);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	public void deleteDocument(String uid) throws SearchException
	{

		org.dom4j.Document xml = DocumentHelper.createDocument();

		Element deleteEl = xml.addElement("delete");

		Element idEl = deleteEl.addElement("id");

		idEl.setText(uid);

		try {
			write(xml);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	public void updateDocument(String uid, Document doc) throws SearchException
	{

		org.dom4j.Document xml = DocumentHelper.createDocument();

		Element updateEl = xml.addElement("update");

		addFieldEls(updateEl, doc);

		try {
			write(xml);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	protected void addFieldEls(Element el, Document doc)
	{
		Element docEl = el.addElement("doc");

		Collection<Field> fields = doc.getFields().values();

		for (Field field : fields) {
			String name = field.getName();
			String value = field.getValue();

			if (value != null) {
				Element fieldEl = docEl.addElement("field");

				fieldEl.addAttribute("name", name);
				fieldEl.addText(value);
			}
		}
	}

	protected void submitRequest(String xml) throws Exception
	{
		PostMethod method = null;

		// TODO: replace with solr embedded client.
		try {
			HttpClient client = new HttpClient();

			method = new PostMethod(serverURL);

			RequestEntity entity = new StringRequestEntity(xml, StringConstants.TEXT_XML, StringConstants.UTF8);

			method.setRequestEntity(entity);

			client.executeMethod(method);
		} finally {
			try {
				if (method != null) {
					method.releaseConnection();
				}
			} catch (Exception e) {
				_log.error(e, e);
			}
		}
	}

	protected void write(org.dom4j.Document xml) throws Exception
	{
		submitRequest(xml.asXML());
		submitRequest(_COMMIT_XML);
	}

	public String getServerURL()
	{
		return serverURL;
	}

	public void setServerURL(String serverURL)
	{
		this.serverURL = serverURL;
	}

}