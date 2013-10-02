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

@Controller
@RequestMapping("/books")
public class BooksController {

	private static final String DEFAULT_OFFSET = "0";
	private static final String DEFAULT_SIZE = "10";

	@Autowired
	BookRepository bookRepository;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "offset", defaultValue = DEFAULT_OFFSET) int offset,
			@RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size)
	{
		int count = bookRepository.count();

		Iterable<Book> books = bookRepository.read(offset, size);

		ModelMap model = new ModelMap();
		model.addAttribute("offset", offset);
		model.addAttribute("size", size);
		model.addAttribute("count", count);
		model.addAttribute("books", books);

		return new ModelAndView("booksList", model);
	}

}
