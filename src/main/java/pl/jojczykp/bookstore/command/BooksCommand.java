package pl.jojczykp.bookstore.command;

import java.util.ArrayList;
import java.util.List;

public class BooksCommand {

	private PagerCommand pager;
	private BookCommand newBook;
	private BookCommand updatedBook;
	private List<BookCommand> books;
	private String message;

	public BooksCommand() {
		pager = new PagerCommand();
		newBook = new BookCommand();
		updatedBook = new BookCommand();
		books = new ArrayList<>();
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
