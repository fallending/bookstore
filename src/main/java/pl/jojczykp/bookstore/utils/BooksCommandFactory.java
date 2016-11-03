package pl.jojczykp.bookstore.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;

@Service
public class BooksCommandFactory {

	@Value("${view.books.defaultPageNumber}") private int defaultPageNumber;
	@Value("${view.books.defaultPageSize}") private int defaultPageSize;
	@Value("${view.books.defaultSortColumn}") private PageSorterColumn defaultSortColumn;
	@Value("${view.books.defaultSortDirection}") private PageSorterDirection defaultSortDirection;

	public DisplayBooksCommand create() {
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();

		displayBooksCommand.getPager().setPageNumber(defaultPageNumber);
		displayBooksCommand.getPager().setPageSize(defaultPageSize);
		displayBooksCommand.getPager().getSorter().setColumn(defaultSortColumn);
		displayBooksCommand.getPager().getSorter().setDirection(defaultSortDirection);

		return displayBooksCommand;
	}
}
