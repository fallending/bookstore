package pl.jojczykp.bookstore.repository;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.StaleObjectStateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/repositories-test-context.xml")
@Transactional
public class BooksRepositoryTest {

	private static final PageSorterColumn SAMPLE_SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection SAMPLE_DIRECTION = ASC;

	private static final Book BOOK_A = new Book(0, 1, "Book Title A");
	private static final Book BOOK_B = new Book(0, 7, "Book Title B");
	private static final Book BOOK_C = new Book(0, 3, "Book Title C");
	private static final Book BOOK_D = new Book(0, 4, "Book Title D");
	private static final Book BOOK_E = new Book(0, 5, "Book Title E");
	private static final Book BOOK_C_LOW_CASE = new Book(0, 2, "Book Title c");

	@Autowired private BooksRepository testee;

	@Test
	@Rollback(true)
	public void shouldComputeTotalCountOfBooks() {
		final Book[] givenBooks = {BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E};
		givenRepositoryWith(givenBooks);

		assertThat(testee.totalCount(), is(givenBooks.length));
	}

	@Test
	@Rollback(true)
	public void shouldGetBook() {
		givenRepositoryWith(BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E);

		Book foundBook = testee.get(BOOK_D.getId());

		assertEqualFields(foundBook, BOOK_D);
	}

