package pl.jojczykp.bookstore.command;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BookCommandTest {

	private BookCommand testee;

	@Before
	public void setupInstance() {
		testee = new BookCommand();
	}

	@Test
	public void shouldSetChecked() {
		final boolean checked = true;

		testee.setChecked(checked);

		assertThat(testee.isChecked(), is(checked));
	}

	@Test
	public void shouldSetId() {
		final int id = 2;

		testee.setId(id);

		assertThat(testee.getId(), equalTo(id));
	}

	@Test
	public void shouldSetVersion() {
		final int version = 54;

		testee.setVersion(version);

		assertThat(testee.getVersion(), equalTo(version));
	}

	@Test
	public void shouldSetTitle() {
		final String title = "some title";

		testee.setTitle(title);

		assertThat(testee.getTitle(), equalTo(title));
	}

}
