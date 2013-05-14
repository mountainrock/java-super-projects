package search.example.srch.solr;

import search.example.srch.*;

public class SolrSearchEngineUtil {

	private static SearchEngine searchEngine;

	public static void addDocument(Document doc) throws SearchException
	{

		searchEngine.getWriter().addDocument(doc);
	}

	public static void deleteDocument(String uid) throws SearchException
	{

		searchEngine.getWriter().deleteDocument(uid);
	}

	public static Hits search(Query query, int start, int end) throws SearchException
	{

		return searchEngine.getSearcher().search(query, start, end);
	}

	public static Hits search(Query query, Sort sort, int start, int end) throws SearchException
	{

		return searchEngine.getSearcher().search(query, sort, start, end);
	}

	public static void updateDocument(String uid, Document doc) throws SearchException
	{

		searchEngine.getWriter().updateDocument(uid, doc);
	}

	public SolrSearchEngineUtil(SearchEngine searchEngine) throws Exception {
		SolrSearchEngineUtil.searchEngine = searchEngine;

	}

}