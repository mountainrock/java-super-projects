package search.example.srch;

public interface IndexWriter {

	public void addDocument(Document doc) throws SearchException;

	public void deleteDocument(String uid) throws SearchException;

	public void updateDocument(String uid, Document doc) throws SearchException;

}