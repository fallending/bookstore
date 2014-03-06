package pl.jojczykp.bookstore.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.jojczykp.bookstore.domain.Authority;
import pl.jojczykp.bookstore.domain.User;
import pl.jojczykp.bookstore.testutils.repository.TestRepository;

import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static pl.jojczykp.bookstore.testutils.builders.UserBuilder.aUser;
import static pl.jojczykp.bookstore.testutils.repository.TestRepository.ID_TO_BE_GENERATED;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/repositories-test-context.xml")
@Transactional
public class SecurityRepositoryTest {

	private static final Authority AUTHORITY_1 = new Authority();
	private static final Authority AUTHORITY_2 = new Authority();

	private static final User USER_1 = aUser().withId(ID_TO_BE_GENERATED)
			.withName("name_1").withPassword("password_1")
			.withNotExpired(true).withNotLocked(true).withCredentialsNotExpired(true).withEnabled(true)
			.withAuthorities(singleton(AUTHORITY_1)).build();

	private static final User USER_2 = aUser().withId(ID_TO_BE_GENERATED)
			.withName("name_2").withPassword("password_2")
			.withNotExpired(false).withNotLocked(false).withCredentialsNotExpired(false).withEnabled(false)
			.withAuthorities(singleton(AUTHORITY_2)).build();

	@Autowired private TestRepository testRepository;
	@Autowired private SecurityRepository testee;

	@Test
	public void shouldFindUser() {
		givenRepositoryWith(USER_1, USER_2);

		User foundUser = testee.findByName(USER_1.getName());

		assertThat(foundUser, samePropertyValuesAs(USER_1));
	}

	@Test
	public void shouldNotFindUserWithNotExistingName() {
		givenRepositoryWith(USER_1, USER_2);

		User foundUser = testee.findByName("not" + USER_1.getName());

		assertThat(foundUser, is(nullValue()));
	}

	@Test
	public void shouldReadUserAuthorities() {
		givenRepositoryWith(USER_1, USER_2);

		User foundUser = testee.findByName(USER_2.getName());

		assertThat(foundUser, samePropertyValuesAs(USER_2));
	}

	private void givenRepositoryWith(User... users) {
		testRepository.givenRepositoryWith(users);
	}

}
