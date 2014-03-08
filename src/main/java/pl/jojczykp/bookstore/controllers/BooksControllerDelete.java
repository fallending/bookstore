package pl.jojczykp.bookstore.controllers;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.commands.BookCommand;
import pl.jojczykp.bookstore.commands.BooksCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controllers.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_DELETE;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_READ;

@Controller
public class BooksControllerDelete {

	@Autowired private BooksRepository booksRepository;

	@RequestMapping(value = URL_ACTION_DELETE, method = POST)
	public RedirectView delete(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		for (BookCommand bookCommand : booksCommand.getBooks()) {
			if (bookCommand.isChecked()) {
				deleteBookFromRepository(bookCommand.getId(), booksCommand);
			}
		}

		return redirectToRead(booksCommand, redirectAttributes);
	}

	private void deleteBookFromRepository(int bookId, BooksCommand messagesContainer) {
		try {
			booksRepository.delete(bookId);
			messagesContainer.getMessages().addInfos("Object deleted.");
		} catch (ObjectNotFoundException ex) {
			messagesContainer.getMessages().addWarns("Object already deleted.");
		}
	}

	private RedirectView redirectToRead(BooksCommand booksCommand, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(BOOKS_COMMAND, booksCommand);
		return new RedirectView(URL_ACTION_READ);
	}

}
