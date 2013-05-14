package search.example.srch;

public interface SearchEngine {

	public String getName();

	public IndexSearcher getSearcher();

	public IndexWriter getWriter();

}