	@Test
	@Rollback(true)
	public void shouldfoundBooksByOffsetAndSize() {
		final int offset = 1;
		final int size = 3;
		givenRepositoryWith(BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E);

		List<Book> foundBooks = testee.read(offset, size, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatHasOnly(foundBooks, BOOK_B, BOOK_C, BOOK_D);
	}

	@Test
	@Rollback(true)
	public void shouldfoundBooksWhenOffsetInRangeAndSizeOutOfRange() {
		final Book[] givenBooks = {BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E};
		givenRepositoryWith(givenBooks);

		List<Book> foundBooks = testee.read(2, givenBooks.length + 1, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatHasOnly(foundBooks, BOOK_C, BOOK_D, BOOK_E);
	}

	@Test
	@Rollback(true)
	public void shouldfoundBooksFromOffsetZeroWhenGivenNegativeOffset() {
		final int negativeOffset = -2;
		final Book[] givenBooks = {BOOK_A, BOOK_B, BOOK_C};
		givenRepositoryWith(givenBooks);

		List<Book> foundBooks = testee.read(negativeOffset, 2, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatHasOnly(foundBooks, BOOK_A, BOOK_B);
	}

	@Test
	@Rollback(true)
	public void shouldReadEmptyBooksListWhenGivenOutOfRangeOffset() {
		final Book[] givenBooks = {BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E};
		final int outOfRangeOffset = givenBooks.length + 2;
		final int anySize = 8;
		givenRepositoryWith(givenBooks);

		List<Book> foundBooks = testee.read(outOfRangeOffset, anySize, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(foundBooks.size(), is(0));
	}

	@Test
	@Rollback(true)
	public void shouldReadEmptyBooksListWhenGivenNegativeSize() {
		final int anyOffset = 2;
		final int negativeSize = -1;
		givenRepositoryWith(BOOK_A, BOOK_B);

		List<Book> foundBooks = testee.read(anyOffset, negativeSize, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(foundBooks.size(), is(0));
	}

	@Test
	@Rollback(true)
	public void shouldReadEmptyBooksListWhenGivenZeroSize() {
		final int anyOffset = 7;
		givenRepositoryWith(BOOK_A, BOOK_B);

		List<Book> foundBooks = testee.read(anyOffset, 0, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(foundBooks.size(), is(0));
	}

	@Test
	@Rollback(true)
	public void shouldfoundBooksOrderingAsc() throws NoSuchFieldException {
		final Book[] givenBooks = {BOOK_B, BOOK_A, BOOK_C};
		givenRepositoryWith(givenBooks);
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);

		List<Book> foundBooks = testee.read(0, givenBooks.length, SAMPLE_SORT_COLUMN, ASC);

		assertThatHasOnly(foundBooks, BOOK_A, BOOK_B, BOOK_C);
	}

	@Test
	@Rollback(true)
	public void shouldfoundBooksOrderingDesc() throws NoSuchFieldException {
		final Book[] givenBooks = {BOOK_B, BOOK_A, BOOK_C};
		givenRepositoryWith(givenBooks);
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);

		List<Book> foundBooks = testee.read(0, givenBooks.length, SAMPLE_SORT_COLUMN, DESC);

		assertThatHasOnly(foundBooks, BOOK_C, BOOK_B, BOOK_A);
	}

	@Test
	@Rollback(true)
	public void shouldfoundBooksOrderingCaseInsensitively() throws NoSuchFieldException {
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);
		givenRepositoryWith(BOOK_C_LOW_CASE, BOOK_A);

		List<Book> foundBooks = testee.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThatHasOnly(foundBooks, BOOK_A, BOOK_C_LOW_CASE);
	}

	@Test
	@Rollback(true)
	public void shouldfoundBooksOrderingCaseSensitively() throws NoSuchFieldException {
		givenIgnoreCaseWhileSort(BOOK_TITLE, false);
		givenRepositoryWith(BOOK_C_LOW_CASE, BOOK_D);

		List<Book> foundBooks = testee.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThatHasOnly(foundBooks, BOOK_D, BOOK_C_LOW_CASE);
	}

	@Test
	@Rollback(true)
	public void shouldCreateBook() {
		int id = testee.create(BOOK_C);

		assertThat(testee.totalCount(), is(1));
		assertEqualFields(testee.get(id), BOOK_C);
	}

	@Test
	@Rollback(true)
	public void shouldUpdateBook() {
		final int id = 4;
		Book oldBook = new Book(id, 2, "Old Title");
		Book newBook = new Book(id, 2, "New Title");
		givenRepositoryWith(oldBook);

		testee.update(newBook);

		assertEqualFields(testee.get(id), newBook);
	}

	@Test(expected = StaleObjectStateException.class)
	@Rollback(true)
	public void shouldFailUpdatingBookWhenModifiedByOtherSession() {
		final int anyId = 7;
		final int currentVersion = 7;
		Book oldBook = new Book(anyId, currentVersion, "Old Title");
		givenRepositoryWith(oldBook);
		Book newBook = new Book(oldBook.getId(), currentVersion - 1, "New Title");

		testee.update(newBook);
	}

	@Test
	@Rollback(true)
	public void shouldDeleteBook() {
		givenRepositoryWith(BOOK_A, BOOK_B, BOOK_C);

		testee.delete(BOOK_B.getId());

		assertThat(testee.totalCount(), is(2));
		assertEqualFields(testee.get(BOOK_A.getId()), BOOK_A);
		assertEqualFields(testee.get(BOOK_C.getId()), BOOK_C);
	}

	@Test(expected = ObjectNotFoundException.class)
	@Rollback(true)
	public void shouldFailDeletingNotExistingBook() {
		givenRepositoryWith(BOOK_A, BOOK_B);
		int notExistingId = BOOK_A.getId() + BOOK_B.getId();

		testee.delete(notExistingId);
	}

	private void givenIgnoreCaseWhileSort(PageSorterColumn column, boolean value) throws NoSuchFieldException {
		Field ignoreCaseField = column.getClass().getDeclaredField("ignoreCase");
		ReflectionUtils.makeAccessible(ignoreCaseField);
		ReflectionUtils.setField(ignoreCaseField, column, value);
	}

	private void givenRepositoryWith(Book... books) {
		for (Book book: books) {
			testee.create(book);
		}
	}

	private void assertThatHasOnly(List<Book> given, Book... expected) {
		assertThat(expected.length, is(given.size()));
		for (int i = 0; i < given.size(); i++) {
			assertEqualFields(given.get(i), expected[i]);
		}
	}

	private void assertEqualFields(Book foundBook, Book givenBook) {
		for (Field field: Book.class.getFields()) {
			assertFieldValuesAreEqual(field, foundBook, givenBook);
		}
	}

	private void assertFieldValuesAreEqual(Field field, Book foundBook, Book givenBook) {
		try {
			assertThat(field.get(foundBook), is(equalTo(field.get(givenBook))));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
