package search.example.srch.lucene;

import search.example.srch.SearchException;
import search.example.srch.impl.*;

public class LuceneSearchEngineImpl extends SearchEngineImpl {
	private static LuceneSearchEngineImpl _instance;

	private SearchContext searchContext;

	private LuceneSearchEngineImpl() throws SearchException {
	}

	private LuceneSearchEngineImpl(SearchContext searchContext) throws SearchException {
		this.searchContext = searchContext;
		setSearcher(new LuceneIndexSearcherImpl(searchContext));
		setWriter(new LuceneIndexWriterImpl(searchContext));
	}

	public static LuceneSearchEngineImpl getInstance(SearchContext searchContext) throws SearchException
	{
		if (_instance == null) {
			_instance = new LuceneSearchEngineImpl(searchContext);
		}
		return _instance;
	}

}
