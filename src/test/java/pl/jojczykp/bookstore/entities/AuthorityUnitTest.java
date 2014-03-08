package pl.jojczykp.bookstore.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AuthorityUnitTest {

	private static final int ID = 7;
	private static final String ROLE = "some_role";

	private Authority testee;

	@Before
	public void setupTestee() {
		testee = new Authority();
	}

	@Test
	public void shouldHaveDefaultConstructorForHibernate() {
		assertThat(testee.getId(), is(0));
		assertThat(testee.getRole(), is(""));
	}

	@Test
	public void shouldSetId() {
		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetPassword() {
		testee.setRole(ROLE);

		assertThat(testee.getRole(), is(ROLE));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(Authority.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetailsForDiagnostic() {
		testee.setId(ID);
		testee.setRole(ROLE);

		String toStringResult = testee.toString();

		assertThat(toStringResult, equalTo(
				format("%s{id=%d, role='%s'}", testee.getClass().getSimpleName(), ID, ROLE)));
	}

}
