package pl.jojczykp.bookstore.controller;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.assembler.BookAssembler;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.validators.UpdateValidator;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controller.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_READ;
import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_UPDATE;

@Controller
public class BooksControllerUpdate {

	@Autowired private UpdateValidator updateValidator;
	@Autowired private BookRepository bookRepository;
	@Autowired private BookAssembler bookAssembler;

	@RequestMapping(value = URL_ACTION_UPDATE, method = POST)
	public RedirectView update(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult)
	{
		updateValidator.validate(booksCommand, bindingResult);
		if (bindingResult.hasErrors()) {
			processWhenCommandInvalid(booksCommand, bindingResult);
		} else {
			processWhenCommandValid(booksCommand);
		}

		redirectAttributes.addFlashAttribute(BOOKS_COMMAND, bindingResult.getTarget());
		return new RedirectView(URL_ACTION_READ);
	}

	private void processWhenCommandInvalid(BooksCommand booksCommand, BindingResult bindingResult) {
		for(ObjectError error: bindingResult.getAllErrors()) {
			booksCommand.getMessages().addError(error.getDefaultMessage());
		}
	}

	private void processWhenCommandValid(BooksCommand booksCommand) {
		try {
			bookRepository.update(bookAssembler.toDomain(booksCommand.getUpdatedBook()));
			booksCommand.getMessages().addInfo("Object updated.");
		} catch (StaleObjectStateException e) {
			booksCommand.getMessages().addWarn(
					"Object updated or deleted by another user. Please try again with actual data.");
		}
	}

}
