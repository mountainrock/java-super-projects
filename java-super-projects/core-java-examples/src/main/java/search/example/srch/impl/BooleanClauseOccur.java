package search.example.srch.impl;

public interface BooleanClauseOccur {

	public static final BooleanClauseOccur MUST = new BooleanClauseOccurImpl("MUST");

	public static final BooleanClauseOccur MUST_NOT = new BooleanClauseOccurImpl("MUST_NOT");

	public static final BooleanClauseOccur SHOULD = new BooleanClauseOccurImpl("SHOULD");

}