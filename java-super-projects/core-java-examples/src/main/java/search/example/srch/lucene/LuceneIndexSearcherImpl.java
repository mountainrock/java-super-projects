package search.example.srch.lucene;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;

import search.example.srch.IndexSearcher;
import search.example.srch.Query;
import search.example.srch.Hits;
import search.example.srch.SearchException;
import search.example.srch.Sort;
import search.example.srch.impl.DocumentImpl;
import search.example.srch.impl.HitsImpl;
import search.example.srch.impl.SearchContext;
import search.example.util.SearchContextParams;

public class LuceneIndexSearcherImpl implements IndexSearcher {
	private static Log logger = LogFactory.getLog(LuceneIndexSearcherImpl.class.getName());
	private SearchContext searchContext;
	org.apache.lucene.search.IndexSearcher indexSearcher;
	Analyzer analyzer;

	public LuceneIndexSearcherImpl(SearchContext searchContext) throws SearchException {
		try {
			this.searchContext = searchContext;
			this.indexSearcher = new org.apache.lucene.search.IndexSearcher(searchContext.getParamAsString(SearchContextParams.KEY_FS_INDEX_DIR));
			this.analyzer = new StandardAnalyzer();
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	public Hits search(Query query, int start, int end) throws SearchException
	{

		return search(query, null, start, end);
	}

	public Hits search(Query query, Sort sort, int start, int end) throws SearchException
	{
		QueryParser queryParser = new QueryParser("content", analyzer);
		org.apache.lucene.search.Query lQuery = null;
		Hits result = new HitsImpl();

		try {
			lQuery = queryParser.parse(query.toString());
			org.apache.lucene.search.Hits hits = indexSearcher.search(lQuery);
			for (int i = 0; i < hits.length(); i++) {
				// TODO: transform result
				DocumentImpl document;

				logger.info("Found : " + hits.doc(i).getFields().get(0));
			}
		} catch (Exception e) {
			throw new SearchException(e);
		}

		return result;

	}

}
