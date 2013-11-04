package pl.jojczykp.bookstore.command;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.utils.ScrollParams;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class BooksCommandTest {

	private BooksCommand testee;

	@Before
	public void createTestee() {
		testee = new BooksCommand();
	}

	@Test
	public void shouldHaveDefaultConstructor() {
		new BooksCommand();
	}

	@Test
	public void shouldSetOriginalScrollParams() {
		final ScrollParams scrollParams = new ScrollParams();

		testee.setOriginalScrollParams(scrollParams);

		assertThat(testee.getOriginalScrollParams(), sameInstance(scrollParams));
	}

	@Test
	public void shouldSetLimitedScrollParams() {
		final ScrollParams scrollParams = new ScrollParams();

		testee.setLimitedScrollParams(scrollParams);

		assertThat(testee.getLimitedScrollParams(), sameInstance(scrollParams));
	}

	@Test
	public void shouldSetPageSize() {
		final int pageSize = 7;

		testee.setPageSize(pageSize);

		assertThat(testee.getPageSize(), equalTo(pageSize));
	}

	@Test
	public void shouldSetTotalCount() {
		final int totalCount = 4;

		testee.setTotalCount(totalCount);

		assertThat(testee.getTotalCount(), equalTo(totalCount));
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
