package pl.jojczykp.bookstore.commands.books;

import pl.jojczykp.bookstore.commands.common.PagerCommand;

public class UpdateBookCommand {

	private PagerCommand pager;
	private int id;
	private int version;
	private String title;

	public UpdateBookCommand() {
		pager = new PagerCommand();
		id = 0;
		version = 0;
		title = "";
	}

	public PagerCommand getPager() {
		return pager;
	}

	public void setPager(PagerCommand pager) {
		this.pager = pager;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
