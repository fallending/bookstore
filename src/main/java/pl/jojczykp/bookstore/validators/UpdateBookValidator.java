package pl.jojczykp.bookstore.validators;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Service
public class UpdateBookValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (UpdateBookCommand.class.equals(clazz));
	}

	@Override
	public void validate(Object object, Errors errors) {
		rejectIfEmptyOrWhitespace(errors, "title", "title.empty", "Updating with empty title is not allowed.");
	}
}
