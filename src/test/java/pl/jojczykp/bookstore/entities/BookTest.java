package pl.jojczykp.bookstore.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.jojczykp.bookstore.testutils.builders.BookBuilder.aBook;

public class BookTest {

	private static final int ID = 8;
	private static final int VERSION = 76;
	private static final String TITLE = "some title";

	@Test
	public void shouldHaveDefaultConstructorForHibernate() {
		Book testee = new Book();

		assertThat(testee.getId(), is(0));
		assertThat(testee.getVersion(), is(0));
		assertThat(testee.getTitle(), is(""));
	}

	@Test
	public void shouldSetId() {
		Book testee = new Book();

		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetVersion() {
		Book testee = new Book();

		testee.setVersion(VERSION);

		assertThat(testee.getVersion(), is(VERSION));
	}

	@Test
	public void shouldSetTitle() {
		Book testee = new Book();

		testee.setTitle(TITLE);

		assertThat(testee.getTitle(), is(TITLE));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(Book.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetailsForDiagnostic() {
		Book testee = aBook(ID, VERSION, TITLE);

		String toStringResult = testee.toString();

		assertThat(toStringResult, containsString("id=" + ID));
		assertThat(toStringResult, containsString("version=" + VERSION));
		assertThat(toStringResult, containsString("title='" + TITLE + "'"));
	}

}
