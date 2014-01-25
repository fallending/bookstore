package pl.jojczykp.bookstore.command;

import org.junit.Before;
import org.junit.Test;

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
		assertThat(testee.getUpdateBookId(), notNullValue());
		assertThat(testee.getUpdateBookTitle(), notNullValue());
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
		final BookCommand book = new BookCommand();

		testee.setNewBook(book);

		assertThat(testee.getNewBook(), sameInstance(book));
	}

	@Test
	public void shouldSetUpdateBookId() {
		final int updateBookId = 9;

		testee.setUpdateBookId(updateBookId);

		assertThat(testee.getUpdateBookId(), sameInstance(updateBookId));
	}

	@Test
	public void shouldSetUpdateBookTitle() {
		final String updateBookTitle = "someTitle";

		testee.setUpdateBookTitle(updateBookTitle);

		assertThat(testee.getUpdateBookTitle(), sameInstance(updateBookTitle));
	}

	@Test
	public void shouldSetBooks() {
		final List<BookCommand> books = new ArrayList<>();

		testee.setBooks(books);

		assertThat(testee.getBooks(), sameInstance(books));
	}

	@Test
	public void shouldSetMessage() {
		final String message = "some message";

		testee.setMessage(message);

		assertThat(testee.getMessage(), sameInstance(message));
	}
}
