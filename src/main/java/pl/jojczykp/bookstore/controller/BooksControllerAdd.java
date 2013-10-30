package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParams;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controller.BooksConsts.MODEL_ATTR_SCROLL_PARAMS;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_ADD;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_LIST;

@Controller
public class BooksControllerAdd {

	@Autowired BookRepository bookRepository;
	@Autowired ScrollParamsLimiter scrollParamsLimiter;

	@RequestMapping(value = URL_ACTION_ADD, method = POST)
	public RedirectView add(
			@ModelAttribute(MODEL_ATTR_SCROLL_PARAMS) ScrollParams scrollParams,
			@ModelAttribute(value = "newBook") Book newBook,
			RedirectAttributes redirectAttributes)
	{
		bookRepository.create(newBook);

		redirectAttributes.addFlashAttribute(MODEL_ATTR_SCROLL_PARAMS, scrollParams);
		return new RedirectView(URL_ACTION_LIST);
	}
}
