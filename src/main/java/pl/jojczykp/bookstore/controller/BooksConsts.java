package pl.jojczykp.bookstore.controller;

public abstract class BooksConsts {

	static final String URL_ACTION_CREATE = "/books/create";
	static final String URL_ACTION_READ = "/books/read";
	static final String URL_ACTION_UPDATE = "/books/update";
	static final String URL_ACTION_DELETE = "/books/delete";

	static final String URL_ACTION_PREV = "/books/prev";
	static final String URL_ACTION_NEXT = "/books/next";
	static final String URL_ACTION_SORT = "/books/sort";
	static final String URL_ACTION_SET_PAGE_SIZE = "/books/setPageSize";

	static final String BOOKS_COMMAND = "booksCommand";

	static final String BOOKS_VIEW = "books";

	static final String EXCEPTION_VIEW = "exception";
}
