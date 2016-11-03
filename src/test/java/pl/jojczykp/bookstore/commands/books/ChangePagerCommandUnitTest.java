package pl.jojczykp.bookstore.commands.books;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.commands.common.PagerCommand;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChangePagerCommandUnitTest {

	private ChangePagerCommand testee;

	@Before
	public void setupInstance() {
		testee = new ChangePagerCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getPager(), is(notNullValue()));
	}

	@Test
	public void shouldSetPager() {
		final PagerCommand pager = new PagerCommand();

		testee.setPager(pager);

		assertThat(testee.getPager(), sameInstance(pager));
	}

}
