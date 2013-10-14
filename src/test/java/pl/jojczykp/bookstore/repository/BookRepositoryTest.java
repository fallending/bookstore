package pl.jojczykp.bookstore.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.jojczykp.bookstore.domain.Book;

import java.util.List;

import static java.lang.Integer.MAX_VALUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/repository-test-context.xml")
@Transactional
public class BookRepositoryTest {

	public static final int TOTAL_TEST_BOOKS_COUNT = 15;

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
		List<Book> books = repository.read(2, 3);

		assertThat(books.size(), is(3));
		assertThat(books.get(0).getId(), is(2));
		assertThat(books.get(1).getId(), is(3));
		assertThat(books.get(2).getId(), is(4));
	}
	@Test
	public void shouldListBooksWhenOffsetInRangeAndSizeOverRange() {
		final int offset = 2;
		final int expectedSize = TOTAL_TEST_BOOKS_COUNT - offset;

		List<Book> books = repository.read(offset, MAX_VALUE);

		assertThat(books.size(), is(expectedSize));
		for (int i = 0; i < expectedSize; i++) {
			assertThat(books.get(i).getId(), is(i + offset));
		}
	}

	@Test
	public void shouldListNoBooksWhenOffsetOverRange() {
		List<Book> books = repository.read(TOTAL_TEST_BOOKS_COUNT, 1);

		assertThat(books.size(), is(0));
	}

	@Test
	public void shouldTreatNegativeOffsetAsZero() {
		final int offset = -2;

		List<Book> books = repository.read(offset, TOTAL_TEST_BOOKS_COUNT);

		assertThat(books.size(), is(TOTAL_TEST_BOOKS_COUNT));
	}

	@Test
	public void shouldListAllForNegativeSize() {
		final int size = -1;

		List<Book> books = repository.read(0, size);

		assertThat(books.size(), is(TOTAL_TEST_BOOKS_COUNT));
	}

	@Test
	@Rollback(true)
	public void shouldCreateBook() {
		final String title = "Added book";
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

		repository.update(new Book(id, newTitle));

		assertThat(repository.get(id).getTitle(), is(newTitle));
	}

	@Test
	@Rollback(true)
	public void shouldDeleteBook() {
		final int id = 3;

		repository.delete(id);

		assertThat(repository.get(id), is(nullValue()));
	}

}
