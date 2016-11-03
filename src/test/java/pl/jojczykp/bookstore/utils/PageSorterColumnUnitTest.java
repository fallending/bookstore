package pl.jojczykp.bookstore.utils;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;

public class PageSorterColumnUnitTest {

	@Test
	public void shouldHavePageSorterColumnForTitle() {
		PageSorterColumn column = BOOK_TITLE;

		assertThat(column.getNameForQuery(), is(equalTo("title")));
		assertThat(column.isIgnoreCase(), is(true));
	}

	@Test
	public void shouldHaveOnlyGivenEnumValues() {
		assertThat(asList(PageSorterColumn.values()), containsInAnyOrder(BOOK_TITLE));
	}

	@Test
	public void shouldHaveOtherDefaultEnumMethods() {
		assertThat(PageSorterColumn.valueOf(BOOK_TITLE.toString()), is(equalTo(BOOK_TITLE)));
	}

}
