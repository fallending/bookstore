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
import pl.jojczykp.bookstore.entities.BookFile;
import pl.jojczykp.bookstore.utils.BlobUtils;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;
import static pl.jojczykp.bookstore.testutils.matchers.IsBookFileEqualTo.isBookFileEqualTo;


public class IsBookFileEqualToTest {

	private static final byte[] CONTENT = new byte[] {1, 2, 3, 4};
	private static final byte[] OTHER_CONTENT = new byte[] {5, 6, 7, 8};

	private BookFile bookFile1;
	private BookFile bookFile2;

	@Before
	public void setupData() {
		bookFile1 = aBookFile().withId(1).withFileType("FType").withContentType("CType").withContent(CONTENT).build();
		bookFile2 = aBookFile().withId(1).withFileType("FType").withContentType("CType").withContent(CONTENT).build();
	}

	@Test
	public void shouldMatch() {
		assertThat(bookFile1, isBookFileEqualTo(bookFile2));
	}

	@Test
	public void shouldNotMatchWhenDifferentId() {
		bookFile2.setId(0);
		assertThat(bookFile1, not(isBookFileEqualTo(bookFile2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentFileType() {
		bookFile2.setFileType("OtherFileType");
		assertThat(bookFile1, not(isBookFileEqualTo(bookFile2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentContentType() {
		bookFile2.setFileType("OtherContentType");
		assertThat(bookFile1, not(isBookFileEqualTo(bookFile2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentContent() {
		bookFile2.setContent(BlobUtils.aSerialBlobWith(OTHER_CONTENT));
		assertThat(bookFile1, not(isBookFileEqualTo(bookFile2)));
	}

}
