package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.assembler.BookAssembler;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.BooksCommandFactory;
import pl.jojczykp.bookstore.utils.ScrollParams;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_VIEW;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_READ;

@Controller
public class BooksControllerRead {

	@Autowired private BooksCommandFactory booksCommandFactory;
	@Autowired private BookRepository bookRepository;
	@Autowired private ScrollParamsLimiter scrollParamsLimiter;
	@Autowired private BookAssembler bookAssembler;

	@ModelAttribute(BOOKS_COMMAND)
	public BooksCommand getDefaultBooksCommand() {
		return booksCommandFactory.create();
	}

	@RequestMapping(value = URL_ACTION_READ, method = GET)
	public ModelAndView read(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand)
	{
		int totalCount = bookRepository.totalCount();
		booksCommand.getScroll().setTotalCount(totalCount);
		ScrollParams limitedScrollParams = scrollParamsLimiter.limit(booksCommand.getScroll().getCurrent(), totalCount);
		booksCommand.getScroll().setLimited(limitedScrollParams);

		List<Book> books = read(booksCommand, limitedScrollParams);

		booksCommand.setBooks(bookAssembler.toCommands(books));

		return new ModelAndView(BOOKS_VIEW, aModelFor(booksCommand));
	}

	private List<Book> read(BooksCommand booksCommand, ScrollParams limitedScrollParams) {
		return bookRepository.read(
					limitedScrollParams.getOffset(),
					limitedScrollParams.getSize(),
					booksCommand.getScroll().getSorter().getColumn(),
					booksCommand.getScroll().getSorter().getDirection());
	}

	private ModelMap aModelFor(BooksCommand booksCommand) {
		return new ModelMap().addAttribute(BOOKS_COMMAND, booksCommand);
	}

}
