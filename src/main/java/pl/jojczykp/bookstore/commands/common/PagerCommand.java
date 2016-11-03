package pl.jojczykp.bookstore.commands.common;

import pl.jojczykp.bookstore.utils.PageSorter;

public class PagerCommand {

	private int pageNumber;
	private int pageSize;
	private int pagesCount;
	private int totalCount;
	private PageSorter sorter;

	public PagerCommand() {
		sorter = new PageSorter();
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public PageSorter getSorter() {
		return sorter;
	}

	public void setSorter(PageSorter sorter) {
		this.sorter = sorter;
	}

}
