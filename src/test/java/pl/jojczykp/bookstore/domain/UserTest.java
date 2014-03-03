package pl.jojczykp.bookstore.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.jojczykp.bookstore.domain.Permission.WRITE;

public class UserTest {

	private static final int ID = 3;
	private static final String NAME = "some_name";
	private static final String PASSWORD = "some_password";
	private static final Set<Permission> PERMISSIONS = new HashSet<>();

	@Test
	public void shouldHaveNoParameterConstructorForHibernate() {
		User testee = new User();

		assertThat(testee.getId(), is(0));
		assertThat(testee.getName(), is(""));
		assertThat(testee.isNotExpired(), is(false));
		assertThat(testee.isNotLocked(), is(false));
		assertThat(testee.isCredentialsNotExpired(), is(false));
		assertThat(testee.isEnabled(), is(false));
		assertThat(testee.getPermissions(), is(notNullValue()));
	}

	@Test
	public void shouldSetId() {
		User testee = new User();

		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetName() {
		User testee = new User();

		testee.setName(NAME);

		assertThat(testee.getName(), is(NAME));
	}

	@Test
	public void shouldSetPassword() {
		User testee = new User();

		testee.setPassword(PASSWORD);

		assertThat(testee.getPassword(), is(PASSWORD));
	}

	@Test
	public void shouldSetNotExpired() {
		User testee = new User();

		testee.setNotExpired(true);

		assertThat(testee.isNotExpired(), is(true));
	}

	@Test
	public void shouldSetNotLocked() {
		User testee = new User();

		testee.setNotLocked(true);

		assertThat(testee.isNotLocked(), is(true));
	}

	@Test
	public void shouldSetCredentialsNotExpired() {
		User testee = new User();

		testee.setCredentialsNotExpired(true);

		assertThat(testee.isCredentialsNotExpired(), is(true));
	}

	@Test
	public void shouldSetEnabled() {
		User testee = new User();

		testee.setEnabled(true);

		assertThat(testee.isEnabled(), is(true));
	}

	@Test
	public void shouldSetPermissions() {
		User testee = new User();

		testee.setPermissions(PERMISSIONS);

		assertThat(testee.getPermissions(), is(sameInstance(PERMISSIONS)));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(User.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetailsForDiagnostic() {
		User testee = new User();
		testee.setId(ID);
		testee.setName(NAME);
		testee.setPassword(PASSWORD);
		testee.getPermissions().add(WRITE);

		String toStringResult = testee.toString();

		assertThat(toStringResult, containsString("id=" + ID));
		assertThat(toStringResult, containsString("name='" + NAME + "'"));
		assertThat(toStringResult, containsString("password='" + PASSWORD + "'"));
		assertThat(toStringResult, containsString("notExpired='false'"));
		assertThat(toStringResult, containsString("notLocked='false'"));
		assertThat(toStringResult, containsString("credentialsNotExpired='false'"));
		assertThat(toStringResult, containsString("enabled='false'"));
		assertThat(toStringResult, containsString("permissions=[" + WRITE + "]"));
	}

}
