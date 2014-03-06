package pl.jojczykp.bookstore.repository;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.StaleObjectStateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.testutils.repository.TestRepository;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static pl.jojczykp.bookstore.testutils.repository.TestRepository.ID_TO_BE_GENERATED;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/repositories-test-context.xml")
@Transactional
public class BooksRepositoryTest {

	private static final int OLD_VERSION = 3;
	private static final String OLD_TITLE = "Old Title";
	private static final String NEW_TITLE = "New Title";

	private static final PageSorterColumn SAMPLE_SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection SAMPLE_DIRECTION = ASC;

	private static final Book BOOK_A = new Book(0, 1, "Book Title A");
	private static final Book BOOK_B = new Book(0, 7, "Book Title B");
	private static final Book BOOK_C = new Book(0, 3, "Book Title C");
	private static final Book BOOK_D = new Book(0, 4, "Book Title D");
	private static final Book BOOK_E = new Book(0, 5, "Book Title E");
	private static final Book BOOK_C_LOW_CASE = new Book(0, 2, "Book Title c");

	@Autowired private TestRepository testRepository;
	@Autowired private BooksRepository testee;

	@Test
	public void shouldComputeTotalCountOfBooks() {
		final Book[] givenBooks = {BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E};
		givenRepositoryWith(givenBooks);

		assertThat(testee.totalCount(), is(givenBooks.length));
	}

	@Test
	public void shouldFindBooksByOffsetAndSize() {
		final int offset = 1;
		final int size = 3;
		givenRepositoryWith(BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E);

		List<Book> foundBooks = testee.read(offset, size, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatCollectionContainsOnly(foundBooks, BOOK_B, BOOK_C, BOOK_D);
	}

	@Test
	public void shouldFindBooksWhenOffsetInRangeAndSizeOutOfRange() {
		final Book[] givenBooks = {BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E};
		givenRepositoryWith(givenBooks);

		List<Book> foundBooks = testee.read(2, givenBooks.length + 1, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatCollectionContainsOnly(foundBooks, BOOK_C, BOOK_D, BOOK_E);
	}

	@Test
	public void shouldFindBooksFromOffsetZeroWhenGivenNegativeOffset() {
		final int negativeOffset = -2;
		final Book[] givenBooks = {BOOK_A, BOOK_B, BOOK_C};
		givenRepositoryWith(givenBooks);

		List<Book> foundBooks = testee.read(negativeOffset, 2, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThatCollectionContainsOnly(foundBooks, BOOK_A, BOOK_B);
	}

	@Test
	public void shouldReadEmptyBooksListWhenGivenOutOfRangeOffset() {
		final Book[] givenBooks = {BOOK_A, BOOK_B, BOOK_C, BOOK_D, BOOK_E};
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
		givenRepositoryWith(BOOK_A, BOOK_B);

		List<Book> foundBooks = testee.read(anyOffset, negativeSize, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(foundBooks.size(), is(0));
	}

	@Test
	public void shouldReadEmptyBooksListWhenGivenZeroSize() {
		final int anyOffset = 7;
		givenRepositoryWith(BOOK_A, BOOK_B);

		List<Book> foundBooks = testee.read(anyOffset, 0, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(foundBooks.size(), is(0));
	}

	@Test
	public void shouldFindBooksOrderingAsc() throws NoSuchFieldException {
		final Book[] givenBooks = {BOOK_B, BOOK_A, BOOK_C};
		givenRepositoryWith(givenBooks);
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);

		List<Book> foundBooks = testee.read(0, givenBooks.length, SAMPLE_SORT_COLUMN, ASC);

		assertThatCollectionContainsOnly(foundBooks, BOOK_A, BOOK_B, BOOK_C);
	}

	@Test
	public void shouldFindBooksOrderingDesc() throws NoSuchFieldException {
		final Book[] givenBooks = {BOOK_B, BOOK_A, BOOK_C};
		givenRepositoryWith(givenBooks);
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);

		List<Book> foundBooks = testee.read(0, givenBooks.length, SAMPLE_SORT_COLUMN, DESC);

		assertThatCollectionContainsOnly(foundBooks, BOOK_C, BOOK_B, BOOK_A);
	}

	@Test
	public void shouldFindBooksOrderingCaseInsensitively() throws NoSuchFieldException {
		givenIgnoreCaseWhileSort(BOOK_TITLE, true);
		givenRepositoryWith(BOOK_C_LOW_CASE, BOOK_A);

		List<Book> foundBooks = testee.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThatCollectionContainsOnly(foundBooks, BOOK_A, BOOK_C_LOW_CASE);
	}

	@Test
	public void shouldFindBooksOrderingCaseSensitively() throws NoSuchFieldException {
		givenIgnoreCaseWhileSort(BOOK_TITLE, false);
		givenRepositoryWith(BOOK_C_LOW_CASE, BOOK_D);

		List<Book> foundBooks = testee.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThatCollectionContainsOnly(foundBooks, BOOK_D, BOOK_C_LOW_CASE);
	}

	@Test
	public void shouldCreateBook() {
		testee.create(BOOK_C);

		assertThatRepositoryContainsOnly(BOOK_C);
	}

	@Test
	public void shouldUpdateBook() {
		Book oldBook = new Book(ID_TO_BE_GENERATED, OLD_VERSION, OLD_TITLE);
		givenRepositoryWith(oldBook);
		Book updatingBook = new Book(oldBook.getId(), OLD_VERSION, NEW_TITLE);
		Book updatedBook = new Book(oldBook.getId(), OLD_VERSION + 1, NEW_TITLE);

		testee.update(updatingBook);

		assertThatRepositoryContainsOnly(updatedBook);
	}

	@Test(expected = StaleObjectStateException.class)
	public void shouldFailUpdatingBookWhenModifiedByOtherSession() {
		Book oldBook = new Book(ID_TO_BE_GENERATED, OLD_VERSION, OLD_TITLE);
		givenRepositoryWith(oldBook);
		Book updatingBook = new Book(oldBook.getId(), OLD_VERSION - 1, NEW_TITLE);

		testee.update(updatingBook);
	}

	@Test
	public void shouldDeleteBook() {
		givenRepositoryWith(BOOK_A, BOOK_B, BOOK_C);

		testee.delete(BOOK_B.getId());

		assertThatRepositoryContainsOnly(BOOK_A, BOOK_C);
	}

	@Test(expected = ObjectNotFoundException.class)
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
		testRepository.givenRepositoryWith(books);
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
