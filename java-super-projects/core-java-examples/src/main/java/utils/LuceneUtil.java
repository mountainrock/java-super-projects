package utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneUtil {

	/*	*//**
	 * Transforms Lucene document to a generic representation.
	 */
	/*
	 * @SuppressWarnings("unchecked") public static GenericDocumentImpl tranformDocument(Document doc) { List<Field> fields = doc.getFields(); GenericDocumentImpl gDocument = new
	 * GenericDocumentImpl(); for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) { Field field = (Field) iterator.next(); GenericField gField = new GenericField();
	 * gField.setName(field.name()); gField.setValue(field.stringValue()); gDocument.add(gField); } return gDocument; }
	 * 
	 * public static Sort transformToLuceneSort(GenericSort sort) { Sort luceneSort = null; if (sort != null) { luceneSort = new Sort(sort.getFieldName(), sort.isReverse()); } return luceneSort; }
	 *//**
	 * Transforms generic field to lucene field type.
	 */
	/*
	 * public static Field tranformGenericField(GenericField genField) { Field lcnField = null; boolean stored = genField.isStored(); boolean sortable = genField.isSortable(); Object value =
	 * genField.getValue();
	 * 
	 * String valueStr = value.toString(); if (sortable) { String valueSubStr = valueStr.length() > Constants.SORTABLE_FIELD_MAX_SIZE ? valueStr.substring(0, Constants.SORTABLE_FIELD_MAX_SIZE) :
	 * valueStr; lcnField = new Field(genField.getName(), valueSubStr, Field.Store.YES, Field.Index.NOT_ANALYZED); } else if (stored) { lcnField = new Field(genField.getName(), valueStr,
	 * Field.Store.YES, Field.Index.ANALYZED); } else { lcnField = new Field(genField.getName(), valueStr, Field.Store.NO, Field.Index.ANALYZED); }
	 * 
	 * return lcnField; }
	 */
	public static void addRangeTerm(BooleanQuery booleanQuery, String field, String lowerValue, String upperValue, boolean inclusive, boolean occurAlways)
	{
		Term lower = new Term(field, lowerValue);
		Term upper = new Term(field, upperValue);
		RangeQuery query = new RangeQuery(lower, upper, inclusive);

		booleanQuery.add(query, (occurAlways ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD));
	}

	/**
	 * Add range term for fields having different aliases but same value.
	 * @param occurAlways - Should this term always occur. Does an AND operation if true.
	 */
	public static void addRangeTermWithAlias(BooleanQuery booleanQuery, String fields[], String lowerValue, String upperValue, boolean inclusive, boolean occurAlways)
	{
		Query query = null;
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			Term lower = new Term(field, lowerValue);
			Term upper = new Term(field, upperValue);
			if (query == null) {
				query = new RangeQuery(lower, upper, inclusive);
			} else {
				query = query.combine(new Query[] { query, new RangeQuery(lower, upper, inclusive) });
			}
		}
		booleanQuery.add(query, (occurAlways ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD));
	}

	/**
	 * Add term for fields having different aliases but same value.
	 * @param occurAlways - Should this term always occur. Does an AND operation if true.
	 */
	public static void addTermWithAlias(BooleanQuery booleanQuery, String fields[], String value, boolean occurAlways) throws ParseException
	{
		Query query = null;
		if (value != null) {
			for (int i = 0; i < fields.length; i++) {
				String field = fields[i];
				if (query == null) {
					query = new TermQuery(new Term(field, value));
				} else {
					query = query.combine(new Query[] { query, new TermQuery(new Term(field, value)) });
				}

			}
			booleanQuery.add(query, (occurAlways ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD));
		}
	}

	
	public static void addTerm(BooleanQuery booleanQuery, String field, String value, boolean occurAlways) throws ParseException
	{

		if (value != null) {
			Query query = new TermQuery(new Term(field, value));

			booleanQuery.add(query, (occurAlways ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD));
		}
	}

	
	public static void addTermWithAlias(BooleanQuery booleanQuery, String fields[], String[] values, boolean occurAlways) throws ParseException
	{
		Query query = null;
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			BooleanQuery queryTemp = getMultiValuedQuery(field, values);
			if (query == null) {
				query = queryTemp;
			} else {
				query = query.combine(new Query[] { query, queryTemp });
			}
		}
		booleanQuery.add(query, (occurAlways ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD));
	}

	
	public static void addTerm(BooleanQuery booleanQuery, String field, String[] values, boolean occurAlways) throws ParseException
	{
		try {
			Query query = getMultiValuedQuery(field, values);
			booleanQuery.add(query, (occurAlways ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD));
		} catch (ParseException pe) {
			throw pe;
		}
	}

	public static org.apache.lucene.search.BooleanQuery getMultiValuedQuery(String field, String values[]) throws ParseException
	{
		org.apache.lucene.search.BooleanQuery mvQuery = new org.apache.lucene.search.BooleanQuery();
		// QueryParser queryParser = new QueryParser(field, LuceneUtil.getAnalyzer());
		for (String value : values) {
			Occur occur = BooleanClause.Occur.SHOULD;
			BooleanClause clause = new BooleanClause(new TermQuery(new Term(field, value)), occur);
			mvQuery.add(clause);

		}
		return mvQuery;
	}

	public static Analyzer getAnalyzer()
	{
		return _instance._getAnalyzer();
	}

	public static Directory getLuceneDir()
	{
		return _instance._getLuceneDir();
	}

	public static IndexReader getReader() throws IOException
	{
		return IndexReader.open(getLuceneDir());
	}

	public static IndexSearcher getSearcher() throws IOException
	{

		return new IndexSearcher(getLuceneDir());
	}

	private LuceneUtil() {
		String analyzerName = PropsUtil.get(Constants.LUCENE_ANALYZER);

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
			PerFieldAnalyzerWrapper perFieldAnalyzerWrapper = new PerFieldAnalyzerWrapper((Analyzer) _analyzerClass.newInstance());
			perFieldAnalyzerWrapper.addAnalyzer("status", new KeywordAnalyzer());

			return (Analyzer) perFieldAnalyzerWrapper;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Directory _getLuceneDir()
	{
		if (_log.isDebugEnabled()) {
			_log.debug("Lucene store type " + Constants.LUCENE_STORE_TYPE);
		}

		if (Constants.LUCENE_STORE_TYPE.equals(LUCENE_STORE_TYPE_FILE)) {
			return _getLuceneDirFile();
		} else {
			throw new RuntimeException("Invalid store type " + Constants.LUCENE_STORE_TYPE);
		}
	}

	private Directory _getLuceneDirFile()
	{
		Directory directory = null;

		String path = _getPath();

		try {
			directory = FSDirectory.getDirectory(path);
		} catch (IOException ioe1) {
			try {
				if (directory != null) {
					directory.close();
				}

				directory = FSDirectory.getDirectory(path);
			} catch (IOException ioe2) {
				throw new RuntimeException(ioe2);
			}
		}

		return directory;
	}

	private String _getPath()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(PropsUtil.get(Constants.LUCENE_DIR));
		sb.append(Constants.SLASH);

		return sb.toString();
	}

	/*
	 * public static String[] getFieldNames(String lucenePath) throws SearchException { String[] fieldNamesAr; try { IndexReader ir = IndexReader.open(lucenePath); Collection fieldNames =
	 * ir.getFieldNames(IndexReader.FieldOption.ALL); fieldNamesAr = (String[]) fieldNames.toArray(new String[fieldNames.size()]); ir.close(); } catch (Exception e) { throw new SearchException(e); }
	 * return fieldNamesAr; }
	 */

	public static String sanitize(String fieldValue)
	{
		char[] luceneSanitizeChars = Constants.LUCENE_SANITIZE_CHARS;
		for (int i = 0; i < luceneSanitizeChars.length; i++) {
			char c = luceneSanitizeChars[i];
			if (fieldValue.indexOf(c) > 0)
				fieldValue = fieldValue.replaceAll(c + "", "");

		}

		return fieldValue;
	}

	private static final String LUCENE_STORE_TYPE_FILE = "file";

	private static Log _log = LogFactory.getLog(LuceneUtil.class);

	private static LuceneUtil _instance = new LuceneUtil();

	private Class<?> _analyzerClass = WhitespaceAnalyzer.class;

}