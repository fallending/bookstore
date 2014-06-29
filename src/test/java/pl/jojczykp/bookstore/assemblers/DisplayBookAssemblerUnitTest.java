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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jojczykp.bookstore.commands.books.DisplayBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookBuilder.aBook;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;

@RunWith(MockitoJUnitRunner.class)
public class DisplayBookAssemblerUnitTest {

	private static final String KNOWN_FILE_TYPE = "txt";
	private static final String OTHER_KNOWN_FILE_TYPE = "pdf";
	private static final String UNKNOWN_FILE_TYPE = "someUnknownFileType";

	@InjectMocks private DisplayBookAssembler testee;

	@Test
	public void shouldAssemblyBookCommandObjectsListFromDomainObjectsList() {
		List<Book> domains = aDomainObjectsList();

		List<DisplayBookCommand> commands = testee.toCommands(domains);

		assertThat(commands.size(), equalTo(domains.size()));
		for (int i = 0; i < domains.size(); i++) {
			assertThatHaveEqualBookData(domains.get(i), commands.get(i));
		}
	}

	@Test
	public void shouldSetDefaultIconForUnknownFileType() {
		Book domain = aBook().withBookFile(aBookFile().withId(0).withFileType(UNKNOWN_FILE_TYPE).build()).build();

		List<DisplayBookCommand> commands = testee.toCommands(asList(domain));

		assertThat(commands.get(0).getIconName(), is(equalTo("unknown")));
	}

	private List<Book> aDomainObjectsList() {
		return asList(
				aBook().withId(1).withVersion(0).withTitle("A Title 001")
						.withBookFile(aBookFile().withId(0).withFileType(KNOWN_FILE_TYPE).build())
						.build(),
				aBook().withId(2).withVersion(1).withTitle("A Title 002")
						.withBookFile(aBookFile().withId(1).withFileType(OTHER_KNOWN_FILE_TYPE).build())
						.build());
	}

	private void assertThatHaveEqualBookData(Book domain, DisplayBookCommand command) {
		assertThat(domain.getId(), equalTo(command.getId()));
		assertThat(domain.getVersion(), equalTo(command.getVersion()));
		assertThat(domain.getTitle(), equalTo(command.getTitle()));
		assertThat(domain.getBookFile().getFileType(), is(equalTo(command.getIconName())));
	}

}
