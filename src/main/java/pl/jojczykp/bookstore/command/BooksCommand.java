package pl.jojczykp.bookstore.command;

import java.util.ArrayList;
import java.util.List;

public class BooksCommand {

	private ScrollCommand scroll;
	private BookCommand newBook;
	private int updateBookId;
	private String updateBookTitle;
	private List<BookCommand> books;
	private String message;

	public BooksCommand() {
		scroll = new ScrollCommand();
		newBook = new BookCommand();
		updateBookId = 0;
		updateBookTitle = "";
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

	public int getUpdateBookId() {
		return updateBookId;
	}

	public void setUpdateBookId(int updateBookId) {
		this.updateBookId = updateBookId;
	}

	public String getUpdateBookTitle() {
		return updateBookTitle;
	}

	public void setUpdateBookTitle(String updateBookTitle) {
		this.updateBookTitle = updateBookTitle;
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
