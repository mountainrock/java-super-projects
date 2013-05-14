package search.example.srch;

import java.io.Serializable;
import java.util.List;

public interface Hits extends Serializable {

	public long getStart();

	public void setStart(long start);

	public float getSearchTime();

	public void setSearchTime(float time);

	public Document[] getDocs();

	public void setDocs(Document[] docs);

	public int getLength();

	public void setLength(int length);

	public float[] getScores();

	public void setScores(float[] scores);

	public void setScores(Float[] scores);

	public Document doc(int n);

	public float score(int n);

	public List<Document> toList();

}