package pl.jojczykp.bookstore.utils;

import org.springframework.beans.factory.annotation.Value;
import pl.jojczykp.bookstore.command.BooksCommand;

public class BooksCommandFactory {

	@Value("${view.books.defaultOffset}") private int defaultOffset;
	@Value("${view.books.defaultSize}") private int defaultSize;
	@Value("${view.books.defaultSortColumn}") private PageSorterColumn defaultSortColumn;
	@Value("${view.books.defaultSortDirection}") private PageSorterDirection defaultSortDirection;

	public BooksCommand create() {
		BooksCommand booksCommand = new BooksCommand();

		booksCommand.getPager().getCurrent().setOffset(defaultOffset);
		booksCommand.getPager().getCurrent().setSize(defaultSize);
		booksCommand.getPager().getSorter().setColumn(defaultSortColumn);
		booksCommand.getPager().getSorter().setDirection(defaultSortDirection);

		return booksCommand;
	}
}
