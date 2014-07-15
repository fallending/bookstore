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

package pl.jojczykp.bookstore.jmx;


import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;

@RunWith(MockitoJUnitRunner.class)
public class JmxSecurityAuthenticatorUnitTest {

	private static final Set<String> ROLES_GRANTING_ACCESS = newHashSet("ROLE_JMX");
	private static final Set<String> ROLES_NOT_GRANTING_ACCESS = newHashSet("ROLE_OTHER");
	private static final String AUTHENTICATION_EXCEPTION_MSG = "authentication exception";

	@Rule public ExpectedException expectedException = ExpectedException.none();

	@Mock private Authentication authentication;
	@Mock private AuthenticationManager authenticationManager;

	@InjectMocks private JmxSecurityAuthenticator testee;

	@Test
	public void shouldAuthenticateAndAuthorize() {
		final String[] userPass = {"user", "password"};
		givenAuthorities(userPass, ROLES_GRANTING_ACCESS);

		Subject result = testee.authenticate(userPass);

		assertThat(result.getPrincipals(), hasOnlyPrincipals(ROLES_GRANTING_ACCESS));
	}

	@Test
	public void shouldAuthenticateButNotAuthorize() {
		final String[] userPass = {"user", "password"};
		givenAuthorities(userPass, ROLES_NOT_GRANTING_ACCESS);

		expectedException.expect(SecurityException.class);
		expectedException.expectMessage(endsWith("Bad credentials or user has no JMX Authority"));

		testee.authenticate(userPass);
	}

	@Test
	public void shouldNotAuthenticate() {
		final String[] userPass = {"badUser", "badPassword"};
		given(authenticationManager.authenticate(argThat(isTokenWith(userPass))))
				.willThrow(new BadCredentialsException(AUTHENTICATION_EXCEPTION_MSG));

		expectedException.expect(SecurityException.class);
		expectedException.expectMessage(endsWith(AUTHENTICATION_EXCEPTION_MSG));

		testee.authenticate(userPass);
	}

	@SuppressWarnings("unchecked")
	private void givenAuthorities(String[] userPass, Set<String> roles) {
		given(authenticationManager.authenticate(argThat(isTokenWith(userPass)))).willReturn(authentication);
		given(authentication.getAuthorities()).willReturn(authoritiesWithRoles(roles));
	}

	private Collection authoritiesWithRoles(Set<String> rolesGrantingAccess) {
		Set<GrantedAuthority> result = newHashSet();
		for (String role : rolesGrantingAccess) {
			result.add(new SimpleGrantedAuthority(role));
		}

		return result;
	}

	private Matcher<Authentication> isTokenWith(final Object[] credentials) {
		return new CustomTypeSafeMatcher<Authentication>("Credentials equal to " + Arrays.toString(credentials)) {
			@Override
			protected boolean matchesSafely(Authentication item) {
				return (credentials[0].equals(item.getPrincipal()) && credentials[1].equals(item.getCredentials()));
			}
		};
	}

	private Matcher<Set<Principal>> hasOnlyPrincipals(final Set<String> roles) {
		return new CustomTypeSafeMatcher<Set<Principal>>("Principals with " + roles +" roles only") {
			@Override
			protected boolean matchesSafely(Set<Principal> item) {
				return sameSize(item, roles) && allRolesPresent(item, roles);
			}

			private boolean sameSize(Set<Principal> a, Set<String> b) {
				return (a.size() == b.size());
			}

			private boolean allRolesPresent(Set<Principal> principals, Set<String> desiredRoles) {
				for (Principal principal : principals) {
					if (!desiredRoles.contains(principal.getName())) {
						return false;
					}
				}
				return true;
			}
		};
	}

}
