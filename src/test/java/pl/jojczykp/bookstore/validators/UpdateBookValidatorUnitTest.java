package pl.jojczykp.bookstore.validators;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UpdateBookValidatorUnitTest {

	private UpdateBookValidator testee;

	@Before
	public void setUpTestee() {
		testee = new UpdateBookValidator();
	}

	@Test
	public void shouldSupportCorrectCommandClass() {
		final Class<?> clazz = UpdateBookCommand.class;

		boolean supports = testee.supports(clazz);

		assertThat(supports, is(true));
	}

	@Test
	public void shouldNotSupportOtherClass() {
		final Class<?> clazz = Object.class;

		boolean supports = testee.supports(clazz);

		assertThat(supports, is(false));
	}

	@Test
	public void shouldPassWhenValidData() {
		final String notEmptyTitle = "not empty new title";
		final UpdateBookCommand command = anUpdateBookCommandWith(notEmptyTitle);
		final Errors errors = new BeanPropertyBindingResult(command, "someObjectName");

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(false));
	}

	@Test
	public void shouldFindErrorsWhenInvalidData() {
		final String emptyTitle = "";
		final UpdateBookCommand command = anUpdateBookCommandWith(emptyTitle);
		final Errors errors = new BeanPropertyBindingResult(command, "someObjectName");

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getErrorCount(), is(equalTo(1)));
		assertThatFieldErrorHas(errors.getFieldError("title"),
				"title.empty", "Updating with empty title is not allowed.");
	}

	private void assertThatFieldErrorHas(FieldError fieldError, String fieldCode, String defaultMessage) {
		assertThat(fieldError.getCode(), is(equalTo(fieldCode)));
		assertThat(fieldError.getDefaultMessage(), is(equalTo(defaultMessage)));
	}

	private UpdateBookCommand anUpdateBookCommandWith(String title) {
		UpdateBookCommand updateBookCommand = new UpdateBookCommand();
		updateBookCommand.setTitle(title);

		return updateBookCommand;
	}
}
