package pl.jojczykp.bookstore.services.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.jojczykp.bookstore.commands.books.ChangePagerCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.validators.ChangePagerValidator;

@Service
public class ChangeBooksPagerService {

	@Autowired private ChangePagerValidator changePagerValidator;

	@Value("${view.books.defaultPageSize}") private int defaultPageSize;

	public DisplayBooksCommand sort(ChangePagerCommand changePagerCommand, BindingResult bindingResult) {
		return createValidatedCommandCopy(changePagerCommand, bindingResult);
	}

	public DisplayBooksCommand goToPage(ChangePagerCommand changePagerCommand, BindingResult bindingResult) {
		return createValidatedCommandCopy(changePagerCommand, bindingResult);
	}

	public DisplayBooksCommand setPageSize(ChangePagerCommand changePagerCommand, BindingResult bindingResult) {
		return createValidatedCommandCopy(changePagerCommand, bindingResult);
	}

	private DisplayBooksCommand createValidatedCommandCopy(
										ChangePagerCommand changePagerCommand, BindingResult bindingResult)
	{
		changePagerValidator.validate(changePagerCommand, bindingResult);

		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(changePagerCommand.getPager());
		setDefaultPageSizeIfNegative(displayBooksCommand);
		setErrorMessagesIfPresent(bindingResult, displayBooksCommand);

		return displayBooksCommand;
	}

	private void setDefaultPageSizeIfNegative(DisplayBooksCommand displayBooksCommand) {
		if (displayBooksCommand.getPager().getPageSize() <= 0) {
			displayBooksCommand.getPager().setPageSize(defaultPageSize);
		}
	}

	private void setErrorMessagesIfPresent(BindingResult bindingResult, DisplayBooksCommand displayBooksCommand) {
		for (ObjectError error: bindingResult.getAllErrors()) {
			displayBooksCommand.getMessages().addErrors(error.getDefaultMessage());
		}
	}

}
