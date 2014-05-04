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

package pl.jojczykp.bookstore.controllers.books;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.assemblers.UpdateBookAssembler;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.BooksUpdateValidator;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.UPDATE_BOOK_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_LIST;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_UPDATE;

@Controller
public class UpdateBookController {

	@Autowired private BooksUpdateValidator booksUpdateValidator;
	@Autowired private BooksRepository booksRepository;
	@Autowired private UpdateBookAssembler updateBookAssembler;

	@RequestMapping(value = URL_ACTION_UPDATE, method = POST)
	public RedirectView update(
			@ModelAttribute(UPDATE_BOOK_COMMAND) UpdateBookCommand updateBookCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult)
	{
		booksUpdateValidator.validate(updateBookCommand, bindingResult);

		DisplayBooksCommand displayBooksCommand;
		if (bindingResult.hasErrors()) {
			displayBooksCommand = processWhenCommandInvalid(updateBookCommand, bindingResult);
		} else {
			displayBooksCommand = processWhenCommandValid(updateBookCommand);
		}

		return redirectToRead(displayBooksCommand, redirectAttributes);

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
			displayBooksCommand.getMessages().addInfos("Object updated.");
		} catch (StaleObjectStateException e) {
			displayBooksCommand.getMessages().addWarns(
					"Object updated or deleted by another user. Please try again with actual data.");
		}

		return displayBooksCommand;
	}

	private RedirectView redirectToRead(
								DisplayBooksCommand displayBooksCommand, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(DISPLAY_BOOKS_COMMAND, displayBooksCommand);
		return new RedirectView(URL_ACTION_LIST);
	}

}
