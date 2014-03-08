package pl.jojczykp.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.assemblers.BookAssembler;
import pl.jojczykp.bookstore.commands.BooksCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.BooksCreateValidator;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controllers.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_CREATE;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_READ;

@Controller
public class BooksControllerCreate {

	@Autowired private BooksCreateValidator booksCreateValidator;
	@Autowired private BooksRepository booksRepository;
	@Autowired private BookAssembler bookAssembler;

	@RequestMapping(value = URL_ACTION_CREATE, method = POST)
	public RedirectView create(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult)
	{
		booksCreateValidator.validate(booksCommand, bindingResult);
		if (bindingResult.hasErrors()) {
			processWhenCommandInvalid(booksCommand, bindingResult);
		} else {
			processWhenCommandValid(booksCommand);
		}

		return redirectToRead(booksCommand, redirectAttributes);
	}

	private void processWhenCommandInvalid(BooksCommand booksCommand, BindingResult bindingResult) {
		for(ObjectError error: bindingResult.getAllErrors()) {
			booksCommand.getMessages().addErrors(error.getDefaultMessage());
		}
	}

	private void processWhenCommandValid(BooksCommand booksCommand) {
		booksRepository.create(bookAssembler.toDomain(booksCommand.getNewBook()));
		booksCommand.getMessages().addInfos("Object created.");
	}

	private RedirectView redirectToRead(BooksCommand booksCommand, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(BOOKS_COMMAND, booksCommand);
		return new RedirectView(URL_ACTION_READ);
	}

}
