package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Paginator {

	private Integer itemsPerPage;
	private Integer totalHits;
	private Integer currentPage;

	public Paginator(Integer totalHits, Integer itemsPerPage, Integer currentPage) {
		this.totalHits = totalHits;
		this.itemsPerPage = itemsPerPage;
		this.currentPage = currentPage;
	}

	public Integer numberOfPages()
	{
		int pages = (totalHits / itemsPerPage);
		if (totalHits % itemsPerPage >= 1) {
			pages++;
		}
		return pages;
	}

	public Integer previousPage()
	{
		return (currentPage <= 1 ? 1 : currentPage - 1);
	}

	public Integer nextPage()
	{
		return (currentPage >= numberOfPages() ? numberOfPages() : currentPage + 1);
	}

	public boolean isFirstPage()
	{
		return (currentPage == 1);
	}

	public boolean isLastPage()
	{
		return (currentPage.equals(numberOfPages()));
	}

	public boolean hasPrevious()
	{
		return (currentPage - 1 >= 1);
	}

	public boolean hasNext()
	{
		return (currentPage + 1 <= numberOfPages());
	}

	public List<String> pageList()
	{
		List<String> pages = new ArrayList<String>();
		int nPages = numberOfPages();
		int pageSize = itemsPerPage;
		for (int i = 0; i < nPages; i++) {
			pages.add(((i == 0) ? 1 : i * itemsPerPage) + "-" + pageSize);
			pageSize += itemsPerPage;

		}
		return pages;
	}

	@Override
	public String toString()
	{

		return ToStringBuilder.reflectionToString(this);
	}

}