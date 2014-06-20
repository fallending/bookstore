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

import java.sql.Blob;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.jojczykp.bookstore.testutils.builders.BookFileBuilder.aBookFile;
import static pl.jojczykp.bookstore.utils.BlobUtils.aSerialBlobWith;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobLength;

public class BookFileUnitTest {

	private static final int ID = 3;
	private static final String FILE_TYPE = "fileType";
	private static final String CONTENT_TYPE = "content/type";
	private static final Blob CONTENT = aSerialBlobWith(new byte[] {2, 4, 6, 8});

	private BookFile testee = new BookFile();

	@Before
	public void setupTestee() {
		testee = new BookFile();
	}

	@Test
	public void shouldHaveDefaultConstructorForHibernate() {
		assertThat(testee.getId(), is(0));
		assertThat(testee.getFileType(), is(equalTo("")));
		assertThat(testee.getContentType(), is(equalTo("")));
		assertThat(blobLength(testee.getContent()), is(0L));
	}

	@Test
	public void shouldHaveConstructorWithContentTypeAndContent() {
		BookFile initializedTestee = new BookFile(FILE_TYPE, CONTENT_TYPE, CONTENT);

		assertThat(initializedTestee.getId(), is(0));
		assertThat(initializedTestee.getFileType(), is(equalTo(FILE_TYPE)));
		assertThat(initializedTestee.getContentType(), is(equalTo(CONTENT_TYPE)));
		assertThat(blobBytes(initializedTestee.getContent()), is(equalTo(blobBytes(CONTENT))));
	}

	@Test
	public void shouldSetId() {
		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetFileType() {
		testee.setFileType(FILE_TYPE);

		assertThat(testee.getFileType(), is(FILE_TYPE));
	}

	@Test
	public void shouldSetContentType() {
		testee.setContentType(CONTENT_TYPE);

		assertThat(testee.getContentType(), is(CONTENT_TYPE));
	}

	@Test
	public void shouldSetContent() {
		testee.setContent(CONTENT);

		assertThat(testee.getContent(), is(CONTENT));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(BookFile.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetails() {
		BookFile testeeWithContent = aBookFile(ID, FILE_TYPE, CONTENT_TYPE, CONTENT);

		String toStringResult = testeeWithContent.toString();

		assertThat(toStringResult, equalTo(format("%s{id=%d, fileType='%s', contentType='%s'}",
						testeeWithContent.getClass().getSimpleName(), ID, FILE_TYPE, CONTENT_TYPE)));
	}

}
