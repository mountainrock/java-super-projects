package search.example.srch.impl;

public class BooleanClauseOccurImpl implements BooleanClauseOccur {
	private String name;

	public BooleanClauseOccurImpl(String name) {
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

}