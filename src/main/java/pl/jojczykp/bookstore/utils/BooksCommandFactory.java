package pl.jojczykp.bookstore.utils;

import org.springframework.beans.factory.annotation.Value;
import pl.jojczykp.bookstore.command.BooksCommand;

public class BooksCommandFactory {

	@Value("${view.books.defaultOffset}") private int defaultOffset;
	@Value("${view.books.defaultSize}") private int defaultSize;
	@Value("${view.books.defaultSortColumn}") private ScrollSorterColumn defaultSortColumn;
	@Value("${view.books.defaultSortDirection}") private ScrollSorterDirection defaultSortDirection;

	public BooksCommand create() {
		BooksCommand booksCommand = new BooksCommand();

		booksCommand.getScroll().getCurrent().setOffset(defaultOffset);
		booksCommand.getScroll().getCurrent().setSize(defaultSize);
		booksCommand.getScroll().getSorter().setColumn(defaultSortColumn);
		booksCommand.getScroll().getSorter().setDirection(defaultSortDirection);

		return booksCommand;
	}
}
