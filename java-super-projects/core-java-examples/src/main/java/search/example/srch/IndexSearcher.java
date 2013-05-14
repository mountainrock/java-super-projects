package search.example.srch;

public interface IndexSearcher {

	public Hits search(Query query, int start, int end) throws SearchException;

	public Hits search(Query query, Sort sort, int start, int end) throws SearchException;

}