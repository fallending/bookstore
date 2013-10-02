package pl.jojczykp.bookstore.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookTest {

	@Test
	public void shouldHaveNoParameterConstructorForHibernate() {
		Book book = new Book();

		assertThat(book.getId(), is(0));
		assertThat(book.getTitle(), is(""));
	}

	@Test
	public void shouldHaveOnlyIdConstructor() {
		final int id = 2;

		Book book = new Book(id);

		assertThat(book.getId(), is(id));
		assertThat(book.getTitle(), is(""));
	}

	@Test
	public void shouldHaveNoIdConstructor() {
		final String title = "A Title";

		Book book = new Book(title);

		assertThat(book.getId(), is(0));
		assertThat(book.getTitle(), is(title));
	}

	@Test
	public void shouldCreateBookWithTitlePassedToConstructor() {
		final int id = 7;
		final String title = "A Title";

		Book book = new Book(id, title);

		assertThat(book.getId(), is(id));
		assertThat(book.getTitle(), is(title));
	}

	@Test
	public void shouldMeetEqualsHashcodeContract() {
		EqualsVerifier.forClass(Book.class)
				.usingGetClass()
				.verify();
	}
}
