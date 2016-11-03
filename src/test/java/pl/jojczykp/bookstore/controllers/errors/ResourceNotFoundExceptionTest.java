package pl.jojczykp.bookstore.controllers.errors;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class ResourceNotFoundExceptionTest {

	@Test
	public void shouldHaveConstructorWithMessage() {
		final String message = "some message";

		ResourceNotFoundException exception = new ResourceNotFoundException(message);

		assertThat(exception.getMessage(), is(equalTo(message)));
	}

	@Test
	public void shouldHaveConstructorWithMessageAndCause() {
		final Throwable cause = new Exception("cause exception message");
		final String message = "some message";

		ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

		assertThat(exception.getMessage(), is(equalTo(message)));
		assertThat(exception.getCause(), is(sameInstance(cause)));
	}

}
