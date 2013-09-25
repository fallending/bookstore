package bookstore.mvc.controller;

import bookstore.mvc.model.domain.Book;
import bookstore.mvc.model.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/books")
public class BooksController {

	private static final int DEFAULT_OFFSET = 0;
	private static final int DEFAULT_LIMIT = 10;

	@Autowired BookRepository bookDao;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		Iterable<Book> books = bookDao.get(DEFAULT_OFFSET, DEFAULT_LIMIT);

		ModelMap model = new ModelMap();
		model.addAttribute("offset", DEFAULT_OFFSET);
		model.addAttribute("limit", DEFAULT_LIMIT);
		model.addAttribute("books", books);

		return new ModelAndView("booksList", model);
	}
}
