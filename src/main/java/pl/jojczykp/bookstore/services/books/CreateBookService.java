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

package pl.jojczykp.bookstore.services.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.jojczykp.bookstore.assemblers.CreateBookAssembler;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.CreateBookValidator;

@Service
public class CreateBookService {

	@Autowired private CreateBookValidator createBookValidator;
	@Autowired private CreateBookAssembler createBookAssembler;
	@Autowired private BooksRepository booksRepository;

	public DisplayBooksCommand create(CreateBookCommand createBookCommand, BindingResult bindingResult) {
		createBookValidator.validate(createBookCommand, bindingResult);

		if (bindingResult.hasErrors()) {
			return processWhenCommandInvalid(createBookCommand, bindingResult);
		} else {
			return processWhenCommandValid(createBookCommand);
		}
	}

	private DisplayBooksCommand processWhenCommandInvalid(
										CreateBookCommand createBookCommand, BindingResult bindingResult) {
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(createBookCommand.getPager());

		for (ObjectError error: bindingResult.getAllErrors()) {
			displayBooksCommand.getMessages().addErrors(error.getDefaultMessage());
		}

		return displayBooksCommand;
	}

	private DisplayBooksCommand processWhenCommandValid(CreateBookCommand createBookCommand) {
		booksRepository.create(createBookAssembler.toDomain(createBookCommand));

		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(createBookCommand.getPager());
		displayBooksCommand.getMessages().addInfos("Object created.");

		return displayBooksCommand;
	}

}
