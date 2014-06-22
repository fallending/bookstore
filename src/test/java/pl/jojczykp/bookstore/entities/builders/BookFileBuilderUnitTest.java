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

package pl.jojczykp.bookstore.entities.builders;

import org.junit.Test;
import pl.jojczykp.bookstore.entities.BookFile;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;

public class BookFileBuilderUnitTest {

	private static final int ID = 7;
	private static final String FILE_TYPE = "aFileType";
	private static final String CONTENT_TYPE = "some-content/type";
	private static final byte[] CONTENT = {7, 5, 3, 0, 1};

	@Test
	public void shouldBuildWithId() {
		BookFile bookFile = aBookFile()
								.withId(ID)
								.build();

		assertThat(bookFile.getId(), is(ID));
	}

	@Test
	public void shouldBuildWithFileType() {
		BookFile bookFile = aBookFile()
								.withFileType(FILE_TYPE)
								.build();

		assertThat(bookFile.getFileType(), is(equalTo(FILE_TYPE)));
	}

	@Test
	public void shouldBuildWithContentType() {
		BookFile bookFile = aBookFile()
								.withContentType(CONTENT_TYPE)
								.build();

		assertThat(bookFile.getContentType(), is(equalTo(CONTENT_TYPE)));
	}

	@Test
	public void shouldBuildWithContentAndContentLength() {
		BookFile bookFile = aBookFile()
								.withContent(CONTENT)
								.build();

		assertThat(blobBytes(bookFile.getContent()), is(equalTo(CONTENT)));
		assertThat(bookFile.getContentLength(), is(CONTENT.length));
	}

}
