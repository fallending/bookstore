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
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;

public class UpdateBookAssemblerUnitTest {

	private static final int ID = 67;
	private static final int VERSION = 76;
	private static final String TITLE = "A Title";

	private UpdateBookAssembler testee;

	@Before
	public void setUpTestee() {
		testee = new UpdateBookAssembler();
	}

	@Test
	public void shouldAssemblySingleBookDomainObjectFromUpdateBookCommandObject() {
		UpdateBookCommand command = anUpdateBookCommand();

		Book domain = testee.toDomain(command);

		assertThatHaveEqualData(domain, command);
	}

	private UpdateBookCommand anUpdateBookCommand() {
		UpdateBookCommand command = new UpdateBookCommand();
		command.setId(ID);
		command.setVersion(VERSION);
		command.setTitle(TITLE);

		return command;
	}

	private void assertThatHaveEqualData(Book domain, UpdateBookCommand command) {
		assertThat(domain.getId(), equalTo(command.getId()));
		assertThat(domain.getVersion(), equalTo(command.getVersion()));
		assertThat(domain.getTitle(), equalTo(command.getTitle()));
		assertThat(domain.getBookFile().getContentType(), is(equalTo("text/plain; charset=utf-8")));
		assertThat(blobBytes(domain.getBookFile().getContent()), is(equalTo(copyFromUtf8("a Book Content"))));
	}

}
