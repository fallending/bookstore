package pl.jojczykp.bookstore.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookTest {

	private static final int ID = 8;
	private static final int VERSION = 76;
	private static final String TITLE = "some title";

	@Test
	public void shouldHaveNoParameterConstructorForHibernate() {
		Book book = new Book();

		assertThat(book.getId(), is(0));
		assertThat(book.getVersion(), is(0));
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
	public void shouldCreateBookWithParamsPassedToConstructor() {
		Book book = new Book(ID, VERSION, TITLE);

		assertThat(book.getId(), is(ID));
		assertThat(book.getVersion(), is(VERSION));
		assertThat(book.getTitle(), is(TITLE));
	}

	@Test
	public void shouldSetId() {
		Book book = new Book();

		book.setId(ID);

		assertThat(book.getId(), is(ID));
	}

	@Test
	public void shouldSetVersion() {
		Book book = new Book();

		book.setVersion(VERSION);

		assertThat(book.getVersion(), is(VERSION));
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
		Book testee = new Book(ID, VERSION, TITLE);

		String toStringResult = testee.toString();

		assertThat(toStringResult, containsString("id=" + ID));
		assertThat(toStringResult, containsString("version=" + VERSION));
		assertThat(toStringResult, containsString("title='" + TITLE + "'"));
	}

}
