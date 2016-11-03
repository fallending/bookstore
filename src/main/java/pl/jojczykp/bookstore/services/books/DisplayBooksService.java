package pl.jojczykp.bookstore.services.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.assemblers.DisplayBookAssembler;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.common.PagerCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.utils.PagerLimiter;

import java.util.List;

@Service
public class DisplayBooksService {

	@Autowired private BooksRepository booksRepository;
	@Autowired private PagerLimiter pagerLimiter;
	@Autowired private DisplayBookAssembler displayBookAssembler;

	public DisplayBooksCommand display(DisplayBooksCommand displayBooksCommand) {
		PagerCommand limitedPager = pagerLimiter.createLimited(
														displayBooksCommand.getPager(), booksRepository.totalCount());
		displayBooksCommand.setPager(limitedPager);

		List<Book> books = read(displayBooksCommand.getPager());
		displayBooksCommand.setBooks(displayBookAssembler.toCommands(books));

		return displayBooksCommand;
	}

	private List<Book> read(PagerCommand pager) {
		int pageSize = pager.getPageSize();
		int pageNumber = pager.getPageNumber();
		int offset = (pageNumber - 1) * pageSize;

		return booksRepository.read(
					offset,
					pageSize,
					pager.getSorter().getColumn(),
					pager.getSorter().getDirection());
	}

}
