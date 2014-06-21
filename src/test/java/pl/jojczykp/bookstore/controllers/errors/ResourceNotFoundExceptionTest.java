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

package pl.jojczykp.bookstore.controllers.errors;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class ResourceNotFoundExceptionTest {

	@Test
	public void shouldHaveConstructorWithMessage() {
		final String message = "some message";

		ResourceNotFoundException exception = new ResourceNotFoundException(message);

		assertThat(exception.getMessage(), is(equalTo(message)));
	}

	@Test
	public void shouldHaveConstructorWithMessageAndCause() {
		final Throwable cause = new Exception("cause exception message");
		final String message = "some message";

		ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

		assertThat(exception.getMessage(), is(equalTo(message)));
		assertThat(exception.getCause(), is(sameInstance(cause)));
	}

}
