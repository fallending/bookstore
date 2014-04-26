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

package pl.jojczykp.bookstore.aspects;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LoggingRequestMappingAspectUnitTest {

	private LoggingRequestMappingAspect testee;

	@Before
	public void setupTestee() {
		testee = new LoggingRequestMappingAspect();
	}

	@Test
	public void shouldEnable() {
		testee.enable();

		assertThat(testee.isEnabled(), is(true));
	}

	@Test
	public void shouldDisable() {
		testee.disable();

		assertThat(testee.isEnabled(), is(false));
	}

	@Test
	public void shouldHavePointcutDefinedByMethod() {
		/* For coverage only */
		testee.requestMappingAnnotatedMethod();
	}

}
