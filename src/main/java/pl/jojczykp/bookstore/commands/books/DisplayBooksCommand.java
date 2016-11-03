package pl.jojczykp.bookstore.commands.books;

import pl.jojczykp.bookstore.commands.common.MessagesCommand;
import pl.jojczykp.bookstore.commands.common.PagerCommand;

import java.util.ArrayList;
import java.util.List;

public class DisplayBooksCommand {

	private MessagesCommand messages;
	private PagerCommand pager;
	private List<DisplayBookCommand> books;

	public DisplayBooksCommand() {
		messages = new MessagesCommand();
		pager = new PagerCommand();
		books = new ArrayList<>();
	}

	public MessagesCommand getMessages() {
		return messages;
	}

	public void setMessages(MessagesCommand messages) {
		this.messages = messages;
	}

	public PagerCommand getPager() {
		return pager;
	}

	public void setPager(PagerCommand pager) {
		this.pager = pager;
	}

	public List<DisplayBookCommand> getBooks() {
		return books;
	}

	public void setBooks(List<DisplayBookCommand> books) {
		this.books = books;
	}

}
