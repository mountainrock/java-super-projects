package search.example.util;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import search.example.srch.impl.IndexWriterFactory;

public class LuceneUtil {

	public static void addExactTerm(BooleanQuery booleanQuery, String field, long value)
	{

		addExactTerm(booleanQuery, field, String.valueOf(value));
	}

	public static void addExactTerm(BooleanQuery booleanQuery, String field, String value)
	{

		// text = KeywordsUtil.escape(value);

		Query query = new TermQuery(new Term(field, value));

		booleanQuery.add(query, BooleanClause.Occur.SHOULD);
	}

	public static void addKeyword(Document doc, String field, double value)
	{
		addKeyword(doc, field, String.valueOf(value));
	}

	public static void addKeyword(Document doc, String field, long value)
	{
		addKeyword(doc, field, String.valueOf(value));
	}

	public static void addKeyword(Document doc, String field, String value)
	{
		if (value != null) {
			doc.add(getKeyword(field, value));
		}
	}

	public static Field getKeyword(String field, String keyword)
	{
		// keyword = KeywordsUtil.escape(keyword);

		Field fieldObj = new Field(field, keyword, Field.Store.YES, Field.Index.UN_TOKENIZED);

		// fieldObj.setBoost(0);

		return fieldObj;
	}

	public static void addKeyword(Document doc, String field, String[] values)
	{
		if (values == null) {
			return;
		}

		for (int i = 0; i < values.length; i++) {
			addKeyword(doc, field, values[i]);
		}
	}

	public static void addRequiredTerm(BooleanQuery booleanQuery, String field, long value)
	{

		addRequiredTerm(booleanQuery, field, String.valueOf(value));
	}

	public static void addRequiredTerm(BooleanQuery booleanQuery, String field, String value)
	{

		// text = KeywordsUtil.escape(value);

		Term term = new Term(field, value);
		TermQuery termQuery = new TermQuery(term);

		booleanQuery.add(termQuery, BooleanClause.Occur.MUST);
	}

	public static void addModifiedDate(Document doc)
	{
		doc.add(getDate(StringConstants.MODIFIED));
	}

	public static Field getDate(String field)
	{
		return getDate(field, new Date());
	}

	public static Field getDate(String field, Date date)
	{
		if (date == null) {
			return getDate(field);
		} else {
			return new Field(field, DateTools.dateToString(date, DateTools.Resolution.SECOND), Field.Store.YES, Field.Index.UN_TOKENIZED);
		}
	}

	public static void addTerm(BooleanQuery booleanQuery, String field, long value) throws ParseException
	{

		addTerm(booleanQuery, field, String.valueOf(value));
	}

	public static void addTerm(BooleanQuery booleanQuery, String field, String value) throws ParseException
	{

		if (value != null) {
			QueryParser queryParser = new QueryParser(field, LuceneUtil.getAnalyzer());

			try {
				Query query = queryParser.parse(value);

				booleanQuery.add(query, BooleanClause.Occur.SHOULD);
			} catch (ParseException pe) {
				if (_log.isDebugEnabled()) {
					_log.debug("ParseException thrown, reverting to literal search", pe);
				}

				value = GetterUtil.escape(value);

				Query query = queryParser.parse(value);

				booleanQuery.add(query, BooleanClause.Occur.SHOULD);
			}
		}
	}

	public static void addText(Document doc, String field, long value)
	{
		addText(doc, field, String.valueOf(value));
	}

	public static void addText(Document doc, String field, String value)
	{
		if (value != null) {
			doc.add(getText(field, value));
		}
	}

	public static Field getText(String field, String text)
	{
		return new Field(field, text, Field.Store.YES, Field.Index.TOKENIZED);
	}

	public static Field getText(String field, StringBuilder sb)
	{
		return getText(field, sb.toString());
	}

	public static Analyzer getAnalyzer()
	{
		return _instance._getAnalyzer();
	}

	public static Directory getLuceneDir(long companyId)
	{
		return _instance._getLuceneDir(companyId);
	}

	public static IndexReader getReader(long companyId) throws IOException
	{
		return IndexReader.open(getLuceneDir(companyId));
	}

	public static IndexSearcher getSearcher(long companyId) throws IOException
	{

		return new IndexSearcher(getLuceneDir(companyId));
	}

	public static IndexWriter getWriter(long companyId) throws IOException
	{
		return getWriter(companyId, false);
	}

	public static IndexWriter getWriter(long companyId, boolean create) throws IOException
	{

		return _instance.indexWriterFactory.getWriter(companyId, create);
	}

	public static void write(IndexWriter writer) throws IOException
	{
		_instance.indexWriterFactory.write(writer);
	}

	private LuceneUtil() {
		String analyzerName = PropsUtil.get(StringConstants.LUCENE_ANALYZER);

		if (analyzerName != null) {
			try {
				_analyzerClass = Class.forName(analyzerName);
			} catch (Exception e) {
				_log.error(e);
			}
		}

	}

	private Analyzer _getAnalyzer()
	{
		try {
			return (Analyzer) _analyzerClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Directory _getLuceneDir(long companyId)
	{
		if (_log.isDebugEnabled()) {
			_log.debug("Lucene store type " + StringConstants.LUCENE_STORE_TYPE);
		}

		if (StringConstants.LUCENE_STORE_TYPE.equals(_LUCENE_STORE_TYPE_FILE)) {
			return _getLuceneDirFile(companyId);
		} else {
			throw new RuntimeException("Invalid store type " + StringConstants.LUCENE_STORE_TYPE);
		}
	}

	private Directory _getLuceneDirFile(long companyId)
	{
		Directory directory = null;

		String path = _getPath(companyId);

		try {
			directory = FSDirectory.getDirectory(path, false);
		} catch (IOException ioe1) {
			try {
				if (directory != null) {
					directory.close();
				}

				directory = FSDirectory.getDirectory(path, true);
			} catch (IOException ioe2) {
				throw new RuntimeException(ioe2);
			}
		}

		return directory;
	}

	private String _getPath(long companyId)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(PropsUtil.get(StringConstants.LUCENE_DIR));
		sb.append(companyId);
		sb.append(StringConstants.SLASH);

		return sb.toString();
	}

	private static final String _LUCENE_STORE_TYPE_FILE = "file";

	private static final String _LUCENE_TABLE_PREFIX = "LUCENE_";

	private static Log _log = LogFactory.getLog(LuceneUtil.class);

	private static LuceneUtil _instance = new LuceneUtil();

	private IndexWriterFactory indexWriterFactory = new IndexWriterFactory();
	private Class<?> _analyzerClass = WhitespaceAnalyzer.class;

}