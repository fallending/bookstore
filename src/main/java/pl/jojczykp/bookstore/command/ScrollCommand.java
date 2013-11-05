package pl.jojczykp.bookstore.command;

import pl.jojczykp.bookstore.utils.ScrollParams;

public class ScrollCommand {

	private ScrollParams current;
	private ScrollParams limited;
	private int totalCount;

	public ScrollCommand() {
		current = new ScrollParams();
		limited = new ScrollParams();
		totalCount = 0;
	}

	public ScrollParams getCurrent() {
		return current;
	}

	public void setCurrent(ScrollParams current) {
		this.current = current;
	}

	public ScrollParams getLimited() {
		return limited;
	}

	public void setLimited(ScrollParams limited) {
		this.limited = limited;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
