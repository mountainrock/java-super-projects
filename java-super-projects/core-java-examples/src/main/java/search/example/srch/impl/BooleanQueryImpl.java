package search.example.srch.impl;

import java.util.ArrayList;
import java.util.List;

import search.example.srch.BooleanClause;
import search.example.srch.BooleanQuery;
import search.example.srch.Query;
import search.example.srch.SearchException;
import search.example.util.LuceneUtil;

public class BooleanQueryImpl implements BooleanQuery {

	public BooleanQueryImpl() {
		booleanQuery = new org.apache.lucene.search.BooleanQuery();
	}

	public void add(Query query, BooleanClauseOccur occur)
	{
		booleanQuery.add(QueryTranslator.translate(query), BooleanClauseOccurTranslator.translate(occur));
	}

	public void addExactTerm(String field, String value)
	{
		LuceneUtil.addExactTerm(booleanQuery, field, value);
	}

	public void addRequiredTerm(String field, String value)
	{
		LuceneUtil.addRequiredTerm(booleanQuery, field, value);
	}

	public void addTerm(String field, String value) throws SearchException
	{
		try {
			LuceneUtil.addTerm(booleanQuery, field, value);
		} catch (org.apache.lucene.queryParser.ParseException pe) {
			throw new SearchException(pe);
		}
	}

	@SuppressWarnings("unchecked")
	public List<BooleanClause> clauses()
	{
		List<org.apache.lucene.search.BooleanClause> luceneClauses = booleanQuery.clauses();

		List<BooleanClause> clauses = new ArrayList<BooleanClause>(luceneClauses.size());

		for (int i = 0; i < luceneClauses.size(); i++) {
			clauses.add(new BooleanClauseImpl(luceneClauses.get(i)));
		}

		return clauses;
	}

	public org.apache.lucene.search.BooleanQuery getBooleanQuery()
	{
		return booleanQuery;
	}

	public String toString()
	{
		return booleanQuery.toString();
	}

	private org.apache.lucene.search.BooleanQuery booleanQuery;

}