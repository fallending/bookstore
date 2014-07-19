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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UpdateBookCommandUnitTest {

	private UpdateBookCommand testee;

	@Before
	public void setupInstance() {
		testee = new UpdateBookCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getPager(), is(notNullValue()));
		assertThat(testee.getId(), is(0));
		assertThat(testee.getVersion(), is(0));
		assertThat(testee.getTitle(), is(equalTo("")));
	}

	@Test
	public void shouldSetPager() {
		final PagerCommand pager = new PagerCommand();

		testee.setPager(pager);

		assertThat(testee.getPager(), sameInstance(pager));
	}

	@Test
	public void shouldSetId() {
		final int id = 2;

		testee.setId(id);

		assertThat(testee.getId(), equalTo(id));
	}

	@Test
	public void shouldSetVersion() {
		final int version = 54;

		testee.setVersion(version);

		assertThat(testee.getVersion(), equalTo(version));
	}

	@Test
	public void shouldSetTitle() {
		final String title = "some title";

		testee.setTitle(title);

		assertThat(testee.getTitle(), equalTo(title));
	}

}
