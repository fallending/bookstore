package pl.jojczykp.bookstore.commands;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessagesCommandUnitTest {

	private static final String MESSAGE_1 = "sample message 1";
	private static final String MESSAGE_2 = "sample message 2";

	@Mock private List<String> infos;
	@Mock private List<String> warns;
	@Mock private List<String> errors;

	@InjectMocks private MessagesCommand testee;

	@Test
	public void shouldAddInfo() {
		testee.addInfos(MESSAGE_1, MESSAGE_2);

		verify(infos).addAll(asList(MESSAGE_1, MESSAGE_2));
	}

	@Test
	public void shouldGetInfos() {
		List<String> result = testee.getInfos();

		assertThat(result, is(sameInstance(infos)));
	}

	@Test
	public void shouldAddWarn() {
		testee.addWarns(MESSAGE_1, MESSAGE_2);

		verify(warns).addAll(asList(MESSAGE_1, MESSAGE_2));
	}

	@Test
	public void shouldGetWarns() {
		List<String> result = testee.getWarns();

		assertThat(result, is(sameInstance(warns)));
	}

	@Test
	public void shouldAddError() {
		testee.addErrors(MESSAGE_1, MESSAGE_2);

		verify(errors).addAll(asList(MESSAGE_1, MESSAGE_2));
	}

	@Test
	public void shouldGetErrors() {
		List<String> result = testee.getErrors();

		assertThat(result, is(sameInstance(errors)));
	}

}
