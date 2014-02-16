package pl.jojczykp.bookstore.command;

import org.junit.Before;
import org.junit.Test;
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
		assertThat(testee.getPageNumber(), equalTo(0));
		assertThat(testee.getPageSize(), equalTo(0));
		assertThat(testee.getPagesCount(), equalTo(0));
		assertThat(testee.getTotalCount(), equalTo(0));
		assertThat(testee.getSorter(), notNullValue());
	}

	@Test
	public void shouldSetPageNumber() {
		final int pageNumber = 3;

		testee.setPageNumber(pageNumber);

		assertThat(testee.getPageNumber(), equalTo(pageNumber));
	}

	@Test
	public void shouldSetPageSize() {
		final int pageSize = 4;

		testee.setPageSize(pageSize);

		assertThat(testee.getPageSize(), equalTo(pageSize));
	}

	@Test
	public void shouldSetPagesCount() {
		final int pagesCount = 5;

		testee.setPagesCount(pagesCount);

		assertThat(testee.getPagesCount(), equalTo(pagesCount));
	}

	@Test
	public void shouldSetTotalCount() {
		final int totalCount = 5;

		testee.setTotalCount(totalCount);

		assertThat(testee.getTotalCount(), equalTo(totalCount));
	}

	@Test
	public void shouldSetSorter() {
		final PageSorter pageSorter = new PageSorter();

		testee.setSorter(pageSorter);

		assertThat(testee.getSorter(), sameInstance(pageSorter));
	}

}
