package pl.jojczykp.bookstore.commands.books;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

public class DisplayBookCommandUnitTest {

	private DisplayBookCommand testee;

	@Before
	public void setupInstance() {
		testee = new DisplayBookCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getId(), is(0));
		assertThat(testee.getVersion(), is(0));
		assertThat(testee.getTitle(), isEmptyString());
		assertThat(testee.getIconName(), isEmptyString());
	}

	@Test
	public void shouldSetId() {
		final int id = 2;

		testee.setId(id);

		assertThat(testee.getId(), is(equalTo(id)));
	}

	@Test
	public void shouldSetVersion() {
		final int version = 54;

		testee.setVersion(version);

		assertThat(testee.getVersion(), is(equalTo(version)));
	}

	@Test
	public void shouldSetTitle() {
		final String title = "some title";

		testee.setTitle(title);

		assertThat(testee.getTitle(), is(equalTo(title)));
	}

	@Test
	public void shouldFileType() {
		final String fileType = "fileType";

		testee.setIconName(fileType);

		assertThat(testee.getIconName(), is(equalTo(fileType)));
	}

}
