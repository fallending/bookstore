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

package pl.jojczykp.bookstore.assemblers;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class CreateBookAssemblerUnitTest {

	private static final int ID_TO_BE_SET_AUTOMATICALLY = 0;
	private static final int VERSION_TO_BE_SET_AUTOMATICALLY = 0;

	private static final String TITLE = "A Title";

	private CreateBookAssembler testee;

	@Before
	public void setUpTestee() {
		testee = new CreateBookAssembler();
	}

	@Test
	public void shouldAssemblySingleBookDomainObjectFromCreateBookCommandObject() {
		CreateBookCommand command = aCreateBookCommand();

		Book domain = testee.toDomain(command);

		assertThat(domain.getId(), is(ID_TO_BE_SET_AUTOMATICALLY));
		assertThat(domain.getVersion(), is(VERSION_TO_BE_SET_AUTOMATICALLY));
		assertThat(domain.getTitle(), is(equalTo(command.getTitle())));
	}

	private CreateBookCommand aCreateBookCommand() {
		CreateBookCommand command = new CreateBookCommand();
		command.setTitle(TITLE);

		return command;
	}

}
