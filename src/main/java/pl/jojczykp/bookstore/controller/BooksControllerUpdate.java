package pl.jojczykp.bookstore.controller;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;

import java.util.Map;

import static com.google.inject.internal.ImmutableMap.of;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_READ;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_UPDATE;

@Controller
public class BooksControllerUpdate {

	private static final boolean UPDATE_SUCCESS = true;
	private static final boolean UPDATE_FAILURE = false;
	private static final Map<Boolean, String> SUCCESS_STATUS_MESSAGES = of(
			UPDATE_SUCCESS, "Object updated.",
			UPDATE_FAILURE, "Object updated or deleted by another user. Please try again with actual data."
	);

	@Autowired private BookRepository bookRepository;

	@RequestMapping(value = URL_ACTION_UPDATE, method = POST)
	public RedirectView update(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		boolean updateSuccessStatus = tryUpdate(
				booksCommand.getUpdatedBook().getId(),
				booksCommand.getUpdatedBook().getVersion(),
				booksCommand.getUpdatedBook().getTitle()
		);

		booksCommand.getMessages().addInfo(messageFor(updateSuccessStatus));

		redirectAttributes.addFlashAttribute(BOOKS_COMMAND, booksCommand);
		return new RedirectView(URL_ACTION_READ);
	}

	private boolean tryUpdate(int id, int version, String title) {
		try {
			bookRepository.update(new Book(id, version, title));
			return UPDATE_SUCCESS;
		} catch (StaleObjectStateException e) {
			return UPDATE_FAILURE;
		}
	}

	private String messageFor(boolean updateSuccessStatus) {
		return SUCCESS_STATUS_MESSAGES.get(updateSuccessStatus);
	}

}
