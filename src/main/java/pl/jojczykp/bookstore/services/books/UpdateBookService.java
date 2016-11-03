package pl.jojczykp.bookstore.services.books;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.jojczykp.bookstore.assemblers.UpdateBookAssembler;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.UpdateBookValidator;

@Service
public class UpdateBookService {

	@Autowired private UpdateBookValidator updateBookValidator;
	@Autowired private UpdateBookAssembler updateBookAssembler;
	@Autowired private BooksRepository booksRepository;

	public DisplayBooksCommand update(UpdateBookCommand updateBookCommand, BindingResult bindingResult) {
		updateBookValidator.validate(updateBookCommand, bindingResult);

		if (bindingResult.hasErrors()) {
			return processWhenCommandInvalid(updateBookCommand, bindingResult);
		} else {
			return processWhenCommandValid(updateBookCommand);
		}
	}

	private DisplayBooksCommand processWhenCommandInvalid(
			UpdateBookCommand updateBookCommand, BindingResult bindingResult) {
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(updateBookCommand.getPager());

		for(ObjectError error: bindingResult.getAllErrors()) {
			displayBooksCommand.getMessages().addErrors(error.getDefaultMessage());
		}

		return displayBooksCommand;
	}

	private DisplayBooksCommand processWhenCommandValid(UpdateBookCommand updateBookCommand) {
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(updateBookCommand.getPager());

		try {
			booksRepository.update(updateBookAssembler.toDomain(updateBookCommand));
			displayBooksCommand.getMessages().addInfos("Title updated.");
		} catch (StaleObjectStateException e) {
			displayBooksCommand.getMessages().addWarns(
					"Object updated or deleted by another user. Please try again with actual data.");
		}

		return displayBooksCommand;
	}

}
