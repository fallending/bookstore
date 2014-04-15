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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.jojczykp.bookstore.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.jojczykp.bookstore.entities.Authority;
import pl.jojczykp.bookstore.entities.User;
import pl.jojczykp.bookstore.repositories.SecurityRepository;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static pl.jojczykp.bookstore.testutils.builders.AuthorityBuilder.anAuthority;
import static pl.jojczykp.bookstore.testutils.builders.UserBuilder.anUser;

@RunWith(MockitoJUnitRunner.class)
public class CustomDbAuthenticationProviderUnitTest {

	private static final User USER = anUser().withId(1)
			.withName("a_user_name").withPassword("password_1")
			.withNotExpired(true).withNotLocked(false).withCredentialsNotExpired(true).withEnabled(false)
			.withAuthorities(anAuthority(2, "SOME_ROLE"))
			.build();

	@Captor private ArgumentCaptor<String> usernameCaptor;
	@Mock private SecurityRepository securityRepository;

	@InjectMocks private CustomDbAuthenticationProvider testee;

	@Test
	public void shouldLoadUserByUsername() {
		givenRepositoryWith(USER);

		UserDetails userDetails = testee.loadUserByUsername(USER.getName());

		assertThatReadFromRepository(USER);
		assertThatSimplePropertiesAreEqual(userDetails, USER);
		assertThatRolesAreEqual(userDetails);
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldFailWhenAttemptingToLoadNotExistingUser() {
		givenRepositoryWith(USER);

		testee.loadUserByUsername("not" + USER.getName());
	}

	@Test(expected = DataAccessException.class)
	public void shouldRethrowDataAccessExceptionFromRepository() {
		givenBrokenRepository();

		testee.loadUserByUsername(USER.getName());
	}

	private void givenRepositoryWith(User user) {
		given(securityRepository.findByName(user.getName())).willReturn(user);
	}


	private void givenBrokenRepository() {
		given(securityRepository.findByName(anyString())).willThrow(new DataAccessException("") { });
	}

	private void assertThatReadFromRepository(User user) {
		verify(securityRepository).findByName(usernameCaptor.capture());
		assertThat(usernameCaptor.getValue(), is(equalTo(user.getName())));
		verifyNoMoreInteractions(securityRepository);
	}

	private void assertThatSimplePropertiesAreEqual(UserDetails userDetails, User user) {
		assertThat(userDetails.getUsername(), is(equalTo(user.getName())));
		assertThat(userDetails.getPassword(), is(equalTo(user.getPassword())));
		assertThat(userDetails.isAccountNonExpired(), is(equalTo(user.isNotExpired())));
		assertThat(userDetails.isAccountNonLocked(), is(equalTo(user.isNotLocked())));
		assertThat(userDetails.isCredentialsNonExpired(), is(equalTo(user.isCredentialsNotExpired())));
		assertThat(userDetails.isEnabled(), is(equalTo(user.isEnabled())));
	}

	private void assertThatRolesAreEqual(UserDetails userDetails) {
		Set<String> givenRoles = getRolesFrom(USER);
		Set<String> foundRoles = getRolesFrom(userDetails);

		assertThat(foundRoles, is(equalTo(givenRoles)));
	}

	private Set<String> getRolesFrom(User user) {
		Set<String> givenRoles = new LinkedHashSet<>();
		for (Authority authority: user.getAuthorities()) {
			givenRoles.add(authority.getRole());
		}

		return givenRoles;
	}

	private Set<String> getRolesFrom(UserDetails userDetails) {
		Set<String> foundRoles = new LinkedHashSet<>();
		for (GrantedAuthority generatedAuthority: userDetails.getAuthorities()) {
			foundRoles.add(generatedAuthority.getAuthority());
		}

		return foundRoles;
	}

}
