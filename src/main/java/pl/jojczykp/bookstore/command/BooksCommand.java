package pl.jojczykp.bookstore.command;

import java.util.ArrayList;
import java.util.List;

public class BooksCommand {

	private ScrollCommand scroll;
	private BookCommand newBook;
	private List<BookCommand> books;
	private String message;

	public BooksCommand() {
		scroll = new ScrollCommand();
		newBook = new BookCommand();
		books = new ArrayList<>();
	}

	public ScrollCommand getScroll() {
		return scroll;
	}

	public void setScroll(ScrollCommand scroll) {
		this.scroll = scroll;
	}

	public BookCommand getNewBook() {
		return newBook;
	}

	public void setNewBook(BookCommand newBook) {
		this.newBook = newBook;
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
