package pl.jojczykp.bookstore.utils;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

public class PageSorterDirectionUnitTest {

	@Test
	public void shouldHaveOnlyGivenEnumValues() {
		assertThat(asList(PageSorterDirection.values()), containsInAnyOrder(ASC, DESC));
	}

	@Test
	public void shouldHaveOtherDefaultEnumMethods() {
		assertThat(PageSorterDirection.valueOf(ASC.toString()), is(equalTo(ASC)));
	}

}
