package pl.jojczykp.bookstore.commands.books;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.jojczykp.bookstore.commands.common.PagerCommand;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

public class CreateBookCommandUnitTest {

	private CreateBookCommand testee;

	@Before
	public void setupInstance() {
		testee = new CreateBookCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getPager(), is(notNullValue()));
		assertThat(testee.getTitle(), is(isEmptyString()));
	}

	@Test
	public void shouldSetPager() {
		final PagerCommand pager = new PagerCommand();

		testee.setPager(pager);

		assertThat(testee.getPager(), sameInstance(pager));
	}

	@Test
	public void shouldSetTitle() {
		final String title = "some title";

		testee.setTitle(title);

		assertThat(testee.getTitle(), equalTo(title));
	}

	@Test
	public void shouldSetFile() {
		final byte[] content = {1, 2, 3};
		final MultipartFile file = new MockMultipartFile("aName", content);

		testee.setFile(file);

		assertThat(testee.getFile(), is(equalTo(file)));
	}

}
