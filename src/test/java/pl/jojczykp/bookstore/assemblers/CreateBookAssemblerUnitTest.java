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

package pl.jojczykp.bookstore.assemblers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;

public class CreateBookAssemblerUnitTest {

	private static final int ID_TO_BE_SET_AUTOMATICALLY = 0;
	private static final int VERSION_TO_BE_SET_AUTOMATICALLY = 0;

	private static final String TITLE = "A Title";
	private static final String FILE_TYPE = "fileType";
	private static final String CONTENT_TYPE = "application/octet-stream";
	private static final byte[] CONTENT = {1, 2, 3};

	private CreateBookAssembler testee;

	@Before
	public void setUpTestee() {
		testee = new CreateBookAssembler();
	}

	@Test
	public void shouldAssemblySingleBookDomainObjectFromCreateBookCommandObject() {
		CreateBookCommand command = aCreateBookCommand(TITLE, aMultiPartFile(FILE_TYPE, CONTENT_TYPE, CONTENT));

		Book domain = testee.toDomain(command);

		assertThat(domain.getId(), is(ID_TO_BE_SET_AUTOMATICALLY));
		assertThat(domain.getVersion(), is(VERSION_TO_BE_SET_AUTOMATICALLY));
		assertThat(domain.getTitle(), is(equalTo(TITLE)));
		assertThat(domain.getBookFile().getFileType(), is(equalTo(FILE_TYPE)));
		assertThat(domain.getBookFile().getContentType(), is(equalTo(CONTENT_TYPE)));
		assertThat(blobBytes(domain.getBookFile().getContent()), is(equalTo(CONTENT)));
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowExceptionWhenRead() {
		CreateBookCommand command = aCreateBookCommand(TITLE, aFileThrowingExceptionWhenRead());

		Book domain = testee.toDomain(command);

		assertThat(domain.getId(), is(ID_TO_BE_SET_AUTOMATICALLY));
		assertThat(domain.getVersion(), is(VERSION_TO_BE_SET_AUTOMATICALLY));
		assertThat(domain.getTitle(), is(equalTo(TITLE)));
		assertThat(domain.getBookFile().getFileType(), is(equalTo(FILE_TYPE)));
		assertThat(domain.getBookFile().getContentType(), is(equalTo(CONTENT_TYPE)));
		assertThat(blobBytes(domain.getBookFile().getContent()), is(equalTo(CONTENT)));
	}

	private CreateBookCommand aCreateBookCommand(String title, MockMultipartFile file) {
		CreateBookCommand command = new CreateBookCommand();
		command.setTitle(title);
		command.setFile(file);

		return command;
	}

	private MockMultipartFile aMultiPartFile(String fileType, String contentType, byte[] content) {
		return new MockMultipartFile("name", "baseName." + fileType, contentType, content);
	}

	private MockMultipartFile aFileThrowingExceptionWhenRead() {
		return new MockMultipartFile("name", "fileExt", "contentType", "content".getBytes()) {
			@Override
			public byte[] getBytes() throws IOException {
				throw new IOException("Dummy " + getClass().getName());
			}
		};
	}

}
