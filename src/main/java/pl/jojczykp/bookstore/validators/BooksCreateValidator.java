package pl.jojczykp.bookstore.validators;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.jojczykp.bookstore.commands.BooksCommand;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Service
public class BooksCreateValidator implements Validator {

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
