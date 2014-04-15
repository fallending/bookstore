/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

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
