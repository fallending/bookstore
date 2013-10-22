package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;
import pl.jojczykp.bookstore.utils.ScrollParamsLimits;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

	private static final String DEFAULT_OFFSET = "0";
	private static final String DEFAULT_SIZE = "10";

	@Autowired BookRepository bookRepository;
	@Autowired
	ScrollParamsLimiter scrollParamsLimiter;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "offset", defaultValue = DEFAULT_OFFSET) int paramOffset,
			@RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int paramSize)
	{
		int totalCount = bookRepository.totalCount();
		ScrollParamsLimits limits = scrollParamsLimiter.computeLimitsFor(paramOffset, paramSize, totalCount);
		List<Book> books = bookRepository.read(limits.getOffset(), limits.getSize());
		return new ModelAndView("booksList", createModel(totalCount, limits, books));
	}

	private ModelMap createModel(int totalCount, ScrollParamsLimits limits, List<Book> books) {
		ModelMap model = new ModelMap();
		model.addAttribute("offset", limits.getOffset());
		model.addAttribute("size", limits.getSize());
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("books", books);

		return model;
	}

}
