package com.bri8.supermag.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.IssuePage;
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

	private static final String INDEX_ISSUE = "issue";
	
	public void indexIssue(Magazine magazine, Issue issue, IssuePage issueCoverPage, Date publishDate) {
		Document doc = Document.newBuilder().setId(issue.getIssueId() + "")
				.addField(Field.newBuilder().setName("magazineId").setText(magazine.getMagazineId()+""))
				.addField(Field.newBuilder().setName("magazineName").setText(magazine.getMagazineName()))
				.addField(Field.newBuilder().setName("magazineDescription").setText(magazine.getDescription()))
				.addField(Field.newBuilder().setName("publishingCompany").setAtom(magazine.getPublishingCompany()))
				.addField(Field.newBuilder().setName("category1").setText(magazine.getCategory1()))
				.addField(Field.newBuilder().setName("category2").setText(magazine.getCategory2()))
				.addField(Field.newBuilder().setName("createdDate").setDate(magazine.getCreatedDate()))
				.addField(Field.newBuilder().setName("keywords").setText(magazine.getKeywords()))
				
				.addField(Field.newBuilder().setName("issueId").setText(issue.getIssueId()+""))
				.addField(Field.newBuilder().setName("issueName").setText(issue.getIssueName()))
				.addField(Field.newBuilder().setName("issueDescription").setText(issue.getDescription()))
				.addField(Field.newBuilder().setName("issueCreatedDate").setDate(issue.getCreatedDate()))
				.addField(Field.newBuilder().setName("issueBlobKeyThumbnail").setText(issueCoverPage.getBlobKeyThumbnail()))
				.addField(Field.newBuilder().setName("status").setText(issue.getStatus()))
				
				.addField(Field.newBuilder().setName("publishDate").setDate(publishDate))
				.build();
		indexADocument(INDEX_ISSUE, doc);
		
	}
	

	public Results<ScoredDocument> searchIssue(String keyword) {
	    SortOptions sortOptions = SortOptions.newBuilder()
	        .addSortExpression(SortExpression.newBuilder()
	            .setExpression("publishDate")
	            .setDirection(SortExpression.SortDirection.DESCENDING)
	            .setDefaultValueDate(null))
	        .setLimit(1000)
	        .build();

	    QueryOptions options = QueryOptions.newBuilder()
	        .setLimit(25)
	        .setFieldsToReturn("magazineId","magazineName", "magazineDescription","category1","category2", "keywords", "issueId", "issueName","issueDescription","issueBlobKeyThumbnail","issueCreatedDate","publishDate")
	        .setSortOptions(sortOptions)
	        .build();

	    String queryString = keyword;

	    //  TODO: search with status="live"
	    Query query = Query.newBuilder().setOptions(options).build(queryString);
	    IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_ISSUE).build();
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
