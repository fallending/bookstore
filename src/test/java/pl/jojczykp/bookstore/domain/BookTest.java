package pl.jojczykp.bookstore.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookTest {

	private static final int ID = 8;
	private static final String TITLE = "some title";

	@Test
	public void shouldHaveNoParameterConstructorForHibernate() {
		Book book = new Book();

		assertThat(book.getId(), is(0));
		assertThat(book.getTitle(), is(""));
	}

	@Test
	public void shouldHaveOnlyIdConstructor() {
		Book book = new Book(ID);

		assertThat(book.getId(), is(ID));
		assertThat(book.getTitle(), is(""));
	}

	@Test
	public void shouldHaveNoIdConstructor() {
		Book book = new Book(TITLE);

		assertThat(book.getId(), is(0));
		assertThat(book.getTitle(), is(TITLE));
	}

	@Test
	public void shouldCreateBookWithTitlePassedToConstructor() {
		Book book = new Book(ID, TITLE);

		assertThat(book.getId(), is(ID));
		assertThat(book.getTitle(), is(TITLE));
	}

	@Test
	public void shouldSetId() {
		Book book = new Book();

		book.setId(ID);

		assertThat(book.getId(), is(ID));
	}

	@Test
	public void shouldSetTitle() {
		Book book = new Book();

		book.setTitle(TITLE);

		assertThat(book.getTitle(), is(TITLE));
	}

	@Test
	public void shouldMeetEqualsHashcodeContract() {
		EqualsVerifier.forClass(Book.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetailsForDiagnostic() {
		Book testee = new Book(ID, TITLE);

		String toStringResult = testee.toString();

		assertThat(toStringResult, containsString("id=" + ID));
		assertThat(toStringResult, containsString("title='" + TITLE + "'"));
	}

}
