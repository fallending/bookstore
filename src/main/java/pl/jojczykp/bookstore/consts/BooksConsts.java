package pl.jojczykp.bookstore.consts;

public abstract class BooksConsts {

	public static final String URL_ACTION_CREATE = "/books/create";
	public static final String URL_ACTION_DISPLAY = "/books/display";
	public static final String URL_ACTION_UPDATE = "/books/update";
	public static final String URL_ACTION_DELETE = "/books/delete";
	public static final String URL_ACTION_DOWNLOAD = "/books/download";

	public static final String URL_ACTION_SORT = "/books/sort";
	public static final String URL_ACTION_GO_TO_PAGE = "/books/goToPage";
	public static final String URL_ACTION_SET_PAGE_SIZE = "/books/setPageSize";

	public static final String CREATE_BOOK_COMMAND = "createBookCommand";
	public static final String DISPLAY_BOOKS_COMMAND = "displayBooksCommand";
	public static final String UPDATE_BOOK_COMMAND = "updateBookCommand";
	public static final String DELETE_BOOKS_COMMAND = "deleteBooksCommand";
	public static final String CHANGE_PAGER_COMMAND = "changePagerCommand";
	public static final String DOWNLOAD_BOOK_COMMAND = "downloadBookCommand";

	public static final String DISPLAY_BOOKS_VIEW = "books";

	public static final String EXCEPTION_VIEW = "exception";
}
