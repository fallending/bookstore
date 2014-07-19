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
