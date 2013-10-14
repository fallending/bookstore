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

import java.util.List;

import static java.lang.Math.max;

@Controller
@RequestMapping("/books")
public class BooksController {

	private static final String DEFAULT_OFFSET = "0";
	private static final String DEFAULT_SIZE = "10";

	@Autowired
	BookRepository bookRepository;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "offset", defaultValue = DEFAULT_OFFSET) int paramOffset,
			@RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int paramSize)
	{
		int totalCount = bookRepository.totalCount();

		List<Book> books = bookRepository.read(paramOffset, paramSize);
		int size = books.size();
		int offset = max(0, paramOffset + size > totalCount ? totalCount - size : paramOffset);

		ModelMap model = new ModelMap();
		model.addAttribute("offset", offset);
		model.addAttribute("size", size);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("books", books);

		return new ModelAndView("booksList", model);
	}

}
