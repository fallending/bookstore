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

package pl.jojczykp.bookstore.commands;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpErrorCommandTest {

	private HttpErrorCommand testee;

	@Before
	public void setUpTestee() {
		testee = new HttpErrorCommand();
	}

	@Test
	public void shouldSetId() {
		final int id = 8;

		testee.setId(id);

		assertThat(testee.getId(), is(id));
	}

	@Test
	public void shouldSetOriginalUrl() {
		final String originalUrl = "originalUrl";

		testee.setOriginalUrl(originalUrl);

		assertThat(testee.getOriginalUrl(), is(equalTo(originalUrl)));
	}

	@Test
	public void shouldSetDescription() {
		final String description = "description";

		testee.setDescription(description);

		assertThat(testee.getDescription(), is(equalTo(description)));
	}

}
