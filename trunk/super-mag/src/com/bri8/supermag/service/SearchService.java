package com.bri8.supermag.service;

import org.springframework.stereotype.Service;

import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.Magazine;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;

@Service("searchService")
public class SearchService {

	private static final String INDEX_MAGAZINE = "magazine";
	private static final String INDEX_ISSUE = "issue";

	public void indexMagazine(Magazine magazine) {
		Document doc = Document.newBuilder().setId(magazine.getMagazineId() + "")
				.addField(Field.newBuilder().setName("magazineId").setText(magazine.getMagazineId()+""))
				.addField(Field.newBuilder().setName("name").setText(magazine.getMagazineName()))
				.addField(Field.newBuilder().setName("description").setText(magazine.getDescription()))
				.addField(Field.newBuilder().setName("publishingCompany").setAtom(magazine.getPublishingCompany()))
				.addField(Field.newBuilder().setName("category1").setText(magazine.getCategory1()))
				.addField(Field.newBuilder().setName("category2").setText(magazine.getCategory2()))
				.addField(Field.newBuilder().setName("createdDate").setDate(magazine.getCreatedDate()))
				.addField(Field.newBuilder().setName("keywords").setText(magazine.getKeywords())).build();
		indexADocument(INDEX_MAGAZINE, doc);

	}
	
	public void indexIssue(Issue issue) {
		//TODO:
		
	}

	public Results<ScoredDocument> searchMagazine(String keyword) {
		// Build the SortOptions with 2 sort keys
	    SortOptions sortOptions = SortOptions.newBuilder()
	        .addSortExpression(SortExpression.newBuilder()
	            .setExpression("createdDate")
	            .setDirection(SortExpression.SortDirection.DESCENDING)
	            .setDefaultValueDate(null))
	        .setLimit(1000)
	        .build();

	    // Build the QueryOptions
	    QueryOptions options = QueryOptions.newBuilder()
	        .setLimit(25)
	        .setFieldsToReturn("magazineId","name", "description", "category1", "category2", "publishingCompany", "keywords", "createdDate")
	        .setSortOptions(sortOptions)
	        .build();

	    // A query string
	    String queryString = keyword;

	    //  Build the Query and run the search
	    Query query = Query.newBuilder().setOptions(options).build(queryString);
	    IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_MAGAZINE).build();
	    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
	    Results<ScoredDocument> result =  index.search(query);

		return result;
	}

	public void indexADocument(String indexName, Document document) {
		Index index = getIndex(indexName);

		index.put(document);
	}

	private Index getIndex(String indexName) {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		return index;
	}

}
