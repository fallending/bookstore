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

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.jojczykp.bookstore.assemblers.UpdateBookAssembler;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.UpdateBookValidator;

@Service
public class UpdateBookService {

	@Autowired private UpdateBookValidator updateBookValidator;
	@Autowired private UpdateBookAssembler updateBookAssembler;
	@Autowired private BooksRepository booksRepository;

	public DisplayBooksCommand update(UpdateBookCommand updateBookCommand, BindingResult bindingResult) {
		updateBookValidator.validate(updateBookCommand, bindingResult);

		if (bindingResult.hasErrors()) {
			return processWhenCommandInvalid(updateBookCommand, bindingResult);
		} else {
			return processWhenCommandValid(updateBookCommand);
		}
	}

	private DisplayBooksCommand processWhenCommandInvalid(
			UpdateBookCommand updateBookCommand, BindingResult bindingResult) {
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(updateBookCommand.getPager());

		for(ObjectError error: bindingResult.getAllErrors()) {
			displayBooksCommand.getMessages().addErrors(error.getDefaultMessage());
		}

		return displayBooksCommand;
	}

	private DisplayBooksCommand processWhenCommandValid(UpdateBookCommand updateBookCommand) {
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(updateBookCommand.getPager());

		try {
			booksRepository.update(updateBookAssembler.toDomain(updateBookCommand));
			displayBooksCommand.getMessages().addInfos("Title updated.");
		} catch (StaleObjectStateException e) {
			displayBooksCommand.getMessages().addWarns(
					"Object updated or deleted by another user. Please try again with actual data.");
		}

		return displayBooksCommand;
	}

}
