package pl.jojczykp.bookstore.commands.books;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.commands.common.PagerCommand;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DeleteBooksCommandUnitTest {

	private DeleteBooksCommand testee;

	@Before
	public void setupInstance() {
		testee = new DeleteBooksCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getPager(), is(notNullValue()));
		assertThat(testee.getIds(), is(empty()));
	}

	@Test
	public void shouldSetPager() {
		final PagerCommand pager = new PagerCommand();

		testee.setPager(pager);

		assertThat(testee.getPager(), sameInstance(pager));
	}

	@Test
	public void shouldSetIds() {
		final List<Integer> ids = asList(1, 2, 3, 4, 5);

		testee.setIds(ids);

		assertThat(testee.getIds(), is(sameInstance(ids)));
	}

}
