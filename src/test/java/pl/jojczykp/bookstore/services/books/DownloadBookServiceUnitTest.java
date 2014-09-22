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

package pl.jojczykp.bookstore.services.books;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jojczykp.bookstore.commands.books.DownloadBookCommand;
import pl.jojczykp.bookstore.controllers.errors.ResourceNotFoundException;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.entities.BookFile;
import pl.jojczykp.bookstore.repositories.BooksRepository;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static pl.jojczykp.bookstore.utils.BlobUtils.aSerialBlobWith;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;

@RunWith(MockitoJUnitRunner.class)
public class DownloadBookServiceUnitTest {

	private static final String EXISTING_ID = "7";
	private static final String NOT_EXISTING_ID = "13";
	private static final String NOT_PARSABLE_ID = "someString";
	private static final byte[] CONTENT = {1, 2, 3, 4, 5};
	private static final String EXCEPTION_MESSAGE = "Content of book with id '%s' not found.";

	@Rule public ExpectedException expectedException = ExpectedException.none();

	@Mock private BooksRepository booksRepository;

	@InjectMocks private DownloadBookService testee;

	@Before
	public void setUp() {
		reset(booksRepository);
	}

	@Test
	public void shouldDownloadBook() {
		DownloadBookCommand command = downloadBookCommandWith(EXISTING_ID);
		givenBookReadFromRepositoryWith(EXISTING_ID);

		Book book = testee.download(command);

		assertThat(blobBytes(book.getBookFile().getContent()), is(equalTo(CONTENT)));
	}

	@Test
	public void shouldFailDownloadingNotExistingBook() {
		DownloadBookCommand command = downloadBookCommandWith(NOT_EXISTING_ID);
		givenBookReadFromRepositoryWith(EXISTING_ID);

		expectedException.expect(ResourceNotFoundException.class);
		expectedException.expectMessage(equalTo(format(EXCEPTION_MESSAGE, NOT_EXISTING_ID)));

		testee.download(command);
	}

	@Test
	public void shouldFailDownloadingBookForNotParsableId() {
		DownloadBookCommand command = downloadBookCommandWith(NOT_PARSABLE_ID);

		expectedException.expect(ResourceNotFoundException.class);
		expectedException.expectMessage(equalTo(format(EXCEPTION_MESSAGE, NOT_PARSABLE_ID)));
		expectedException.expectCause(Matchers.<Throwable>instanceOf(NumberFormatException.class));

		testee.download(command);
	}

	private DownloadBookCommand downloadBookCommandWith(String id) {
		DownloadBookCommand command = new DownloadBookCommand();
		command.setId(id);

		return command;
	}

	private void givenBookReadFromRepositoryWith(String id) {
		Book book = mock(Book.class);
		BookFile bookFile = mock(BookFile.class);

		given(booksRepository.find(parseInt(id))).willReturn(book);
		given(book.getBookFile()).willReturn(bookFile);
		given(bookFile.getContent()).willReturn(aSerialBlobWith(CONTENT));
	}

}
