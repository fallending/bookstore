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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jojczykp.bookstore.commands.books.DisplayBookCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.entities.BookFile;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.testutils.builders.BookBuilder.aBook;

@RunWith(MockitoJUnitRunner.class)
public class DisplayBookAssemblerUnitTest {

	@Mock private BookFile bookFile1;
	@Mock private BookFile bookFile2;

	@InjectMocks private DisplayBookAssembler testee;

	@Test
	public void shouldAssemblyBookCommandObjectsListFromDomainObjectsList() {
		List<Book> domains = aDomainObjectsList();

		List<DisplayBookCommand> commands = testee.toCommands(domains);

		assertThat(commands.size(), equalTo(domains.size()));
		for (int i = 0; i < domains.size(); i++) {
			assertThatHaveEqualData(domains.get(i), commands.get(i));
		}
	}

	private List<Book> aDomainObjectsList() {
		return asList(
				aBook(1, 0, "A Title 001", bookFile1),
				aBook(2, 1, "A Title 002", bookFile2));
	}

	private void assertThatHaveEqualData(Book domain, DisplayBookCommand command) {
		assertThat(domain.getId(), equalTo(command.getId()));
		assertThat(domain.getVersion(), equalTo(command.getVersion()));
		assertThat(domain.getTitle(), equalTo(command.getTitle()));
		assertThat(domain.getBookFile().getFileType(), is(equalTo(command.getIconName())));
	}

}
