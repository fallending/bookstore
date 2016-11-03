package pl.jojczykp.bookstore.services.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.jojczykp.bookstore.assemblers.CreateBookAssembler;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.CreateBookValidator;

@Service
public class CreateBookService {

	@Autowired private CreateBookValidator createBookValidator;
	@Autowired private CreateBookAssembler createBookAssembler;
	@Autowired private BooksRepository booksRepository;

	public DisplayBooksCommand create(CreateBookCommand createBookCommand, BindingResult bindingResult) {
		createBookValidator.validate(createBookCommand, bindingResult);

		if (bindingResult.hasErrors()) {
			return processWhenCommandInvalid(createBookCommand, bindingResult);
		} else {
			return processWhenCommandValid(createBookCommand);
		}
	}

	private DisplayBooksCommand processWhenCommandInvalid(
										CreateBookCommand createBookCommand, BindingResult bindingResult) {
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(createBookCommand.getPager());

		for (ObjectError error: bindingResult.getAllErrors()) {
			displayBooksCommand.getMessages().addErrors(error.getDefaultMessage());
		}

		return displayBooksCommand;
	}

	private DisplayBooksCommand processWhenCommandValid(CreateBookCommand createBookCommand) {
		booksRepository.create(createBookAssembler.toDomain(createBookCommand));

		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(createBookCommand.getPager());
		displayBooksCommand.getMessages().addInfos("Object created.");

		return displayBooksCommand;
	}

}
