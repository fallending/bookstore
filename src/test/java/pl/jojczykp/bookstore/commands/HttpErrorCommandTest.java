package pl.jojczykp.bookstore.commands;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpErrorCommandTest {

	private HttpErrorCommand testee;

	@Before
	public void setUpTestee() {
		testee = new HttpErrorCommand();
	}

	@Test
	public void shouldSetId() {
		final int id = 8;

		testee.setId(id);

		assertThat(testee.getId(), is(id));
	}

	@Test
	public void shouldSetOriginalUrl() {
		final String originalUrl = "originalUrl";

		testee.setOriginalUrl(originalUrl);

		assertThat(testee.getOriginalUrl(), is(equalTo(originalUrl)));
	}

	@Test
	public void shouldSetDescription() {
		final String description = "description";

		testee.setDescription(description);

		assertThat(testee.getDescription(), is(equalTo(description)));
	}

}
