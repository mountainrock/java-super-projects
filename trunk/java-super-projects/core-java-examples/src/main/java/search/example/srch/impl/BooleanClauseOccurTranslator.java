package search.example.srch.impl;

public class BooleanClauseOccurTranslator {

	public static org.apache.lucene.search.BooleanClause.Occur translate(BooleanClauseOccur occur)
	{

		if (occur == BooleanClauseOccur.MUST) {
			return org.apache.lucene.search.BooleanClause.Occur.MUST;
		} else if (occur == BooleanClauseOccur.MUST_NOT) {
			return org.apache.lucene.search.BooleanClause.Occur.MUST_NOT;
		} else if (occur == BooleanClauseOccur.SHOULD) {
			return org.apache.lucene.search.BooleanClause.Occur.SHOULD;
		} else {
			return null;
		}
	}

}