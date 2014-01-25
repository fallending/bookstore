package pl.jojczykp.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_LIST;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_UPDATE;

@Controller
public class BooksControllerUpdate {

	@Autowired private BookRepository bookRepository;

	@RequestMapping(value = URL_ACTION_UPDATE, method = POST)
	public RedirectView update(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		int id = booksCommand.getUpdateBookId();
		bookRepository.update(new Book(id, booksCommand.getUpdateBookTitle()));

		redirectAttributes.addFlashAttribute(BOOKS_COMMAND, booksCommand);
		return new RedirectView(URL_ACTION_LIST);
	}
}
