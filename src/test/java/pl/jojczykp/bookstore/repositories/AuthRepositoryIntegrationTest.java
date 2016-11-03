package pl.jojczykp.bookstore.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.jojczykp.bookstore.entities.User;
import pl.jojczykp.bookstore.testutils.repositories.BooksRepositorySpy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static pl.jojczykp.bookstore.testutils.builders.AuthorityBuilder.anAuthority;
import static pl.jojczykp.bookstore.testutils.builders.UserBuilder.anUser;
import static pl.jojczykp.bookstore.testutils.repositories.BooksRepositorySpy.ID_TO_GENERATE;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/repositories-test-context.xml")
@Transactional
public class AuthRepositoryIntegrationTest {

	private User userA = anUser()
			.withId(ID_TO_GENERATE)
			.withName("nameA").withPassword("passwordA")
			.withNotExpired(true).withNotLocked(true).withCredentialsNotExpired(true).withEnabled(true)
			.withAuthorities(anAuthority(ID_TO_GENERATE, "ROLE_1"))
			.build();

	private User userB = anUser()
			.withId(ID_TO_GENERATE)
			.withName("nameB").withPassword("passwordB")
			.withNotExpired(false).withNotLocked(false).withCredentialsNotExpired(false).withEnabled(false)
			.withAuthorities(anAuthority(ID_TO_GENERATE, "ROLE_2"))
			.build();

	@Autowired private BooksRepositorySpy booksRepositorySpy;
	@Autowired private AuthRepository testee;

	@Test
	public void shouldFindUser() {
		givenRepositoryWith(userA, userB);

		User foundUser = testee.findByName(userB.getName());

		assertThat(foundUser, samePropertyValuesAs(userB));
	}

	@Test
	public void shouldNotFindUserWithNotExistingName() {
		givenRepositoryWith(userA, userB);

		User foundUser = testee.findByName("not" + userA.getName());

		assertThat(foundUser, is(nullValue()));
	}

	@Test
	public void shouldReadUserAuthorities() {
		givenRepositoryWith(userA, userB);

		User foundUser = testee.findByName(userB.getName());

		assertThat(foundUser, samePropertyValuesAs(userB));
	}

	private void givenRepositoryWith(User... users) {
		booksRepositorySpy.givenRepositoryWith((Object[]) users);
	}

}
