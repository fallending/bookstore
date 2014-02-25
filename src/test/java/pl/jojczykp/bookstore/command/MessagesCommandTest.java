package pl.jojczykp.bookstore.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessagesCommandTest {

	private static final String MESSAGE = "sample message";

	@Mock private List<String> infos;
	@Mock private List<String> warns;
	@Mock private List<String> errors;

	@InjectMocks private MessagesCommand testee;

	@Test
	public void shouldAddInfo() {
		testee.addInfo(MESSAGE);

		verify(infos).add(MESSAGE);
	}

	@Test
	public void shouldGetInfos() {
		List<String> result = testee.getInfos();

		assertThat(result, is(sameInstance(infos)));
	}

	@Test
	public void shouldAddWarn() {
		testee.addWarn(MESSAGE);

		verify(warns).add(MESSAGE);
	}

	@Test
	public void shouldGetWarns() {
		List<String> result = testee.getWarns();

		assertThat(result, is(sameInstance(warns)));
	}

	@Test
	public void shouldAddError() {
		testee.addError(MESSAGE);

		verify(errors).add(MESSAGE);
	}

	@Test
	public void shouldGetErrors() {
		List<String> result = testee.getErrors();

		assertThat(result, is(sameInstance(errors)));
	}

}
