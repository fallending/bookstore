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

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;
import static pl.jojczykp.bookstore.utils.BlobUtils.aSerialBlobWith;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobLength;

public class BookFileUnitTest {

	private static final int ID_TO_BE_GENERATED = 0;

	private static final int ID = 3;
	private static final String FILE_TYPE = "fileType";
	private static final String CONTENT_TYPE = "content/type";
	private static final byte[] CONTENT = {2, 4, 6, 8};

	private BookFile testee = new BookFile();

	@Before
	public void setupTestee() {
		testee = new BookFile();
	}

	@Test
	public void shouldHaveDefaultConstructorForHibernate() {
		assertThat(testee.getId(), is(ID_TO_BE_GENERATED));
		assertThat(testee.getFileType(), is(equalTo("")));
		assertThat(testee.getContentType(), is(equalTo("")));
		assertThat(blobLength(testee.getContent()), is(0L));
	}

	@Test
	public void shouldSetId() {
		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetFileType() {
		testee.setFileType(FILE_TYPE);

		assertThat(testee.getFileType(), is(equalTo(FILE_TYPE)));
	}

	@Test
	public void shouldSetContentType() {
		testee.setContentType(CONTENT_TYPE);

		assertThat(testee.getContentType(), is(equalTo(CONTENT_TYPE)));
	}

	@Test
	public void shouldSetContentLength() {
		testee.setContentLength(CONTENT.length);

		assertThat(testee.getContentLength(), is(CONTENT.length));
	}

	@Test
	public void shouldSetContent() {
		testee.setContent(aSerialBlobWith(CONTENT));

		assertThat(blobBytes(testee.getContent()), is(equalTo(CONTENT)));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(BookFile.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetails() {
		BookFile testeeWithContent = aBookFile()
										.withId(ID)
										.withFileType(FILE_TYPE)
										.withContentType(CONTENT_TYPE)
										.withContent(CONTENT)
										.build();

		String toStringResult = testeeWithContent.toString();

		assertThat(toStringResult, equalTo(format("%s{id=%d, fileType='%s', contentType='%s', contentLength='%d'}",
						testeeWithContent.getClass().getSimpleName(), ID, FILE_TYPE, CONTENT_TYPE, CONTENT.length)));
	}

}
