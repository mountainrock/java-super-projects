package search.example.srch.lucene;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.dom4j.Element;

import search.example.util.*;
import search.example.srch.Document;
import search.example.srch.Field;
import search.example.srch.IndexWriter;
import search.example.srch.SearchException;
import search.example.srch.impl.SearchContext;

public class LuceneIndexWriterImpl implements IndexWriter {

	private static Log logger = LogFactory.getLog(LuceneIndexWriterImpl.class);
	org.apache.lucene.index.IndexWriter indexWriter;
	private SearchContext searchContext;

	public LuceneIndexWriterImpl(SearchContext searchContext) throws SearchException {
		try {
			this.searchContext = searchContext;
			Directory fsDirectory = FSDirectory.getDirectory(searchContext.getParamAsString(SearchContextParams.KEY_FS_INDEX_DIR));
			Analyzer analyzer = new StandardAnalyzer();
			indexWriter = new org.apache.lucene.index.IndexWriter(fsDirectory, analyzer, false, MaxFieldLength.UNLIMITED);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	public void addDocument(Document doc) throws SearchException
	{
		try {
			// File system for now.
			logger.info("Indexing document : " + doc);

			// add docs now
			Map<String, Field> fields = doc.getFields();
			Set<Entry<String, Field>> entrySet = fields.entrySet();
			for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
				org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
				Entry<String, Field> entry = (Entry<String, Field>) iterator.next();
				document.add(new org.apache.lucene.document.Field(entry.getKey(), entry.getValue().getValue(), org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.NO));
				indexWriter.addDocument(document);
			}
			indexWriter.optimize();
			indexWriter.close();
		} catch (IOException e) {
			throw new SearchException(e);
		}

	}

	public void deleteDocument(String uid) throws SearchException
	{
		throw new RuntimeException("Not yet implemented!!");
	}

	public void updateDocument(String uid, Document doc) throws SearchException
	{
		throw new RuntimeException("Not yet implemented!!");
	}

	protected void addFieldEls(Element el, Document doc)
	{
		throw new RuntimeException("Not yet implemented!!");
	}

}