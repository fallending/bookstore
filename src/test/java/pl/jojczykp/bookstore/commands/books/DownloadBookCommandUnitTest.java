package pl.jojczykp.bookstore.commands.books;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class DownloadBookCommandUnitTest {

	private DownloadBookCommand testee;

	@Before
	public void createTestee() {
		testee = new DownloadBookCommand();
	}

	@Test
	public void shouldSetId() {
		final String id = "2";

		testee.setId(id);

		assertThat(testee.getId(), is(sameInstance(id)));
	}

}
