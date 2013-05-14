package search.example.srch.impl;

import search.example.srch.BooleanClause;

public class BooleanClauseImpl implements BooleanClause {
	private org.apache.lucene.search.BooleanClause booleanClause;

	public BooleanClauseImpl(org.apache.lucene.search.BooleanClause booleanClause) {

		this.booleanClause = booleanClause;
	}

	public org.apache.lucene.search.BooleanClause getBooleanClause()
	{
		return booleanClause;
	}

}