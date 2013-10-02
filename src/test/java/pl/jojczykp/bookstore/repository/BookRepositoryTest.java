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

	public static final int TEST_DATABASE_SIZE = 15;

	@Autowired private BookRepository repository;

	@Test
	public void shouldCountBooks() {
		assertThat(repository.count(), is(TEST_DATABASE_SIZE));
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
		final int expectedSize = TEST_DATABASE_SIZE - offset;

		List<Book> books = repository.read(offset, MAX_VALUE);

		assertThat(books.size(), is(expectedSize));
		for (int i = 0; i < expectedSize; i++) {
			assertThat(books.get(i).getId(), is(i + offset));
		}
	}

	@Test
	public void shouldListNoBooksWhenOffsetOverRange() {
		List<Book> books = repository.read(TEST_DATABASE_SIZE, 1);

		assertThat(books.size(), is(0));
	}

	@Test
	public void shouldTreatNegativeOffsetAsZero() {
		final int offset = -2;

		List<Book> books = repository.read(offset, TEST_DATABASE_SIZE);

		assertThat(books.size(), is(TEST_DATABASE_SIZE));
	}

	@Test
	public void shouldListAllForNegativeSize() {
		final int size = -1;

		List<Book> books = repository.read(0, size);

		assertThat(books.size(), is(TEST_DATABASE_SIZE));
	}

	@Test
	@Rollback(true)
	public void shouldCreateBook() {
		final String title = "Added book";
		Book book = new Book(title);

		int id = repository.create(book);

		assertThat(repository.count(), is(TEST_DATABASE_SIZE + 1));
		assertThat(repository.get(TEST_DATABASE_SIZE).getId(), is(id));
		assertThat(repository.get(TEST_DATABASE_SIZE).getTitle(), is(title));
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
