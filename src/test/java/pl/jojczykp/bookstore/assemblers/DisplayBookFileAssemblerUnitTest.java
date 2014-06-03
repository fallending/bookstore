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
import pl.jojczykp.bookstore.commands.books.DisplayBookFileCommand;
import pl.jojczykp.bookstore.entities.BookFile;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DisplayBookFileAssemblerUnitTest {

	private static final int ID = 65;
	private static final String CONTENT_TYPE = "content/type";

	private DisplayBookFileAssembler testee;

	@Before
	public void setUpTestee() {
		testee = new DisplayBookFileAssembler();
	}

	@Test
	public void shouldAssemblyBookFileCommandObjectFromBookFileDomainObject() {
		BookFile domain = aBookFile();

		DisplayBookFileCommand command = testee.toCommand(domain);

		assertThat(command.getId(), equalTo(domain.getId()));
		assertThat(command.getIconName(), equalTo(toIconName(domain)));
	}

	private String toIconName(BookFile domain) {
		return domain.getContentType().replace('/', '_');
	}

	private BookFile aBookFile() {
		BookFile domain = new BookFile();
		domain.setId(ID);
		domain.setContentType(CONTENT_TYPE);

		return domain;
	}

}
