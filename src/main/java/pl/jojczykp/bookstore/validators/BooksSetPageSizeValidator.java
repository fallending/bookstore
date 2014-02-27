package pl.jojczykp.bookstore.validators;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.jojczykp.bookstore.command.BooksCommand;

@Service
public class BooksSetPageSizeValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (BooksCommand.class.equals(clazz));
	}

	@Override
	public void validate(Object object, Errors errors) {
		BooksCommand command = (BooksCommand) object;
		int pageSize = command.getPager().getPageSize();

		if (pageSize <= 0) {
			errors.rejectValue("pager.pageSize", "pager.pageSize.notPositive",
					"Negative or zero page size is not allowed. Defaults used.");
		}
	}
}
