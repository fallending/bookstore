package pl.jojczykp.bookstore.controllers.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.services.books.DisplayBooksService;
import pl.jojczykp.bookstore.utils.BooksCommandFactory;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_VIEW;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DISPLAY;

@Controller
public class DisplayBooksController {

	@Autowired private BooksCommandFactory booksCommandFactory;
	@Autowired private DisplayBooksService displayBooksService;

	@ModelAttribute(DISPLAY_BOOKS_COMMAND)
	public DisplayBooksCommand getDefaultCommand() {
		return booksCommandFactory.create();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = URL_ACTION_DISPLAY, method = GET)
	public ModelAndView display(
			@ModelAttribute(DISPLAY_BOOKS_COMMAND) DisplayBooksCommand displayBooksCommand)
	{
		DisplayBooksCommand resultDisplayBooksCommand = displayBooksService.display(displayBooksCommand);

		return new ModelAndView(DISPLAY_BOOKS_VIEW, aModelFor(resultDisplayBooksCommand));
	}

	private ModelMap aModelFor(DisplayBooksCommand displayBooksCommand) {
		return new ModelMap().addAttribute(DISPLAY_BOOKS_COMMAND, displayBooksCommand);
	}

}
