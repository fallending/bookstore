package pl.jojczykp.bookstore.utils;

public enum ScrollSorterColumn {

	BOOK_TITLE("title", true);

	private String nameForQuery;
	private boolean ignoreCase;

	ScrollSorterColumn(String nameForQuery, boolean ignoreCase) {
		this.nameForQuery = nameForQuery;
		this.ignoreCase = ignoreCase;
	}

	public String getNameForQuery() {
		return nameForQuery;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}
}
