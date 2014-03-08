package pl.jojczykp.bookstore.validators;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import pl.jojczykp.bookstore.commands.BooksCommand;
import pl.jojczykp.bookstore.commands.PagerCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BooksSetPageSizeValidatorUnitTest {

	private BooksSetPageSizeValidator testee;

	@Before
	public void setUpTestee() {
		testee = new BooksSetPageSizeValidator();
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
		final int pageSize = 10;
		final BooksCommand command = aSetPageSizeBooksCommandWith(pageSize);
		final Errors errors = new BeanPropertyBindingResult(command, "someObjectName");

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(false));
	}

	@Test
	public void shouldFindErrorsWhenInvalidData() {
		final int pageSize = -2;
		final BooksCommand command = aSetPageSizeBooksCommandWith(pageSize);
		final Errors errors = new BeanPropertyBindingResult(command, "someObjectName");

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getErrorCount(), is(equalTo(1)));
		assertThatFieldErrorHas(errors.getFieldError("pager.pageSize"),
				"pager.pageSize.notPositive", "Negative or zero page size is not allowed. Defaults used.");
	}

	private void assertThatFieldErrorHas(FieldError fieldError, String fieldCode, String defaultMessage) {
		assertThat(fieldError.getCode(), is(equalTo(fieldCode)));
		assertThat(fieldError.getDefaultMessage(), is(equalTo(defaultMessage)));
	}

	private BooksCommand aSetPageSizeBooksCommandWith(int pageSize) {
		BooksCommand booksCommand = new BooksCommand();
		PagerCommand pager = new PagerCommand();
		booksCommand.setPager(pager);
		pager.setPageSize(pageSize);

		return booksCommand;
	}
}
