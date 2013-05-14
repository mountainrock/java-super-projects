package search.example.srch.impl;

import search.example.srch.*;

public class SearchEngineImpl implements SearchEngine {

	private String name;
	private IndexSearcher searcher;
	private IndexWriter writer;

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public IndexSearcher getSearcher()
	{
		return this.searcher;
	}

	public void setSearcher(IndexSearcher searcher)
	{
		this.searcher = searcher;
	}

	public IndexWriter getWriter()
	{
		return this.writer;
	}

	public void setWriter(IndexWriter writer)
	{
		this.writer = writer;
	}

}