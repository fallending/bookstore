package pl.jojczykp.bookstore.command;

import pl.jojczykp.bookstore.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksCommand {

	private ScrollCommand scroll;
	private Book newBook;
	private List<Book> books;

	public BooksCommand() {
		scroll = new ScrollCommand();
		newBook = new Book();
		books = new ArrayList<>();
	}

	public ScrollCommand getScroll() {
		return scroll;
	}

	public void setScroll(ScrollCommand scroll) {
		this.scroll = scroll;
	}

	public Book getNewBook() {
		return newBook;
	}

	public void setNewBook(Book newBook) {
		this.newBook = newBook;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
