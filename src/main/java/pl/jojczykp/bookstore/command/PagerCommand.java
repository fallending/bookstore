package pl.jojczykp.bookstore.command;

import pl.jojczykp.bookstore.utils.PageParams;
import pl.jojczykp.bookstore.utils.PageSorter;

public class PagerCommand {

	private PageParams current;
	private PageParams limited;
	private PageSorter sorter;
	private int totalCount;

	public PagerCommand() {
		current = new PageParams();
		limited = new PageParams();
		sorter = new PageSorter();
		totalCount = 0;
	}

	public PageParams getCurrent() {
		return current;
	}

	public void setCurrent(PageParams current) {
		this.current = current;
	}

	public PageParams getLimited() {
		return limited;
	}

	public void setLimited(PageParams limited) {
		this.limited = limited;
	}

	public PageSorter getSorter() {
		return sorter;
	}

	public void setSorter(PageSorter sorter) {
		this.sorter = sorter;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
