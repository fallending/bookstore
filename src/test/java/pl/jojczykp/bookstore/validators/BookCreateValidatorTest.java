package pl.jojczykp.bookstore.validators;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import pl.jojczykp.bookstore.command.BookCommand;
import pl.jojczykp.bookstore.command.BooksCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BookCreateValidatorTest {

	private BookCreateValidator testee;

	@Before
	public void setUpTestee() {
		testee = new BookCreateValidator();
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
		final BooksCommand command = aCreateBooksCommandWith(notEmptyTitle);
		final Errors errors = new BeanPropertyBindingResult(command, "someObjectName");

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(false));
	}

	@Test
	public void shouldFindErrorsWhenInvalidData() {
		final String emptyTitle = "";
		final BooksCommand command = aCreateBooksCommandWith(emptyTitle);
		final Errors errors = new BeanPropertyBindingResult(command, "someObjectName");

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getErrorCount(), is(equalTo(1)));
		assertThatFieldErrorHas(errors.getFieldError("newBook.title"),
				"newBook.title.empty", "Creating with empty title is not allowed.");
	}

	private void assertThatFieldErrorHas(FieldError fieldError, String fieldCode, String defaultMessage) {
		assertThat(fieldError.getCode(), is(equalTo(fieldCode)));
		assertThat(fieldError.getDefaultMessage(), is(equalTo(defaultMessage)));
	}

	private BooksCommand aCreateBooksCommandWith(String title) {
		BooksCommand booksCommand = new BooksCommand();
		BookCommand newBook = new BookCommand();
		booksCommand.setNewBook(newBook);
		newBook.setTitle(title);

		return booksCommand;
	}
}
