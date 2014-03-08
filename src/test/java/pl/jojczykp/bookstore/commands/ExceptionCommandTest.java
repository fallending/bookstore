package pl.jojczykp.bookstore.commands;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ExceptionCommandTest {

	private ExceptionCommand testee;

	@Before
	public void setUpTestee() {
		testee = new ExceptionCommand();
	}

	@Test
	public void shouldSetMessage() {
		final String message = "some message";

		testee.setMessage(message);

		assertThat(testee.getMessage(), equalTo(message));
	}

	@Test
	public void shouldSetStackTraceAsText() {
		final String stackTraceContent = "stacktrace content";

		testee.setStackTraceAsString(stackTraceContent);

		assertThat(testee.getStackTraceAsString(), equalTo(stackTraceContent));
	}
}
