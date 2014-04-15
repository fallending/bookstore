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

package pl.jojczykp.bookstore.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.jojczykp.bookstore.testutils.builders.BookBuilder.aBook;

public class BookUnitTest {

	private static final int ID = 8;
	private static final int VERSION = 76;
	private static final String TITLE = "some title";

	@Test
	public void shouldHaveDefaultConstructorForHibernate() {
		Book testee = new Book();

		assertThat(testee.getId(), is(0));
		assertThat(testee.getVersion(), is(0));
		assertThat(testee.getTitle(), is(""));
	}

	@Test
	public void shouldSetId() {
		Book testee = new Book();

		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetVersion() {
		Book testee = new Book();

		testee.setVersion(VERSION);

		assertThat(testee.getVersion(), is(VERSION));
	}

	@Test
	public void shouldSetTitle() {
		Book testee = new Book();

		testee.setTitle(TITLE);

		assertThat(testee.getTitle(), is(TITLE));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(Book.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetails() {
		Book testee = aBook(ID, VERSION, TITLE);

		String toStringResult = testee.toString();

		assertThat(toStringResult, equalTo(
				format("%s{id=%d, version=%d, title='%s'}",
						testee.getClass().getSimpleName(), ID, VERSION, TITLE)));
	}

}
