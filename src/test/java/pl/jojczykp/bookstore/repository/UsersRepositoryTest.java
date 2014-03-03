package pl.jojczykp.bookstore.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.jojczykp.bookstore.domain.User;

import java.lang.reflect.Field;

import static java.util.EnumSet.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static pl.jojczykp.bookstore.domain.Permission.READ;
import static pl.jojczykp.bookstore.domain.Permission.WRITE;
import static pl.jojczykp.bookstore.testutils.builders.UserBuilder.aUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/repositories-test-context.xml")
@Transactional
public class UsersRepositoryTest {

	private static final User USER_1 = aUser().withId(1)
			.withName("user_1").withPassword("password_1")
			.withNotExpired(true).withNotLocked(true).withCredentialsNotExpired(true).withEnabled(true)
			.withPermissions(of(READ, WRITE)).build();

	private static final User USER_2 = aUser().withId(2)
			.withName("user_2").withPassword("password_2")
			.withNotExpired(false).withNotLocked(false).withCredentialsNotExpired(false).withEnabled(false)
			.withPermissions(of(READ)).build();

	@Autowired private UsersRepository testee;

	@Test
	@Rollback(true)
	public void shouldCreateUser() {
		int id = testee.create(USER_2);

		assertEqualFields(testee.get(id), USER_2);
	}

	@Test
	@Rollback(true)
	public void shouldGetUser() {
		givenRepositoryWith(USER_1, USER_2);

		User foundUser = testee.get(USER_2.getId());

		assertEqualFields(foundUser, USER_2);
	}

	@Test
	@Rollback(true)
	public void shouldFindUser() {
		givenRepositoryWith(USER_1, USER_2);

		User foundUser = testee.findByNameAndPassword(USER_1.getName(), USER_1.getPassword());

		assertEqualFields(foundUser, USER_1);
	}

	@Test
	@Rollback(true)
	public void shouldNotFindUserWithNotExistingName() {
		givenRepositoryWith(USER_1, USER_2);

		User foundUser = testee.findByNameAndPassword("not" + USER_1.getName(), USER_1.getPassword());

		assertThat(foundUser, is(nullValue()));
	}

	@Test
	@Rollback(true)
	public void shouldNotFindUserWithWrongPassword() {
		givenRepositoryWith(USER_1, USER_2);

		User foundUser = testee.findByNameAndPassword(USER_2.getName(), "wrong" + USER_2.getPassword());

		assertThat(foundUser, is(nullValue()));
	}

	private void givenRepositoryWith(User... users) {
		for (User user: users) {
			testee.create(user);
		}
	}

	private void assertEqualFields(User foundUser, User givenUser) {
		for (Field field: User.class.getFields()) {
			assertFieldValuesAreEqual(field, foundUser, givenUser);
		}
	}

	private void assertFieldValuesAreEqual(Field field, User foundUser, User givenUser) {
		try {
			assertThat(field.get(foundUser), is(equalTo(field.get(givenUser))));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
