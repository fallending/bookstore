package pl.jojczykp.bookstore.commands;

import java.util.ArrayList;
import java.util.List;

public class BooksCommand {

	private MessagesCommand messages;
	private PagerCommand pager;
	private BookCommand newBook;
	private BookCommand updatedBook;
	private List<BookCommand> books;

	public BooksCommand() {
		messages = new MessagesCommand();
		pager = new PagerCommand();
		newBook = new BookCommand();
		updatedBook = new BookCommand();
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

	public BookCommand getNewBook() {
		return newBook;
	}

	public void setNewBook(BookCommand newBook) {
		this.newBook = newBook;
	}

	public BookCommand getUpdatedBook() {
		return updatedBook;
	}

	public void setUpdatedBook(BookCommand updatedBook) {
		this.updatedBook = updatedBook;
	}

	public List<BookCommand> getBooks() {
		return books;
	}

	public void setBooks(List<BookCommand> books) {
		this.books = books;
	}

}
