package pl.jojczykp.bookstore.controllers.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.commands.books.ChangePagerCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.services.books.ChangeBooksPagerService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.consts.BooksConsts.CHANGE_PAGER_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DISPLAY;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_GO_TO_PAGE;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_SET_PAGE_SIZE;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_SORT;

@Controller
public class ChangeBooksPagerController {

	@Autowired private ChangeBooksPagerService changeBooksPagerService;

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = URL_ACTION_SORT, method = POST)
	public RedirectView sort(
			@ModelAttribute(CHANGE_PAGER_COMMAND) ChangePagerCommand changePagerCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult,
			HttpServletRequest request)
	{
		DisplayBooksCommand displayBooksCommand = changeBooksPagerService.sort(changePagerCommand, bindingResult);

		return redirect(request, displayBooksCommand, redirectAttributes);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = URL_ACTION_GO_TO_PAGE, method = POST)
	public RedirectView goToPage(
			@ModelAttribute(CHANGE_PAGER_COMMAND) ChangePagerCommand changePagerCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult,
			HttpServletRequest request)
	{
		DisplayBooksCommand displayBooksCommand = changeBooksPagerService.goToPage(changePagerCommand, bindingResult);

		return redirect(request, displayBooksCommand, redirectAttributes);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = URL_ACTION_SET_PAGE_SIZE, method = POST)
	public RedirectView setPageSize(
			@ModelAttribute(CHANGE_PAGER_COMMAND) ChangePagerCommand changePagerCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult,
			HttpServletRequest request)
	{
		DisplayBooksCommand displayBooksCommand = changeBooksPagerService
														.setPageSize(changePagerCommand, bindingResult);

		return redirect(request, displayBooksCommand, redirectAttributes);
	}

	private RedirectView redirect(HttpServletRequest request, DisplayBooksCommand displayBooksCommand,
									RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(DISPLAY_BOOKS_COMMAND, displayBooksCommand);
		return new RedirectView(request.getContextPath() + URL_ACTION_DISPLAY);
	}

}
