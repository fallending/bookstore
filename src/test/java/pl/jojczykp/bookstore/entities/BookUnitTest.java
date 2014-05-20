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

package pl.jojczykp.bookstore.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static pl.jojczykp.bookstore.testutils.builders.BookBuilder.aBook;

@RunWith(MockitoJUnitRunner.class)
public class BookUnitTest {

	private static final int ID = 8;
	private static final int VERSION = 76;
	private static final String TITLE = "some title";

	@Mock private BookFile bookFile;
	private static final String BOOK_FILE_AS_STRING = "bookFile as String";

	private Book testee = new Book();

	@Before
	public void setupTestee() {
		testee = new Book();
	}

	@Before
	public void setupMock() {
		given(bookFile.toString()).willReturn(BOOK_FILE_AS_STRING);
	}

	@Test
	public void shouldHaveDefaultConstructorForHibernate() {
		assertThat(testee.getId(), is(0));
		assertThat(testee.getVersion(), is(0));
		assertThat(testee.getTitle(), is(""));
		assertThat(testee.getBookFile(), is(nullValue()));
	}

	@Test
	public void shouldSetId() {
		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetVersion() {
		testee.setVersion(VERSION);

		assertThat(testee.getVersion(), is(VERSION));
	}

	@Test
	public void shouldSetTitle() {
		testee.setTitle(TITLE);

		assertThat(testee.getTitle(), is(TITLE));
	}

	@Test
	public void shouldSetBookFile() {
		testee.setBookFile(bookFile);

		assertThat(testee.getBookFile(), is(sameInstance(bookFile)));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(Book.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetails() {
		Book testeeWithContent = aBook(ID, VERSION, TITLE, bookFile);

		String toStringResult = testeeWithContent.toString();

		assertThat(toStringResult, equalTo(
				format("%s{id=%d, version=%d, title='%s', bookFile=" + BOOK_FILE_AS_STRING + "}",
						testeeWithContent.getClass().getSimpleName(), ID, VERSION, TITLE)));
	}

}
