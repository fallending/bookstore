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

package pl.jojczykp.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.assemblers.CreateBookAssembler;
import pl.jojczykp.bookstore.commands.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.CreateBookCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.BooksCreateValidator;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controllers.BooksConsts.DISPLAY_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controllers.BooksConsts.CREATE_BOOK_COMMAND;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_CREATE;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_LIST;

@Controller
public class CreateBookController {

	@Autowired private BooksCreateValidator booksCreateValidator;
	@Autowired private BooksRepository booksRepository;
	@Autowired private CreateBookAssembler createBookAssembler;

	@RequestMapping(value = URL_ACTION_CREATE, method = POST)
	public RedirectView create(
			@ModelAttribute(CREATE_BOOK_COMMAND) CreateBookCommand createBookCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult)
	{
		booksCreateValidator.validate(createBookCommand, bindingResult);

		DisplayBooksCommand displayBooksCommand;
		if (bindingResult.hasErrors()) {
			displayBooksCommand = processWhenCommandInvalid(createBookCommand, bindingResult);
		} else {
			displayBooksCommand = processWhenCommandValid(createBookCommand);
		}

		return redirectToRead(displayBooksCommand, redirectAttributes);
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
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(createBookCommand.getPager());

		booksRepository.create(createBookAssembler.toDomain(createBookCommand));
		displayBooksCommand.getMessages().addInfos("Object created.");

		return displayBooksCommand;
	}

	private RedirectView redirectToRead(
								DisplayBooksCommand displayBooksCommand, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(DISPLAY_BOOKS_COMMAND, displayBooksCommand);
		return new RedirectView(URL_ACTION_LIST);
	}

}