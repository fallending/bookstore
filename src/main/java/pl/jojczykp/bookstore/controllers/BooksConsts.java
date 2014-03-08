package pl.jojczykp.bookstore.controllers;

public abstract class BooksConsts {

	static final String URL_ACTION_CREATE = "/books/create";
	static final String URL_ACTION_READ = "/books/read";
	static final String URL_ACTION_UPDATE = "/books/update";
	static final String URL_ACTION_DELETE = "/books/delete";

	static final String URL_ACTION_SORT = "/books/sort";
	static final String URL_ACTION_GO_TO_PAGE = "/books/goToPage";
	static final String URL_ACTION_SET_PAGE_SIZE = "/books/setPageSize";

	static final String BOOKS_COMMAND = "booksCommand";

	static final String BOOKS_VIEW = "books";

	static final String EXCEPTION_VIEW = "exception";
}
