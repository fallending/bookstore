package pl.jojczykp.bookstore.command;

import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.utils.ScrollParams;

import java.util.ArrayList;
import java.util.List;

public class BooksCommand {

	private ScrollParams originalScrollParams;
	private ScrollParams limitedScrollParams;
	private int pageSize;
	private int totalCount;
	private Book newBook;
	private List<Book> books;

	public BooksCommand() {
		originalScrollParams = new ScrollParams();
		limitedScrollParams = new ScrollParams();
		totalCount = 0;
		newBook = new Book();
		books = new ArrayList<>();
	}

	public ScrollParams getOriginalScrollParams() {
		return originalScrollParams;
	}

	public void setOriginalScrollParams(ScrollParams originalScrollParams) {
		this.originalScrollParams = originalScrollParams;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public ScrollParams getLimitedScrollParams() {
		return limitedScrollParams;
	}

	public void setLimitedScrollParams(ScrollParams limitedScrollParams) {
		this.limitedScrollParams = limitedScrollParams;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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
