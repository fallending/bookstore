/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

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
