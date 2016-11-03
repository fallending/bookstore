package pl.jojczykp.bookstore.controllers.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.services.books.CreateBookService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.consts.BooksConsts.CREATE_BOOK_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_CREATE;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DISPLAY;

@Controller
public class CreateBookController {

	@Autowired private CreateBookService createBookService;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value = URL_ACTION_CREATE, method = POST)
	public RedirectView create(
			@ModelAttribute(CREATE_BOOK_COMMAND) CreateBookCommand createBookCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult,
			HttpServletRequest request)
	{
		DisplayBooksCommand displayBooksCommand = createBookService.create(createBookCommand, bindingResult);

		redirectAttributes.addFlashAttribute(DISPLAY_BOOKS_COMMAND, displayBooksCommand);
		return new RedirectView(request.getContextPath() + URL_ACTION_DISPLAY);
	}

}
