package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;
import pl.jojczykp.bookstore.utils.ScrollParamsLimits;

import java.util.List;

import static pl.jojczykp.bookstore.controller.BooksControllerList.ATTR_OFFSET;
import static pl.jojczykp.bookstore.controller.BooksControllerList.ATTR_SIZE;

@Controller
@RequestMapping("/books")
@SessionAttributes({ATTR_OFFSET, ATTR_SIZE})
public class BooksControllerList {

	static final String ATTR_OFFSET = "offset";
	static final String ATTR_SIZE = "size";
	static final String ATTR_NEW_BOOK = "newBook";

	private static final String REQUEST_PAGE_LIST = "list";

	@Autowired BookRepository bookRepository;
	@Autowired ScrollParamsLimiter scrollParamsLimiter;

	@RequestMapping(value = REQUEST_PAGE_LIST, method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = ATTR_OFFSET, defaultValue = "0") int paramOffset,
			@RequestParam(value = ATTR_SIZE, defaultValue = "10") int paramSize)
	{
		int totalCount = bookRepository.totalCount();
		ScrollParamsLimits limits = scrollParamsLimiter.computeLimitsFor(paramOffset, paramSize, totalCount);
		List<Book> books = bookRepository.read(limits.getOffset(), limits.getSize());

		return new ModelAndView("booksList", createModelFromList(totalCount, limits, books));
	}

	private ModelMap createModelFromList(int totalCount, ScrollParamsLimits limits, List<Book> books) {
		ModelMap model = new ModelMap();

		model.addAttribute(ATTR_OFFSET, limits.getOffset());
		model.addAttribute(ATTR_SIZE, limits.getSize());
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("books", books);

		model.addAttribute(ATTR_NEW_BOOK, new Book());

		return model;
	}

}
