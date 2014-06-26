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

package pl.jojczykp.bookstore.testutils.matchers;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.entities.BookFile;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookBuilder.aBook;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;
import static pl.jojczykp.bookstore.testutils.matchers.IsBookEqualTo.isBookEqualTo;


public class IsBookEqualToTest {
	private Book book1;
	private Book book2;

	@Before
	public void setupData() {
		BookFile bookFile1 = aBookFile().withId(1).withContentType("SomeType").build();
		BookFile bookFile2 = aBookFile().withId(1).withContentType("OtherType").build();

		book1 = aBook().withId(1).withVersion(2).withTitle("Title").withBookFile(bookFile1).build();
		book2 = aBook().withId(1).withVersion(2).withTitle("Title").withBookFile(bookFile2).build();
	}

	@Test
	public void shouldMatchWithNotNullBookFiles() {
		assertThat(book1, isBookEqualTo(book2));
	}

	@Test
	public void shouldMatchWithNullBookFiles() {
		book1.setBookFile(null);
		book2.setBookFile(null);

		assertThat(book1, isBookEqualTo(book2));
	}

	@Test
	public void shouldNotMatchWhenDifferentId() {
		book2.setId(0);
		assertThat(book1, not(isBookEqualTo(book2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentVersion() {
		book2.setVersion(0);
		assertThat(book1, not(isBookEqualTo(book2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentTitle() {
		book2.setTitle("Aaother Title");
		assertThat(book1, not(isBookEqualTo(book2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentBookFiles() {
		book2.getBookFile().setId(0);
		assertThat(book1, not(isBookEqualTo(book2)));
	}

}
