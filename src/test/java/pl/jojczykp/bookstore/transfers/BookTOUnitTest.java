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

package pl.jojczykp.bookstore.transfers;

import com.google.protobuf.ByteString;
import org.junit.Before;
import org.junit.Test;

import static com.google.protobuf.ByteString.copyFrom;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class BookTOUnitTest {

	private static final String TITLE = "Some Book Title";
	private static final String FILE_TYPE = "fileType";
	private static final String CONTENT_TYPE = "some/content-type";
	private static final ByteString CONTENT = copyFrom(new byte[] {8, 7, 3, 2, 3, 4, 9});

	private BookTO testee;

	@Before
	public void createTestee() {
		testee = new BookTO(TITLE, FILE_TYPE, CONTENT_TYPE, CONTENT);
	}

	@Test
	public void shouldBeSetUpByConstructor() {
		assertThat(testee.getTitle(), is(sameInstance(TITLE)));
		assertThat(testee.getFileType(), is(sameInstance(FILE_TYPE)));
		assertThat(testee.getContentType(), is(sameInstance(CONTENT_TYPE)));
		assertThat(testee.getContent(), is(sameInstance(CONTENT)));
	}

	@Test
	public void shouldGetTitle() {
		String title = testee.getTitle();

		assertThat(title, is(sameInstance(TITLE)));
	}

	@Test
	public void shouldGetFileType() {
		String fileType = testee.getFileType();

		assertThat(fileType, is(sameInstance(FILE_TYPE)));
	}

	@Test
	public void shouldGetContentType() {
		String contentType = testee.getContentType();

		assertThat(contentType, is(sameInstance(CONTENT_TYPE)));
	}

	@Test
	public void shouldGetContent() {
		ByteString content = testee.getContent();

		assertThat(content, is(sameInstance(CONTENT)));
	}

}
