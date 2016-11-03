package pl.jojczykp.bookstore.commands.books;

import pl.jojczykp.bookstore.commands.common.PagerCommand;

public class ChangePagerCommand {

	private PagerCommand pager;

	public ChangePagerCommand() {
		pager = new PagerCommand();
	}

	public PagerCommand getPager() {
		return pager;
	}

	public void setPager(PagerCommand pager) {
		this.pager = pager;
	}

}
