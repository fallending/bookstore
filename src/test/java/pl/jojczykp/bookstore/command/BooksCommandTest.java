package pl.jojczykp.bookstore.command;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.domain.Book;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class BooksCommandTest {

	private BooksCommand testee;

	@Before
	public void createTestee() {
		testee = new BooksCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getScroll(), notNullValue());
		assertThat(testee.getNewBook(), notNullValue());
		assertThat(testee.getBooks(), empty());
	}

	@Test
	public void shouldSetScroll() {
		final ScrollCommand scroll = new ScrollCommand();

		testee.setScroll(scroll);

		assertThat(testee.getScroll(), sameInstance(scroll));
	}

	@Test
	public void shouldSetNewBook() {
		final Book book = new Book();

		testee.setNewBook(book);

		assertThat(testee.getNewBook(), sameInstance(book));
	}

	@Test
	public void shouldSetBooks() {
		final List<Book> books = new ArrayList<>();

		testee.setBooks(books);

		assertThat(testee.getBooks(), sameInstance(books));
	}
}
