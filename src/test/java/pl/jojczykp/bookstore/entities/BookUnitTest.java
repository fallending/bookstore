package pl.jojczykp.bookstore.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static pl.jojczykp.bookstore.entities.builders.BookBuilder.aBook;

@RunWith(MockitoJUnitRunner.class)
public class BookUnitTest {

	private static final int ID = 8;
	private static final int VERSION = 76;
	private static final String TITLE = "some title";

	@Mock private BookFile bookFile;
	private static final String BOOK_FILE_AS_STRING = "bookFile as String";

	private Book testee = new Book();

	@Before
	public void setupTestee() {
		testee = new Book();
	}

	@Before
	public void setupMock() {
		given(bookFile.toString()).willReturn(BOOK_FILE_AS_STRING);
	}

	@Test
	public void shouldHaveDefaultConstructorForHibernate() {
		assertThat(testee.getId(), is(0));
		assertThat(testee.getVersion(), is(0));
		assertThat(testee.getTitle(), is(""));
		assertThat(testee.getBookFile(), is(nullValue()));
	}

	@Test
	public void shouldSetId() {
		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetVersion() {
		testee.setVersion(VERSION);

		assertThat(testee.getVersion(), is(VERSION));
	}

	@Test
	public void shouldSetTitle() {
		testee.setTitle(TITLE);

		assertThat(testee.getTitle(), is(TITLE));
	}

	@Test
	public void shouldSetBookFile() {
		testee.setBookFile(bookFile);

		assertThat(testee.getBookFile(), is(sameInstance(bookFile)));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(Book.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetails() {
		Book testeeFilled = aBook().withId(ID).withVersion(VERSION).withTitle(TITLE).withBookFile(bookFile).build();

		String toStringResult = testeeFilled.toString();

		assertThat(toStringResult, equalTo(
				format("%s{id=%d, version=%d, title='%s', bookFile=" + BOOK_FILE_AS_STRING + "}",
						testeeFilled.getClass().getSimpleName(), ID, VERSION, TITLE)));
	}

}
