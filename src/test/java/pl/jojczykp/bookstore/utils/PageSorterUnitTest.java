package pl.jojczykp.bookstore.utils;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;

public class PageSorterUnitTest {

	private PageSorter testee;

	@Before
	public void setUpTestee() {
		testee = new PageSorter();
	}

	@Test
	public void shouldHaveDefaultConstructorSettingDefaultValues() {
		assertThat(testee.getColumn(), is(notNullValue()));
		assertThat(testee.getDirection(), is(notNullValue()));
	}

	@Test
	public void shouldSetColumn() {
		final PageSorterColumn givenColumn = BOOK_TITLE;

		testee.setColumn(givenColumn);

		assertThat(testee.getColumn(), equalTo(givenColumn));
	}

	@Test
	public void shouldSetDirection() {
		final PageSorterDirection givenDirection = ASC;

		testee.setDirection(givenDirection);

		assertThat(testee.getDirection(), equalTo(givenDirection));
	}

	@Test
	public void shouldHaveToStringWithDetails() {
		final PageSorterColumn givenColumn = BOOK_TITLE;
		final PageSorterDirection givenDirection = ASC;
		testee.setColumn(givenColumn);
		testee.setDirection(givenDirection);

		String toStringResult = testee.toString();

		assertThat(toStringResult, containsString("column=" + givenColumn.name()));
		assertThat(toStringResult, containsString("direction=" + givenDirection.name()));
	}

}
