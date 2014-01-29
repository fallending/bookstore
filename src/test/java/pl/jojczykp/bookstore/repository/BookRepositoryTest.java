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
import pl.jojczykp.bookstore.utils.ScrollSorterColumn;
import pl.jojczykp.bookstore.utils.ScrollSorterDirection;

import java.lang.reflect.Field;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static pl.jojczykp.bookstore.utils.ScrollSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.ScrollSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.ScrollSorterDirection.DESC;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/repository-test-context.xml")
@Transactional
public class BookRepositoryTest {

	private static final int TOTAL_TEST_BOOKS_COUNT = 15;
	private static final int CURRENT_TEST_BOOK_VERSION = 4;
	private static final ScrollSorterColumn SAMPLE_SORT_COLUMN = BOOK_TITLE;
	private static final ScrollSorterDirection SAMPLE_DIRECTION = ASC;

	@Autowired private BookRepository repository;

	@Test
	public void shouldComputeTotalCountOfBooks() {
		assertThat(repository.totalCount(), is(TOTAL_TEST_BOOKS_COUNT));
	}

	@Test
	public void shouldGetBook() {
		final int id = 4;

		Book book = repository.get(id);

		assertThat(book.getId(), is(id));
	}

	@Test
	public void shouldListBooksByOffsetAndSize() {
		final int offset = 2;
		final int size = 3;

		List<Book> books = repository.read(offset, size, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(books.size(), is(size));
		assertThat(books.get(0), is(notNullValue()));
		assertThat(books.get(1), is(notNullValue()));
		assertThat(books.get(2), is(notNullValue()));
	}
	@Test
	public void shouldListBooksWhenOffsetInRangeAndSizeOverRange() {
		final int offset = 2;
		final int expectedSize = TOTAL_TEST_BOOKS_COUNT - offset;

		List<Book> books = repository.read(offset, MAX_VALUE, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(books.size(), is(expectedSize));
		for (int i = 0; i < expectedSize; i++) {
			assertThat(books.get(i), is(notNullValue()));
		}
	}

	@Test
	public void shouldListNoBooksWhenOffsetOverRange() {
		final int anySize = 8;

		List<Book> books = repository.read(TOTAL_TEST_BOOKS_COUNT, anySize, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(books.size(), is(0));
	}

	@Test
	public void shouldTreatNegativeOffsetAsZero() {
		final int offset = -2;

		List<Book> books = repository.read(offset, TOTAL_TEST_BOOKS_COUNT, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(books.size(), is(TOTAL_TEST_BOOKS_COUNT));
	}

	@Test
	public void shouldListEmptyForNegativeSize() {
		final int size = -1;

		List<Book> books = repository.read(2, size, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(books, emptyCollectionOf(Book.class));
	}

	@Test
	public void shouldListEmptyForZeroSize() {
		final int offset = 7;

		List<Book> books = repository.read(offset, 0, SAMPLE_SORT_COLUMN, SAMPLE_DIRECTION);

		assertThat(books, emptyCollectionOf(Book.class));
	}

	@Test
	public void shouldOrderAsc() throws NoSuchFieldException {
		setSortColumnWithIgnoreCase(BOOK_TITLE, true);

		List<Book> books = repository.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThat(books.get(0).getTitle(), is(equalTo("Test Book 00 AAA")));
		assertThat(books.get(1).getTitle(), is(equalTo("Test Book 00 bbb")));
	}

	@Test
	public void shouldOrderDesc() throws NoSuchFieldException {
		setSortColumnWithIgnoreCase(BOOK_TITLE, true);

		List<Book> books = repository.read(1, 2, SAMPLE_SORT_COLUMN, DESC);

		assertThat(books.get(0).getTitle(), is(equalTo("Test Book 14")));
		assertThat(books.get(1).getTitle(), is(equalTo("Test Book 13")));
	}

	@Test
	public void shouldOrderCaseInsensitive() throws NoSuchFieldException {
		setSortColumnWithIgnoreCase(BOOK_TITLE, false);

		List<Book> books = repository.read(0, 2, SAMPLE_SORT_COLUMN, ASC);

		assertThat(books.get(0).getTitle(), is(equalTo("Test Book 00 AAA")));
		assertThat(books.get(1).getTitle(), is(equalTo("Test Book 00 bbb")));
	}

	private void setSortColumnWithIgnoreCase(ScrollSorterColumn column, boolean value) throws NoSuchFieldException {
		Field ignoreCaseField = column.getClass().getDeclaredField("ignoreCase");
		ReflectionUtils.makeAccessible(ignoreCaseField);
		ReflectionUtils.setField(ignoreCaseField, column, value);
	}
	@Test
	@Rollback(true)
	public void shouldCreateBook() {
		final String title = "Created book";
		Book book = new Book(title);

		int id = repository.create(book);

		assertThat(repository.totalCount(), is(TOTAL_TEST_BOOKS_COUNT + 1));
		assertThat(repository.get(TOTAL_TEST_BOOKS_COUNT).getId(), is(id));
		assertThat(repository.get(TOTAL_TEST_BOOKS_COUNT).getTitle(), is(title));
	}

	@Test
	@Rollback(true)
	public void shouldUpdateBook() {
		final int id = 4;
		final String newTitle = "New Title";

		repository.update(new Book(id, CURRENT_TEST_BOOK_VERSION, newTitle));

		assertThat(repository.get(id).getTitle(), is(newTitle));
	}

	@Test(expected = StaleObjectStateException.class)
	@Rollback(true)
	public void shouldFailUpdatingBookWhenModifiedByOtherSession() {
		final int id = 7;
		final String newTitle = "New Title";

		repository.update(new Book(id, CURRENT_TEST_BOOK_VERSION - 1, newTitle));
	}

	@Test(expected = ObjectNotFoundException.class)
	@Rollback(true)
	public void shouldDeleteBook() {
		final int id = 3;

		repository.delete(id);

		repository.get(id);
	}

	@Test(expected = ObjectNotFoundException.class)
	@Rollback(true)
	public void shouldFailWhenDeletingNotExistingRecord() {
		final int idThatDoesNotExist = 1234;

		repository.delete(idThatDoesNotExist);
	}

}
