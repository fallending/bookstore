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

package pl.jojczykp.bookstore.repositories;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.StaleObjectStateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.entities.BookFile;
import pl.jojczykp.bookstore.testutils.repositories.BooksRepositorySpy;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static pl.jojczykp.bookstore.entities.builders.BookBuilder.aBook;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;
import static pl.jojczykp.bookstore.testutils.repositories.BooksRepositorySpy.ID_TO_GENERATE;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/repositories-test-context.xml")
@Transactional
public class BooksRepositoryIntegrationTest {

	private static final int OLD_VERSION = 3;
	private static final String OLD_TITLE = "Old Title";
	private static final String NEW_TITLE = "New Title";

	private static final PageSorterColumn SAMPLE_SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection SAMPLE_DIRECTION = ASC;

	private Book bookA = aBook().withId(ID_TO_GENERATE).withVersion(1).withTitle("Book Title A")
													.withBookFile(aBookFile().withId(ID_TO_GENERATE).build()).build();
	private Book bookB = aBook().withId(ID_TO_GENERATE).withVersion(2).withTitle("Book Title B")
													.withBookFile(aBookFile().withId(ID_TO_GENERATE).build()).build();
	private Book bookC = aBook().withId(ID_TO_GENERATE).withVersion(1).withTitle("Book Title C")
													.withBookFile(aBookFile().withId(ID_TO_GENERATE).build()).build();
	private Book bookD = aBook().withId(ID_TO_GENERATE).withVersion(2).withTitle("Book Title D")
													.withBookFile(aBookFile().withId(ID_TO_GENERATE).build()).build();
	private Book bookE = aBook().withId(ID_TO_GENERATE).withVersion(1).withTitle("Book Title E")
													.withBookFile(aBookFile().withId(ID_TO_GENERATE).build()).build();
	private Book bookLowCaseC = aBook().withId(ID_TO_GENERATE).withVersion(2).withTitle("Book Title c")
													.withBookFile(aBookFile().withId(ID_TO_GENERATE).build()).build();

	@Autowired private BooksRepositorySpy booksRepositorySpy;
	@Autowired private BooksRepository testee;

	@Test
	public void shouldComputeTotalCountOfBooks() {
		final Book[] givenBooks = {bookA, bookB, bookC, bookD, bookE};
		givenRepositoryWith(givenBooks);

		assertThat(testee.totalCount(), is(givenBooks.length));
	}

