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
		assertThat(testee.getMessages(), notNullValue());
		assertThat(testee.getPager(), notNullValue());
		assertThat(testee.getNewBook(), notNullValue());
		assertThat(testee.getUpdatedBook(), notNullValue());
		assertThat(testee.getBooks(), empty());
	}

	@Test
	public void shouldSetMessages() {
		final MessagesCommand messages = new MessagesCommand();

		testee.setMessages(messages);

		assertThat(testee.getMessages(), sameInstance(messages));
	}

	@Test
	public void shouldSetPager() {
		final PagerCommand pager = new PagerCommand();

		testee.setPager(pager);

		assertThat(testee.getPager(), sameInstance(pager));
	}

	@Test
	public void shouldSetNewBook() {
		final BookCommand book = new BookCommand();

		testee.setNewBook(book);

		assertThat(testee.getNewBook(), sameInstance(book));
	}

	@Test
	public void shouldSetUpdatedBook() {
		final BookCommand book = new BookCommand();

		testee.setUpdatedBook(book);

		assertThat(testee.getUpdatedBook(), sameInstance(book));
	}

	@Test
	public void shouldSetBooks() {
		final List<BookCommand> books = new ArrayList<>();

		testee.setBooks(books);

		assertThat(testee.getBooks(), sameInstance(books));
	}

}
