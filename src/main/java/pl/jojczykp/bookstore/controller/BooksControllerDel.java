package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.command.BookCommand;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.repository.BookRepository;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_DEL;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_LIST;

@Controller
public class BooksControllerDel {

	@Autowired private BookRepository bookRepository;

	@RequestMapping(value = URL_ACTION_DEL, method = POST)
	public RedirectView del(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		for (BookCommand bookCommand : booksCommand.getBooks()) {
			if (bookCommand.isChecked()) {
				bookRepository.delete(bookCommand.getId());
			}
		}

		redirectAttributes.addFlashAttribute(BOOKS_COMMAND, booksCommand);
		return new RedirectView(URL_ACTION_LIST);
	}
}
