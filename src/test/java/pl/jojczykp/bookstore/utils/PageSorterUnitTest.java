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
