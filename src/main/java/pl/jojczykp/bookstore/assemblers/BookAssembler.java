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

import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.DisplayBookCommand;
import pl.jojczykp.bookstore.commands.CreateBookCommand;
import pl.jojczykp.bookstore.commands.UpdateBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookAssembler {

	private static final int ID_TO_BE_SET_AUTOMATICALLY = 0;
	private static final int VERSION_TO_BE_SET_AUTOMATICALLY = 0;

	public List<DisplayBookCommand> toCommands(List<Book> domains) {
		List<DisplayBookCommand> commands = new ArrayList<>(domains.size());
		for (Book domain : domains) {
			commands.add(toCommand(domain));
		}

		return commands;
	}

	private DisplayBookCommand toCommand(Book domain) {
		DisplayBookCommand command = new DisplayBookCommand();
		command.setId(domain.getId());
		command.setVersion(domain.getVersion());
		command.setTitle(domain.getTitle());

		return command;
	}

	public Book toDomain(DisplayBookCommand command) {
		Book domain = new Book();
		domain.setId(command.getId());
		domain.setVersion(command.getVersion());
		domain.setTitle(command.getTitle());

		return domain;
	}

	public Book toDomain(CreateBookCommand command) {
		Book domain = new Book();
		domain.setId(ID_TO_BE_SET_AUTOMATICALLY);
		domain.setVersion(VERSION_TO_BE_SET_AUTOMATICALLY);
		domain.setTitle(command.getTitle());

		return domain;
	}

	public Book toDomain(UpdateBookCommand command) {
		Book domain = new Book();
		domain.setId(command.getId());
		domain.setVersion(command.getVersion());
		domain.setTitle(command.getTitle());

		return domain;
	}

}
