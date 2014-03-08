package pl.jojczykp.bookstore.validators;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import pl.jojczykp.bookstore.commands.BookCommand;
import pl.jojczykp.bookstore.commands.BooksCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BooksUpdateValidatorUnitTest {

	private BooksUpdateValidator testee;

	@Before
	public void setUpTestee() {
		testee = new BooksUpdateValidator();
	}

	@Test
	public void shouldSupportBooksCommand() {
		final Class<?> clazz = BooksCommand.class;

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
		final BooksCommand command = anUpdateBooksCommandWith(notEmptyTitle);
		final Errors errors = new BeanPropertyBindingResult(command, "someObjectName");

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(false));
	}

	@Test
	public void shouldFindErrorsWhenInvalidData() {
		final String emptyTitle = "";
		final BooksCommand command = anUpdateBooksCommandWith(emptyTitle);
		final Errors errors = new BeanPropertyBindingResult(command, "someObjectName");

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getErrorCount(), is(equalTo(1)));
		assertThatFieldErrorHas(errors.getFieldError("updatedBook.title"),
				"updatedBook.title.empty", "Updating with empty title is not allowed.");
	}

	private void assertThatFieldErrorHas(FieldError fieldError, String fieldCode, String defaultMessage) {
		assertThat(fieldError.getCode(), is(equalTo(fieldCode)));
		assertThat(fieldError.getDefaultMessage(), is(equalTo(defaultMessage)));
	}

	private BooksCommand anUpdateBooksCommandWith(String title) {
		BooksCommand booksCommand = new BooksCommand();
		BookCommand updatedBook = new BookCommand();
		booksCommand.setUpdatedBook(updatedBook);
		updatedBook.setTitle(title);

		return booksCommand;
	}
}
