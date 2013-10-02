package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;

@Controller
@RequestMapping("/books")
public class BooksController {

	private static final int DEFAULT_OFFSET = 0;
	private static final int DEFAULT_SIZE = 10;

	@Autowired
	BookRepository bookRepository;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		Iterable<Book> books = bookRepository.read(DEFAULT_OFFSET, DEFAULT_SIZE);

		ModelMap model = new ModelMap();
		model.addAttribute("offset", DEFAULT_OFFSET);
		model.addAttribute("size", DEFAULT_SIZE);
		model.addAttribute("books", books);

		return new ModelAndView("booksList", model);
	}

}
