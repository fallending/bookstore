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

package pl.jojczykp.bookstore.commands.errors;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ExceptionCommandUnitTest {

	private ExceptionCommand testee;

	@Before
	public void setUpTestee() {
		testee = new ExceptionCommand();
	}

	@Test
	public void shouldSetMessage() {
		final String message = "some message";

		testee.setMessage(message);

		assertThat(testee.getMessage(), equalTo(message));
	}

	@Test
	public void shouldSetStackTraceAsText() {
		final String stackTraceContent = "stack trace content";

		testee.setStackTraceAsString(stackTraceContent);

		assertThat(testee.getStackTraceAsString(), equalTo(stackTraceContent));
	}
}
