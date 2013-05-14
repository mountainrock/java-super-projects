package search.example.srch.impl;

import search.example.srch.Query;

public class QueryTranslator {

	public static org.apache.lucene.search.Query translate(Query query)
	{
		if (query instanceof BooleanQueryImpl) {
			return ((BooleanQueryImpl) query).getBooleanQuery();
		} else {
			return null;
		}
	}

}