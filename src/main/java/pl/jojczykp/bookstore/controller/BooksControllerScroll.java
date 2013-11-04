package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParams;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_LIST;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_NEXT;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_PREV;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_SET_PAGE_SIZE;

@Controller
public class BooksControllerScroll {

	@Autowired BookRepository bookRepository;
	@Autowired ScrollParamsLimiter scrollParamsLimiter;

	@RequestMapping(value = URL_ACTION_PREV, method = POST)
	public RedirectView prev(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		ScrollParams originalScrollParams = booksCommand.getOriginalScrollParams();
		originalScrollParams.setOffset(originalScrollParams.getOffset() - originalScrollParams.getSize());

		return redirectToList(booksCommand, redirectAttributes);
	}

	@RequestMapping(value = URL_ACTION_NEXT, method = POST)
	public RedirectView next(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		ScrollParams originalScrollParams = booksCommand.getOriginalScrollParams();
		originalScrollParams.setOffset(originalScrollParams.getOffset() + originalScrollParams.getSize());

		return redirectToList(booksCommand, redirectAttributes);
	}

	@RequestMapping(value = URL_ACTION_SET_PAGE_SIZE, method = POST)
	public RedirectView setPageSize(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		return redirectToList(booksCommand, redirectAttributes);
	}

	private RedirectView redirectToList(BooksCommand booksCommand, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(BOOKS_COMMAND, booksCommand);
		return new RedirectView(URL_ACTION_LIST);
	}
}
