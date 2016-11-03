package pl.jojczykp.bookstore.validators;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Service
public class CreateBookValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (CreateBookCommand.class.equals(clazz));
	}

	@Override
	public void validate(Object object, Errors errors) {
		rejectIfEmptyOrWhitespace(errors, "title", "title.empty", "Creating with empty title is not allowed.");
		rejectIfNoFileProvided(errors, "file", "file.empty", "Creating with no file is not allowed.");
	}

	private static void rejectIfNoFileProvided(Errors errors, String field, String errorCode, String defaultMessage) {
		MultipartFile value = (MultipartFile) errors.getFieldValue(field);
		if (value == null || !StringUtils.hasText(value.getOriginalFilename())) {
			errors.rejectValue(field, errorCode, defaultMessage);
		}
	}
}
