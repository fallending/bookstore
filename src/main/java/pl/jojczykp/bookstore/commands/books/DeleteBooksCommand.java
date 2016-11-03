package pl.jojczykp.bookstore.commands.books;

import pl.jojczykp.bookstore.commands.common.PagerCommand;

import java.util.ArrayList;
import java.util.List;

public class DeleteBooksCommand {

	private PagerCommand pager;
	private List<Integer> ids;

	public DeleteBooksCommand() {
		pager = new PagerCommand();
		ids = new ArrayList<>();
	}

	public PagerCommand getPager() {
		return pager;
	}

	public void setPager(PagerCommand pager) {
		this.pager = pager;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

}
