package search.example.srch.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexWriter;

import search.example.util.LuceneUtil;

public class IndexWriterFactory {

	private static Log logger = LogFactory.getLog(IndexWriterFactory.class.getName());

	public IndexWriter getWriter(long companyId, boolean create)
	{
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(LuceneUtil.getLuceneDir(companyId), LuceneUtil.getAnalyzer(), create);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return indexWriter;
	}

	public void write(IndexWriter writer)
	{
		try {
			writer.optimize();
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

}
