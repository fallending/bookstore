package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;

import static pl.jojczykp.bookstore.controller.BooksControllerList.ATTR_OFFSET;
import static pl.jojczykp.bookstore.controller.BooksControllerList.ATTR_SIZE;

@Controller
@RequestMapping("/books")
@SessionAttributes({ATTR_OFFSET, ATTR_SIZE})
public class BooksControllerAdd {

	@Autowired BookRepository bookRepository;
	@Autowired ScrollParamsLimiter scrollParamsLimiter;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public RedirectView add(
			@ModelAttribute(value = ATTR_OFFSET) int paramOffset,
			@ModelAttribute(value = ATTR_SIZE) int paramSize,
			@ModelAttribute(value = "newBook") Book book)
	{
		bookRepository.create(book);
		return new RedirectView("list");
	}

}
