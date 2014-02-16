package pl.jojczykp.bookstore.utils;

import org.springframework.beans.factory.annotation.Value;
import pl.jojczykp.bookstore.command.BooksCommand;

public class BooksCommandFactory {

	@Value("${view.books.defaultPageNumber}") private int defaultPageNumber;
	@Value("${view.books.defaultPageSize}") private int defaultPageSize;
	@Value("${view.books.defaultSortColumn}") private PageSorterColumn defaultSortColumn;
	@Value("${view.books.defaultSortDirection}") private PageSorterDirection defaultSortDirection;

	public BooksCommand create() {
		BooksCommand booksCommand = new BooksCommand();

		booksCommand.getPager().setPageNumber(defaultPageNumber);
		booksCommand.getPager().setPageSize(defaultPageSize);
		booksCommand.getPager().getSorter().setColumn(defaultSortColumn);
		booksCommand.getPager().getSorter().setDirection(defaultSortDirection);

		return booksCommand;
	}
}
