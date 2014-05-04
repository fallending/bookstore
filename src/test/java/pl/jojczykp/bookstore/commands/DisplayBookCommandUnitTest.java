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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DisplayBookCommandUnitTest {

	private DisplayBookCommand testee;

	@Before
	public void setupInstance() {
		testee = new DisplayBookCommand();
	}

	@Test
	public void shouldSetId() {
		final int id = 2;

		testee.setId(id);

		assertThat(testee.getId(), equalTo(id));
	}

	@Test
	public void shouldSetVersion() {
		final int version = 54;

		testee.setVersion(version);

		assertThat(testee.getVersion(), equalTo(version));
	}

	@Test
	public void shouldSetTitle() {
		final String title = "some title";

		testee.setTitle(title);

		assertThat(testee.getTitle(), equalTo(title));
	}

}
