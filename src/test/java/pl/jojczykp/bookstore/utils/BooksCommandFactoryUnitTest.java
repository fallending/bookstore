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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.jojczykp.bookstore.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.jojczykp.bookstore.commands.BooksCommand;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/books-command-factory-test-context.xml")
public class BooksCommandFactoryUnitTest {

	@Value("${view.books.defaultPageNumber}") private int defaultPageNumber;
	@Value("${view.books.defaultPageSize}") private int defaultPageSize;
	@Value("${view.books.defaultSortColumn}") private PageSorterColumn defaultSortColumn;
	@Value("${view.books.defaultSortDirection}") private PageSorterDirection defaultSortDirection;

	@Autowired private BooksCommandFactory testee;

	@Test
	public void shouldCreateDefaultCommandWithDefaultPageNumber() {
		BooksCommand command = testee.create();

		assertThat(command.getPager().getPageNumber(), is(defaultPageNumber));
	}

	@Test
	public void shouldCreateDefaultCommandWithDefaultPageSize() {
		BooksCommand command = testee.create();

		assertThat(command.getPager().getPageSize(), is(defaultPageSize));
	}

	@Test
	public void shouldCreateDefaultCommandWithDefaultSortColumn() {
		BooksCommand command = testee.create();

		assertThat(command.getPager().getSorter().getColumn(), is(defaultSortColumn));
	}

	@Test
	public void shouldCreateDefaultCommandWithDefaultSortDirection() {
		BooksCommand command = testee.create();

		assertThat(command.getPager().getSorter().getDirection(), is(defaultSortDirection));
	}

}
