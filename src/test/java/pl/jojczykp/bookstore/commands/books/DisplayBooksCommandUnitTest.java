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

package pl.jojczykp.bookstore.commands.books;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.commands.parts.MessagesCommand;
import pl.jojczykp.bookstore.commands.parts.PagerCommand;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class DisplayBooksCommandUnitTest {

	private DisplayBooksCommand testee;

	@Before
	public void createTestee() {
		testee = new DisplayBooksCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getMessages(), notNullValue());
		assertThat(testee.getPager(), notNullValue());
		assertThat(testee.getBooks(), empty());
	}

	@Test
	public void shouldSetMessages() {
		final MessagesCommand messages = new MessagesCommand();

		testee.setMessages(messages);

		assertThat(testee.getMessages(), sameInstance(messages));
	}

	@Test
	public void shouldSetPager() {
		final PagerCommand pager = new PagerCommand();

		testee.setPager(pager);

		assertThat(testee.getPager(), sameInstance(pager));
	}

	@Test
	public void shouldSetBooks() {
		final List<DisplayBookCommand> books = new ArrayList<>();

		testee.setBooks(books);

		assertThat(testee.getBooks(), sameInstance(books));
	}

}
