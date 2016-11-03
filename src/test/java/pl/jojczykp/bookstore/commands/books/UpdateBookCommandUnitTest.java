package pl.jojczykp.bookstore.commands.books;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.commands.common.PagerCommand;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UpdateBookCommandUnitTest {

	private UpdateBookCommand testee;

	@Before
	public void setupInstance() {
		testee = new UpdateBookCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getPager(), is(notNullValue()));
		assertThat(testee.getId(), is(0));
		assertThat(testee.getVersion(), is(0));
		assertThat(testee.getTitle(), is(equalTo("")));
	}

	@Test
	public void shouldSetPager() {
		final PagerCommand pager = new PagerCommand();

		testee.setPager(pager);

		assertThat(testee.getPager(), sameInstance(pager));
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
