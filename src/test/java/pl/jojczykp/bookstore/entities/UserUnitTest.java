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

package pl.jojczykp.bookstore.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserUnitTest {

	private static final int ID = 3;
	private static final String NAME = "some_name";
	private static final String PASSWORD = "some_password";
	private static final Set<Authority> AUTHORITIES = new HashSet<>();

	private User testee;

	@Before
	public void setupTestee() {
		testee = new User();
	}

	@Test
	public void shouldHaveDefaultConstructorForHibernate() {
		assertThat(testee.getId(), is(0));
		assertThat(testee.getName(), is(""));
		assertThat(testee.isNotExpired(), is(false));
		assertThat(testee.isNotLocked(), is(false));
		assertThat(testee.isCredentialsNotExpired(), is(false));
		assertThat(testee.isEnabled(), is(false));
		assertThat(testee.getAuthorities(), is(notNullValue()));
	}

	@Test
	public void shouldSetId() {
		testee.setId(ID);

		assertThat(testee.getId(), is(ID));
	}

	@Test
	public void shouldSetName() {
		testee.setName(NAME);

		assertThat(testee.getName(), is(NAME));
	}

	@Test
	public void shouldSetPassword() {
		testee.setPassword(PASSWORD);

		assertThat(testee.getPassword(), is(PASSWORD));
	}

	@Test
	public void shouldSetNotExpired() {
		testee.setNotExpired(true);

		assertThat(testee.isNotExpired(), is(true));
	}

	@Test
	public void shouldSetNotLocked() {
		testee.setNotLocked(true);

		assertThat(testee.isNotLocked(), is(true));
	}

	@Test
	public void shouldSetCredentialsNotExpired() {
		testee.setCredentialsNotExpired(true);

		assertThat(testee.isCredentialsNotExpired(), is(true));
	}

	@Test
	public void shouldSetEnabled() {
		testee.setEnabled(true);

		assertThat(testee.isEnabled(), is(true));
	}

	@Test
	public void shouldSetPermissions() {
		testee.setAuthorities(AUTHORITIES);

		assertThat(testee.getAuthorities(), is(sameInstance(AUTHORITIES)));
	}

	@Test
	public void shouldMeetEqualsHashCodeContract() {
		EqualsVerifier.forClass(User.class)
				.usingGetClass()
				.verify();
	}

	@Test
	public void shouldHaveToStringWithDetails() {
		testee.setId(ID);
		testee.setName(NAME);
		testee.setPassword(PASSWORD);

		String toStringResult = testee.toString();

		assertThat(toStringResult, equalTo(
				format("%s{id=%d, name='%s', password='%s', notExpired='false', notLocked='false', " +
								"credentialsNotExpired='false', enabled='false', authorities=[]}",
						testee.getClass().getSimpleName(), ID, NAME, PASSWORD)));
	}

}
