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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.services.books.DisplayBooksService;
import pl.jojczykp.bookstore.utils.BooksCommandFactory;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_VIEW;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DISPLAY;

@Controller
public class DisplayBooksController {

	@Autowired private BooksCommandFactory booksCommandFactory;
	@Autowired private DisplayBooksService displayBooksService;

	@ModelAttribute(DISPLAY_BOOKS_COMMAND)
	public DisplayBooksCommand getDefaultCommand() {
		return booksCommandFactory.create();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = URL_ACTION_DISPLAY, method = GET)
	public ModelAndView display(
			@ModelAttribute(DISPLAY_BOOKS_COMMAND) DisplayBooksCommand displayBooksCommand)
	{
		DisplayBooksCommand resultDisplayBooksCommand = displayBooksService.display(displayBooksCommand);

		return new ModelAndView(DISPLAY_BOOKS_VIEW, aModelFor(resultDisplayBooksCommand));
	}

	private ModelMap aModelFor(DisplayBooksCommand displayBooksCommand) {
		return new ModelMap().addAttribute(DISPLAY_BOOKS_COMMAND, displayBooksCommand);
	}

}
