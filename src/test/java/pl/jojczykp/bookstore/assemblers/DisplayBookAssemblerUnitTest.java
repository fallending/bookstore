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
import pl.jojczykp.bookstore.commands.DisplayBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.testutils.builders.BookBuilder.aBook;

public class DisplayBookAssemblerUnitTest {

	private static final int ID1 = 34;
	private static final int VERSION1 = 45;
	private static final String TITLE1 = "A Title 001";

	private static final int ID2 = 54;
	private static final int VERSION2 = 89;
	private static final String TITLE2 = "A Title 002";

	private DisplayBookAssembler testee;

	@Before
	public void setUpTestee() {
		testee = new DisplayBookAssembler();
	}

	@Test
	public void shouldAssemblyBookCommandObjectsListFromDomainObjectsList() {
		List<Book> domains = aDomainObjectsList();

		List<DisplayBookCommand> commands = testee.toCommands(domains);

		assertThat(commands.size(), equalTo(domains.size()));
		for (int i = 0; i < domains.size(); i++) {
			assertThatHaveEqualData(domains.get(i), commands.get(i));
		}
	}

	@Test
	public void shouldAssemblySingleBookDomainObjectFromBookCommandObject() {
		DisplayBookCommand command = aDisplayBookCommand();

		Book domain = testee.toDomain(command);

		assertThatHaveEqualData(domain, command);
	}

	private List<Book> aDomainObjectsList() {
		return asList(
				aBook(ID1, VERSION1, TITLE1),
				aBook(ID2, VERSION2, TITLE2));
	}

	private DisplayBookCommand aDisplayBookCommand() {
		DisplayBookCommand command = new DisplayBookCommand();
		command.setId(ID1);
		command.setVersion(VERSION1);
		command.setTitle(TITLE1);

		return command;
	}

	private void assertThatHaveEqualData(Book domain, DisplayBookCommand command) {
		assertThat(domain.getId(), equalTo(command.getId()));
		assertThat(domain.getVersion(), equalTo(command.getVersion()));
		assertThat(domain.getTitle(), equalTo(command.getTitle()));
	}

}
