package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParams;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_VIEW;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_LIST;

@Controller
public class BooksControllerList {

	@Autowired BookRepository bookRepository;
	@Autowired ScrollParamsLimiter scrollParamsLimiter;

	@Value("${view.books.defaultOffset}") int defaultOffset;
	@Value("${view.books.defaultSize}") int defaultSize;

	@ModelAttribute(BOOKS_COMMAND)
	public BooksCommand getDefaultBooksCommand() {
		BooksCommand booksCommand = new BooksCommand();
		booksCommand.getScroll().getCurrent().setOffset(defaultOffset);
		booksCommand.getScroll().getCurrent().setSize(defaultSize);
		return booksCommand;
	}

	@RequestMapping(value = URL_ACTION_LIST, method = GET)
	public ModelAndView list(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand)
	{
		int totalCount = bookRepository.totalCount();
		booksCommand.getScroll().setTotalCount(totalCount);
		ScrollParams limitedScrollParams = scrollParamsLimiter.limit(booksCommand.getScroll().getCurrent(), totalCount);
		booksCommand.getScroll().setLimited(limitedScrollParams);
		booksCommand.setBooks(bookRepository.read(limitedScrollParams.getOffset(), limitedScrollParams.getSize()));

		return new ModelAndView(BOOKS_VIEW, new ModelMap().addAttribute(BOOKS_COMMAND, booksCommand));
	}

}
