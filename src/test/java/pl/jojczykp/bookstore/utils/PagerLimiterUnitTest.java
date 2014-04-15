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
import pl.jojczykp.bookstore.commands.PagerCommand;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

public class PagerLimiterUnitTest {

	private static final int PAGE_NUMBER = 5;
	private static final int PAGE_SIZE = 12;
	private static final int PAGES_COUNT = 7;
	private static final int TOTAL_COUNT = PAGE_SIZE * PAGES_COUNT;
	private static final PageSorterColumn SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection SORT_DIRECTION = DESC;

	private PagerLimiter testee;

	@Before
	public void setUpTestee() {
		testee = new PagerLimiter();
	}

	@Test
	public void shouldLimitPageSizeWhenBelowRange() {
		final int givenPageSize = -3;
		final int expectedPageSize = 0;
		final PagerCommand requested = aPagerCommandWith(PAGE_NUMBER, givenPageSize, PAGES_COUNT, TOTAL_COUNT);

		PagerCommand limited = testee.createLimited(requested, TOTAL_COUNT);

		assertThat(limited.getPageSize(), equalTo(expectedPageSize));
	}

	@Test
	public void shouldPopulatePageSizeWhenAboveRange() {
		final int pageSize = TOTAL_COUNT + 2;
		final PagerCommand requested = aPagerCommandWith(PAGE_NUMBER, pageSize, PAGES_COUNT, TOTAL_COUNT);

		PagerCommand limited = testee.createLimited(requested, TOTAL_COUNT);

		assertThat(limited.getPageSize(), equalTo(pageSize));
	}

	@Test
	public void shouldPropagatePageNumberWhenNoLimitationNeeded() {
		final PagerCommand requested = aPagerCommandWith(PAGE_NUMBER, PAGE_SIZE, PAGES_COUNT, TOTAL_COUNT);

		PagerCommand limited = testee.createLimited(requested, TOTAL_COUNT);

		assertThat(limited.getPageNumber(), equalTo(PAGE_NUMBER));
	}

	@Test
	public void shouldLimitPageNumberWhenBelowRange() {
		final int givenPageNumber = 0;
		final int expectedPageNumber = 1;
		final PagerCommand requested = aPagerCommandWith(givenPageNumber, PAGE_SIZE, PAGES_COUNT, TOTAL_COUNT);

		PagerCommand limited = testee.createLimited(requested, TOTAL_COUNT);

		assertThat(limited.getPageNumber(), equalTo(expectedPageNumber));
	}

	@Test
	public void shouldLimitPageNumberWhenAboveRange() {
		final int givenPageNumber = PAGES_COUNT + 1;
		final int expectedPageNumber = PAGES_COUNT;
		final PagerCommand requested = aPagerCommandWith(givenPageNumber, PAGE_SIZE, PAGES_COUNT, TOTAL_COUNT);

		PagerCommand limited = testee.createLimited(requested, TOTAL_COUNT);

		assertThat(limited.getPageNumber(), equalTo(expectedPageNumber));
	}

	@Test
	public void shouldComputePagesCountWhenLastPageIsFull() {
		final int pagesCount = 12;
		final int pageSize = 7;
		final int totalCount = pagesCount * pageSize;
		final PagerCommand requested = aPagerCommandWith(PAGE_NUMBER, pageSize, pagesCount, totalCount);

		PagerCommand limited = testee.createLimited(requested, totalCount);

		assertThat(limited.getPagesCount(), equalTo(pagesCount));
	}

	@Test
	public void shouldComputePagesCountWhenLastPageIsNotFull() {
		final int fullPagesCount = 8;
		final int pageSize = 14;
		final int totalCount = fullPagesCount * pageSize + 1;
		final PagerCommand requested = aPagerCommandWith(PAGE_NUMBER, pageSize, PAGES_COUNT, totalCount);

		PagerCommand limited = testee.createLimited(requested, totalCount);

		assertThat(limited.getPagesCount(), equalTo(fullPagesCount + 1));
	}

	@Test
	public void shouldOverrideExistingTotalCountWithGiven() {
		final int givenTotalCount = TOTAL_COUNT + 1;
		final PagerCommand requested = aPagerCommandWith(PAGE_NUMBER, PAGE_SIZE, PAGES_COUNT, TOTAL_COUNT);

		PagerCommand limited = testee.createLimited(requested, givenTotalCount);

		assertThat(limited.getTotalCount(), equalTo(givenTotalCount));
	}

	@Test
	public void shouldPropagateSortColumn() {
		final PageSorterColumn sortColumn = BOOK_TITLE;
		final PagerCommand requested = aPagerCommandWith(sortColumn, SORT_DIRECTION);

		PagerCommand limited = testee.createLimited(requested, TOTAL_COUNT);

		assertThat(limited.getSorter().getColumn(), is(sortColumn));
	}

	@Test
	public void shouldPropagateSortDirection() {
		final PageSorterDirection sortDirection = DESC;
		final PagerCommand requested = aPagerCommandWith(SORT_COLUMN, sortDirection);

		PagerCommand limited = testee.createLimited(requested, TOTAL_COUNT);

		assertThat(limited.getSorter().getDirection(), is(sortDirection));
	}

	private PagerCommand aPagerCommandWith(int pageNumber, int pageSize, int pagesCount, int totalCount) {
		PagerCommand requested = new PagerCommand();
		requested.setTotalCount(totalCount);
		requested.setPagesCount(pagesCount);
		requested.setPageSize(pageSize);
		requested.setPageNumber(pageNumber);

		return requested;
	}

	private PagerCommand aPagerCommandWith(PageSorterColumn sortColumn, PageSorterDirection sorterDirection) {
		PagerCommand requested = new PagerCommand();
		requested.setPageSize(PAGE_SIZE);
		requested.getSorter().setColumn(sortColumn);
		requested.getSorter().setDirection(sorterDirection);

		return requested;
	}

}
