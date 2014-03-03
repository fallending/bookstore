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
		Book testee = new Book();

		assertThat(testee.getId(), is(0));
		assertThat(testee.getVersion(), is(0));
		assertThat(testee.getTitle(), is(""));
	}

	@Test
	public void shouldHaveOnlyIdConstructor() {
		Book testee = new Book(ID);

		assertThat(testee.getId(), is(ID));
		assertThat(testee.getTitle(), is(""));
	}

	@Test
	public void shouldHaveNoIdConstructor() {
		Book testee = new Book(TITLE);

		assertThat(testee.getId(), is(0));
		assertThat(testee.getTitle(), is(TITLE));
	}

	@Test
	public void shouldCreateBookWithParamsPassedToConstructor() {
		Book testee = new Book(ID, VERSION, TITLE);

		assertThat(testee.getId(), is(ID));
		assertThat(testee.getVersion(), is(VERSION));
		assertThat(testee.getTitle(), is(TITLE));
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
		Book testee = new Book(ID, VERSION, TITLE);

		String toStringResult = testee.toString();

		assertThat(toStringResult, containsString("id=" + ID));
		assertThat(toStringResult, containsString("version=" + VERSION));
		assertThat(toStringResult, containsString("title='" + TITLE + "'"));
	}

}
