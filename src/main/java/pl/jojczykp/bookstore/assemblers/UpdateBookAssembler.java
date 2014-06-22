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

import com.google.common.base.Charsets;
import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;

@Service
public class UpdateBookAssembler {

	public Book toDomain(UpdateBookCommand command) {
		Book domain = new Book();
		domain.setId(command.getId());
		domain.setVersion(command.getVersion());
		domain.setTitle(command.getTitle());
		domain.setBookFile(aBookFile()
								.withFileType("txt")
								.withContentType("text/plain; charset=utf-8")
								.withContent("a Book Content".getBytes(Charsets.UTF_8))
								.build());

		return domain;
	}

}
