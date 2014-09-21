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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.books.DeleteBooksCommand;
import pl.jojczykp.bookstore.services.books.DeleteBookService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.DELETE_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DELETE;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DISPLAY;

@Controller
public class DeleteBookController {

	@Autowired private DeleteBookService deleteBookService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = URL_ACTION_DELETE, method = POST)
	public RedirectView delete(
			@ModelAttribute(DELETE_BOOKS_COMMAND) DeleteBooksCommand deleteBooksCommand,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request)
	{
		DisplayBooksCommand displayBooksCommand = deleteBookService.delete(deleteBooksCommand);

		redirectAttributes.addFlashAttribute(DISPLAY_BOOKS_COMMAND, displayBooksCommand);
		return new RedirectView(request.getContextPath() + URL_ACTION_DISPLAY);
	}

}
