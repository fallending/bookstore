package pl.jojczykp.bookstore.validators;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.jojczykp.bookstore.commands.books.ChangePagerCommand;

@Service
public class ChangePagerValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (ChangePagerCommand.class.equals(clazz));
	}

	@Override
	public void validate(Object object, Errors errors) {
		ChangePagerCommand command = (ChangePagerCommand) object;
		int pageSize = command.getPager().getPageSize();

		if (pageSize <= 0) {
			errors.rejectValue("pager.pageSize", "pager.pageSize.notPositive",
					"Negative or zero page size is not allowed. Defaults used.");
		}
	}
}
