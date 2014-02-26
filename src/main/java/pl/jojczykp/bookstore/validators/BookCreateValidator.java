package pl.jojczykp.bookstore.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.jojczykp.bookstore.command.BooksCommand;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class BookCreateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (BooksCommand.class.equals(clazz));
	}

	@Override
	public void validate(Object object, Errors errors) {
		rejectIfEmptyOrWhitespace(errors,
				"newBook.title", "newBook.title.empty", "Creating with empty title is not allowed.");
	}
}
