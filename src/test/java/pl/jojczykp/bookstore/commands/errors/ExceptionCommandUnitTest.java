package pl.jojczykp.bookstore.commands.errors;

import org.junit.Before;
import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ExceptionCommandUnitTest {

	private static final String MESSAGE = "some message";
	private static final String STACK_TRACE = "some stack trace";

	private ExceptionCommand testee;

	@Before
	public void setUpTestee() {
		testee = new ExceptionCommand();
	}

	@Test
	public void shouldSetMessage() {
		testee.setMessage(MESSAGE);

		assertThat(testee.getMessage(), is(equalTo(MESSAGE)));
	}

	@Test
	public void shouldSetStackTraceAsText() {
		testee.setStackTraceAsString(STACK_TRACE);

		assertThat(testee.getStackTraceAsString(), is(equalTo(STACK_TRACE)));
	}

	@Test
	public void shouldHaveToStringForDiagnostics() {
		testee.setMessage(MESSAGE);
		testee.setStackTraceAsString(STACK_TRACE);

		assertThat(testee.toString(), is(equalTo(format("message: %s\nstackTrace: %s", MESSAGE, STACK_TRACE))));
	}

}