	@Test
	public void shouldReadBooksByOffsetAndSize() {
		final int offset = 1;
		final int size = 3;
		givenRepositoryWith(bookA, bookB, bookC, bookD, bookE);

		List<Book> readBooks = testee.read(offset, size, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatListContainsOnly(readBooks, bookB, bookC, bookD);
	}

	@Test
	public void shouldReadBooksWhenOffsetInRangeAndSizeOutOfRange() {
		final Book[] givenBooks = {bookA, bookB, bookC, bookD, bookE};
		givenRepositoryWith(givenBooks);

		List<Book> readBooks = testee.read(2, givenBooks.length + 1, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatListContainsOnly(readBooks, bookC, bookD, bookE);
	}

	@Test
	public void shouldReadBooksFromOffsetZeroWhenGivenNegativeOffset() {
		final int negativeOffset = -2;
		final Book[] givenBooks = {bookA, bookB, bookC};
		givenRepositoryWith(givenBooks);

		List<Book> readBooks = testee.read(negativeOffset, 2, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatListContainsOnly(readBooks, bookA, bookB);
	}

	@Test
	public void shouldReadEmptyBooksListWhenGivenOutOfRangeOffset() {
		final Book[] givenBooks = {bookA, bookB, bookC, bookD, bookE};
		final int outOfRangeOffset = givenBooks.length + 2;
		final int anySize = 8;
		givenRepositoryWith(givenBooks);

		List<Book> readBooks = testee.read(outOfRangeOffset, anySize, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(readBooks.size(), is(0));
	}

	@Test
	public void shouldReadEmptyBooksListWhenGivenNegativeSize() {
		final int anyOffset = 2;
		final int negativeSize = -1;
		givenRepositoryWith(bookA, bookB);

		List<Book> readBooks = testee.read(anyOffset, negativeSize, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(readBooks.size(), is(0));
	}

	@Test
	public void shouldReadEmptyBooksListWhenGivenZeroSize() {
		final int anyOffset = 7;
		givenRepositoryWith(bookA, bookB);

		List<Book> readBooks = testee.read(anyOffset, 0, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(readBooks.size(), is(0));
	}

	@Test
	public void shouldReadBooksOrderingAsc() throws NoSuchFieldException {
		final Book[] givenBooks = {bookB, bookA, bookC};
		givenRepositoryWith(givenBooks);
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);

		List<Book> readBooks = testee.read(0, givenBooks.length, SAMPLE_SORT_COLUMN, ASC);

		assertThatListContainsOnly(readBooks, bookA, bookB, bookC);
	}

	@Test
	public void shouldReadBooksOrderingDesc() throws NoSuchFieldException {
		final Book[] givenBooks = {bookB, bookA, bookC};
		givenRepositoryWith(givenBooks);
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);

		List<Book> readBooks = testee.read(0, givenBooks.length, SAMPLE_SORT_COLUMN, DESC);

		assertThatListContainsOnly(readBooks, bookC, bookB, bookA);
	}

	@Test
	public void shouldReadBooksOrderingCaseInsensitively() throws NoSuchFieldException {
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);
		givenRepositoryWith(bookLowCaseC, bookA);

		List<Book> readBooks = testee.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThatListContainsOnly(readBooks, bookA, bookLowCaseC);
	}

	@Test
	public void shouldReadBooksOrderingCaseSensitively() throws NoSuchFieldException {
		givenIgnoreCaseWhileSort(BOOK_TITLE, false);
		givenRepositoryWith(bookLowCaseC, bookD);

		List<Book> readBooks = testee.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThatListContainsOnly(readBooks, bookD, bookLowCaseC);
	}

	@Test
	public void shouldCreateBook() {
		testee.create(bookC);

		assertThatRepositoryContainsOnly(bookC);
	}

	@Test
	public void shouldUpdateBookTitle() {
		Book oldBook = aBook().withId(ID_TO_GENERATE).withVersion(OLD_VERSION).withTitle(OLD_TITLE)
						.withBookFile(aBookFile().withId(ID_TO_GENERATE).build()).build();
		givenRepositoryWith(oldBook);
		Book updatingBook = aBook().withId(oldBook.getId()).withVersion(OLD_VERSION).withTitle(NEW_TITLE)
																		.withBookFile(oldBook.getBookFile()).build();
		Book updatedBook = aBook().withId(oldBook.getId()).withVersion(OLD_VERSION + 1).withTitle(NEW_TITLE)
																		.withBookFile(oldBook.getBookFile()).build();

		testee.update(updatingBook);

		assertThatRepositoryContainsOnly(updatedBook);
	}

	@Test(expected = StaleObjectStateException.class)
	public void shouldFailUpdatingBookWhenModifiedByOtherSession() {
		Book oldBook = aBook().withId(ID_TO_GENERATE).withVersion(OLD_VERSION).withTitle(OLD_TITLE)
						.withBookFile(aBookFile().withId(ID_TO_GENERATE).build())
						.build();

		givenRepositoryWith(oldBook);
		Book updatingBook = aBook().withId(oldBook.getId()).withVersion(OLD_VERSION - 1).withTitle(NEW_TITLE)
																		.withBookFile(oldBook.getBookFile()).build();

		testee.update(updatingBook);
	}

	@Test
	public void shouldDeleteBook() {
		givenRepositoryWith(bookA, bookB, bookC);

		testee.delete(bookB.getId());

		assertThatRepositoryContainsOnly(bookA, bookC);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void shouldFailDeletingNotExistingBook() {
		givenRepositoryWith(bookA, bookB);
		int notExistingId = bookA.getId() + bookB.getId();

		testee.delete(notExistingId);
	}

	@Test
	public void shouldFindBookById() {
		givenRepositoryWith(bookB, bookC);

		Book foundBook = testee.find(bookB.getId());

		assertThat(foundBook, samePropertyValuesAs(bookB));
		assertEquals(foundBook.getBookFile(), bookB.getBookFile());
	}

	@Test
	public void shouldFailFindingNotExistingBookById() {
		givenRepositoryWith(bookB, bookC);
		int notExistingId = bookB.getId() + bookC.getId();

		Book foundBook = testee.find(notExistingId);

		assertThat(foundBook, is(nullValue()));
	}

	private void givenIgnoreCaseWhileSort(PageSorterColumn column, boolean value) throws NoSuchFieldException {
		Field ignoreCaseField = column.getClass().getDeclaredField("ignoreCase");
		ReflectionUtils.makeAccessible(ignoreCaseField);
		ReflectionUtils.setField(ignoreCaseField, column, value);
	}

	private void givenRepositoryWith(Book... books) {
		booksRepositorySpy.givenRepositoryWith((Object[]) books);
	}

	private void assertThatRepositoryContainsOnly(Book... books) {
		assertThatListContainsOnly(booksRepositorySpy.getAllBooks(), books);
		assertThatListContainsOnly(booksRepositorySpy.getAllBookFiles(), bookFilesOf(books));
	}

	private BookFile[] bookFilesOf(Book[] books) {
		BookFile[] result = new BookFile[books.length];
		for (int i = 0; i < books.length; i++) {
			result[i] = books[i].getBookFile();
		}

		return result;
	}

	private void assertThatListContainsOnly(List<Book> givens, Book... expecteds) {
		assertThat(expecteds.length, is(givens.size()));
		for (int i = 0; i < givens.size(); i++) {
			Book given = givens.get(i);
			Book expected = expecteds[i];
			assertThat(given, samePropertyValuesAs(expected));
		}
	}

	private void assertThatListContainsOnly(List<BookFile> givens, BookFile... expecteds) {
		assertThat(expecteds.length, is(givens.size()));
		for (int i = 0; i < givens.size(); i++) {
			BookFile given = givens.get(i);
			BookFile expected = expecteds[i];
			assertEquals(given, expected);
		}
	}

	private void assertEquals(BookFile given, BookFile expected) {
		assertThat(given.getId(), is(equalTo(expected.getId())));
		assertThat(given.getFileType(), is(equalTo(expected.getFileType())));
		assertThat(given.getContentType(), is(equalTo(expected.getContentType())));
		assertThat(blobBytes(given.getContent()), is(equalTo(blobBytes(expected.getContent()))));
	}

}
