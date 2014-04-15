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
import pl.jojczykp.bookstore.testutils.repositories.TestRepository;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static pl.jojczykp.bookstore.testutils.builders.BookBuilder.aBook;
import static pl.jojczykp.bookstore.testutils.repositories.TestRepository.ID_TO_GENERATE;
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

	private Book bookA = aBook(ID_TO_GENERATE, 1, "Book Title A");
	private Book bookB = aBook(ID_TO_GENERATE, 2, "Book Title B");
	private Book bookC = aBook(ID_TO_GENERATE, 1, "Book Title C");
	private Book bookD = aBook(ID_TO_GENERATE, 2, "Book Title D");
	private Book bookE = aBook(ID_TO_GENERATE, 1, "Book Title E");
	private Book bookLowCaseC = aBook(ID_TO_GENERATE, 2, "Book Title c");

	@Autowired private TestRepository testRepository;
	@Autowired private BooksRepository testee;

	@Test
	public void shouldComputeTotalCountOfBooks() {
		final Book[] givenBooks = {bookA, bookB, bookC, bookD, bookE};
		givenRepositoryWith(givenBooks);

		assertThat(testee.totalCount(), is(givenBooks.length));
	}

	@Test
	public void shouldFindBooksByOffsetAndSize() {
		final int offset = 1;
		final int size = 3;
		givenRepositoryWith(bookA, bookB, bookC, bookD, bookE);

		List<Book> foundBooks = testee.read(offset, size, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatCollectionContainsOnly(foundBooks, bookB, bookC, bookD);
	}

	@Test
	public void shouldFindBooksWhenOffsetInRangeAndSizeOutOfRange() {
		final Book[] givenBooks = {bookA, bookB, bookC, bookD, bookE};
		givenRepositoryWith(givenBooks);

		List<Book> foundBooks = testee.read(2, givenBooks.length + 1, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatCollectionContainsOnly(foundBooks, bookC, bookD, bookE);
	}

	@Test
	public void shouldFindBooksFromOffsetZeroWhenGivenNegativeOffset() {
		final int negativeOffset = -2;
		final Book[] givenBooks = {bookA, bookB, bookC};
		givenRepositoryWith(givenBooks);

		List<Book> foundBooks = testee.read(negativeOffset, 2, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatCollectionContainsOnly(foundBooks, bookA, bookB);
	}

	@Test
	public void shouldReadEmptyBooksListWhenGivenOutOfRangeOffset() {
		final Book[] givenBooks = {bookA, bookB, bookC, bookD, bookE};
		final int outOfRangeOffset = givenBooks.length + 2;
		final int anySize = 8;
		givenRepositoryWith(givenBooks);

		List<Book> foundBooks = testee.read(outOfRangeOffset, anySize, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(foundBooks.size(), is(0));
	}

	@Test
	public void shouldReadEmptyBooksListWhenGivenNegativeSize() {
		final int anyOffset = 2;
		final int negativeSize = -1;
		givenRepositoryWith(bookA, bookB);

		List<Book> foundBooks = testee.read(anyOffset, negativeSize, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(foundBooks.size(), is(0));
	}

	@Test
	public void shouldReadEmptyBooksListWhenGivenZeroSize() {
		final int anyOffset = 7;
		givenRepositoryWith(bookA, bookB);

		List<Book> foundBooks = testee.read(anyOffset, 0, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(foundBooks.size(), is(0));
	}

	@Test
	public void shouldFindBooksOrderingAsc() throws NoSuchFieldException {
		final Book[] givenBooks = {bookB, bookA, bookC};
		givenRepositoryWith(givenBooks);
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);

		List<Book> foundBooks = testee.read(0, givenBooks.length, SAMPLE_SORT_COLUMN, ASC);

		assertThatCollectionContainsOnly(foundBooks, bookA, bookB, bookC);
	}

	@Test
	public void shouldFindBooksOrderingDesc() throws NoSuchFieldException {
		final Book[] givenBooks = {bookB, bookA, bookC};
		givenRepositoryWith(givenBooks);
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);

		List<Book> foundBooks = testee.read(0, givenBooks.length, SAMPLE_SORT_COLUMN, DESC);

		assertThatCollectionContainsOnly(foundBooks, bookC, bookB, bookA);
	}

	@Test
	public void shouldFindBooksOrderingCaseInsensitively() throws NoSuchFieldException {
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);
		givenRepositoryWith(bookLowCaseC, bookA);

		List<Book> foundBooks = testee.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThatCollectionContainsOnly(foundBooks, bookA, bookLowCaseC);
	}

	@Test
	public void shouldFindBooksOrderingCaseSensitively() throws NoSuchFieldException {
		givenIgnoreCaseWhileSort(BOOK_TITLE, false);
		givenRepositoryWith(bookLowCaseC, bookD);

		List<Book> foundBooks = testee.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThatCollectionContainsOnly(foundBooks, bookD, bookLowCaseC);
	}

	@Test
	public void shouldCreateBook() {
		testee.create(bookC);

		assertThatRepositoryContainsOnly(bookC);
	}

	@Test
	public void shouldUpdateBook() {
		Book oldBook = aBook(ID_TO_GENERATE, OLD_VERSION, OLD_TITLE);
		givenRepositoryWith(oldBook);
		Book updatingBook = aBook(oldBook.getId(), OLD_VERSION, NEW_TITLE);
		Book updatedBook = aBook(oldBook.getId(), OLD_VERSION + 1, NEW_TITLE);

		testee.update(updatingBook);

		assertThatRepositoryContainsOnly(updatedBook);
	}

	@Test(expected = StaleObjectStateException.class)
	public void shouldFailUpdatingBookWhenModifiedByOtherSession() {
		Book oldBook = aBook(ID_TO_GENERATE, OLD_VERSION, OLD_TITLE);
		givenRepositoryWith(oldBook);
		Book updatingBook = aBook(oldBook.getId(), OLD_VERSION - 1, NEW_TITLE);

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

	private void givenIgnoreCaseWhileSort(PageSorterColumn column, boolean value) throws NoSuchFieldException {
		Field ignoreCaseField = column.getClass().getDeclaredField("ignoreCase");
		ReflectionUtils.makeAccessible(ignoreCaseField);
		ReflectionUtils.setField(ignoreCaseField, column, value);
	}

	private void givenRepositoryWith(Book... books) {
		testRepository.givenRepositoryWith((Object[]) books);
	}

	private void assertThatRepositoryContainsOnly(Book... books) {
		assertThatCollectionContainsOnly(testRepository.getAllBooks(), books);
	}

	private void assertThatCollectionContainsOnly(List<Book> given, Book... books) {
		assertThat(books.length, is(given.size()));
		for (int i = 0; i < given.size(); i++) {
			Book givenBook = given.get(i);
			Book expectedBook = books[i];
			assertThat(givenBook, samePropertyValuesAs(expectedBook));
		}
	}

}
