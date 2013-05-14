package search.example.srch.solr;

import java.io.StringReader;

import search.example.srch.*;
import search.example.srch.impl.*;
import search.example.util.*;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SolrIndexSearcherImpl implements IndexSearcher {
	private static Log logger = LogFactory.getLog(SolrIndexSearcherImpl.class.getName());

	private String serverURL;

	public String getServerURL()
	{
		return serverURL;
	}

	public void setServerURL(String serverURL)
	{
		this.serverURL = serverURL;
	}

	public Hits search(Query query, int start, int end) throws SearchException
	{

		return search(query, null, start, end);
	}

	public Hits search(Query query, Sort sort, int start, int end) throws SearchException
	{

		try {
			// TODO: use embedded solr client impl
			String url = HttpUtil.addParameter(serverURL, "q", query.toString());

			url = HttpUtil.addParameter(url, "fl", "score");
			// TODO:
			logger.debug("Sending GET request " + url);

			String xml = HttpUtil.requestURL(url);

			logger.debug("Received " + xml);
			return subset(xml, start, end);
		} catch (Exception e) {
			logger.error("Error sending request to Solr", e);

			throw new SearchException(e);
		}
	}

	protected Hits subset(String xml, int start, int end) throws Exception
	{
		long startTime = System.currentTimeMillis();

		Hits subset = new HitsImpl();

		SAXReader reader = new SAXReader();

		org.dom4j.Document xmlDoc = reader.read(new StringReader(xml));

		Element root = xmlDoc.getRootElement();

		Element resultEl = root.element("result");

		int length = GetterUtil.getInteger(resultEl.attributeValue("numFound"));

		float maxScore = GetterUtil.getFloat(resultEl.attributeValue("maxScore"));

		if ((start > -1) && (start <= end)) {
			if (end > length) {
				end = length;
			}

			int subsetTotal = end - start;

			Document[] subsetDocs = new DocumentImpl[subsetTotal];
			float[] subsetScores = new float[subsetTotal];

			List<Element> docsEl = resultEl.elements();

			int j = 0;

			for (int i = start; i < end; i++, j++) {
				Element docEl = docsEl.get(i);

				Document doc = new DocumentImpl();

				List<Element> fieldEls = docEl.elements();

				for (Element fieldEl : fieldEls) {
					Field field = new Field(fieldEl.attributeValue("name"), fieldEl.getText(), false);

					doc.add(field);
				}

				float score = GetterUtil.getFloat(doc.get("score"));

				subsetDocs[j] = doc;
				subsetScores[j] = score / maxScore;
			}

			subset.setLength(length);
			subset.setDocs(subsetDocs);
			subset.setScores(subsetScores);
			subset.setStart(startTime);

			float searchTime = (float) (System.currentTimeMillis() - startTime) / StringConstants.SECOND;

			subset.setSearchTime(searchTime);
		}

		return subset;
	}

}
