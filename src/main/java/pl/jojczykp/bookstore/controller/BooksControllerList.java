package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParams;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.controller.BooksConsts.MODEL_ATTR_BOOKS;
import static pl.jojczykp.bookstore.controller.BooksConsts.MODEL_ATTR_NEW_BOOK;
import static pl.jojczykp.bookstore.controller.BooksConsts.MODEL_ATTR_SCROLL_PARAMS;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_LIST;
import static pl.jojczykp.bookstore.controller.BooksConsts.VIEW_BOOKS;

@Controller
public class BooksControllerList {

	@Autowired BookRepository bookRepository;
	@Autowired ScrollParamsLimiter scrollParamsLimiter;

	@ModelAttribute(MODEL_ATTR_SCROLL_PARAMS)
	public ScrollParams getDefaultScrollParams() {
		return new ScrollParams(0, 10, 0);
	}

	@RequestMapping(value = URL_ACTION_LIST, method = GET)
	public ModelAndView list(
			@ModelAttribute(MODEL_ATTR_SCROLL_PARAMS) ScrollParams scrollParams)
	{
		scrollParams.setTotalCount(bookRepository.totalCount());
		scrollParamsLimiter.limit(scrollParams);

		List<Book> books = bookRepository.read(scrollParams.getOffset(), scrollParams.getSize());

		return new ModelAndView(VIEW_BOOKS, createModelFor(scrollParams, books));
	}

	private ModelMap createModelFor(ScrollParams scrollParams, List<Book> books) {
		ModelMap model = new ModelMap();

		model.addAttribute(MODEL_ATTR_SCROLL_PARAMS, scrollParams);
		model.addAttribute(MODEL_ATTR_NEW_BOOK, new Book());
		model.addAttribute(MODEL_ATTR_BOOKS, books);

		return model;
	}

}
