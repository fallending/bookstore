package pl.jojczykp.bookstore.command;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.utils.PageParams;
import pl.jojczykp.bookstore.utils.PageSorter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class PagerCommandTest {

	private PagerCommand testee;

	@Before
	public void setupInstance() {
		testee = new PagerCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getCurrent(), notNullValue());
		assertThat(testee.getLimited(), notNullValue());
		assertThat(testee.getSorter(), notNullValue());
		assertThat(testee.getTotalCount(), equalTo(0));
	}

	@Test
	public void shouldSetCurrent() {
		final PageParams pageParams = new PageParams();

		testee.setCurrent(pageParams);

		assertThat(testee.getCurrent(), sameInstance(pageParams));
	}

	@Test
	public void shouldSetLimited() {
		final PageParams pageParams = new PageParams();

		testee.setLimited(pageParams);

		assertThat(testee.getLimited(), sameInstance(pageParams));
	}

	@Test
	public void shouldSetSorter() {
		final PageSorter pageSorter = new PageSorter();

		testee.setSorter(pageSorter);

		assertThat(testee.getSorter(), sameInstance(pageSorter));
	}

	@Test
	public void shouldSetTotalCount() {
		final int totalCount = 3;

		testee.setTotalCount(totalCount);

		assertThat(testee.getTotalCount(), equalTo(totalCount));
	}

}
