package search.example.srch.impl;

import java.util.ArrayList;
import java.util.List;

import search.example.srch.Document;
import search.example.srch.Hits;

@SuppressWarnings("serial")
public class HitsImpl implements Hits {
	private long start;
	private float searchTime;
	private Document[] docs;
	private int length;
	private float[] scores = new float[0];

	public HitsImpl() {
	}

	public long getStart()
	{
		return this.start;
	}

	public void setStart(long start)
	{
		this.start = start;
	}

	public float getSearchTime()
	{
		return this.searchTime;
	}

	public void setSearchTime(float time)
	{
		this.searchTime = time;
	}

	public Document[] getDocs()
	{
		return docs;
	}

	public void setDocs(Document[] docs)
	{
		this.docs = docs;
	}

	public int getLength()
	{
		return this.length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public float[] getScores()
	{
		return scores;
	}

	public void setScores(float[] scores)
	{
		this.scores = scores;
	}

	public void setScores(Float[] scores)
	{
		float[] primScores = new float[scores.length];

		for (int i = 0; i < scores.length; i++) {
			primScores[i] = scores[i].floatValue();
		}

		setScores(primScores);
	}

	public Document doc(int n)
	{
		return docs[n];
	}

	public float score(int n)
	{
		return scores[n];
	}

	public List<Document> toList()
	{
		List<Document> subset = new ArrayList<Document>(docs.length);

		for (int i = 0; i < docs.length; i++) {
			subset.add(docs[i]);
		}

		return subset;
	}

}