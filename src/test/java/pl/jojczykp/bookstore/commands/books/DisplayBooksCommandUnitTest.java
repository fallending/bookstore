package pl.jojczykp.bookstore.commands.books;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.commands.common.MessagesCommand;
import pl.jojczykp.bookstore.commands.common.PagerCommand;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class DisplayBooksCommandUnitTest {

	private DisplayBooksCommand testee;

	@Before
	public void createTestee() {
		testee = new DisplayBooksCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getMessages(), notNullValue());
		assertThat(testee.getPager(), notNullValue());
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
	public void shouldSetBooks() {
		final List<DisplayBookCommand> books = new ArrayList<>();

		testee.setBooks(books);

		assertThat(testee.getBooks(), sameInstance(books));
	}

}
