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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.jojczykp.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.commands.BooksCommand;
import pl.jojczykp.bookstore.validators.BooksSetPageSizeValidator;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controllers.BooksConsts.BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_GO_TO_PAGE;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_READ;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_SET_PAGE_SIZE;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_SORT;

@Controller
public class BooksControllerPager {

	@Autowired private BooksSetPageSizeValidator booksSetPageSizeValidator;

	@Value("${view.books.defaultPageSize}") private int defaultPageSize;

	@RequestMapping(value = URL_ACTION_SORT, method = POST)
	public RedirectView sort(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		return redirectToRead(booksCommand, redirectAttributes);
	}

	@RequestMapping(value = URL_ACTION_SET_PAGE_SIZE, method = POST)
	public RedirectView setPageSize(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult)
	{
		booksSetPageSizeValidator.validate(booksCommand, bindingResult);
		if (bindingResult.hasErrors()) {
			processWhenCommandInvalid(booksCommand, bindingResult);
		} else {
			processWhenCommandValid(booksCommand);
		}

		return redirectToRead(booksCommand, redirectAttributes);
	}

	private void processWhenCommandInvalid(BooksCommand booksCommand, BindingResult bindingResult) {
		booksCommand.getPager().setPageSize(defaultPageSize);
		for(ObjectError error: bindingResult.getAllErrors()) {
			booksCommand.getMessages().addErrors(error.getDefaultMessage());
		}
	}

	private void processWhenCommandValid(BooksCommand booksCommand) {
		booksCommand.getMessages().addInfos("Page size changed.");
	}

	@RequestMapping(value = URL_ACTION_GO_TO_PAGE, method = POST)
	public RedirectView goToPage(
			@ModelAttribute(BOOKS_COMMAND) BooksCommand booksCommand,
			RedirectAttributes redirectAttributes)
	{
		return redirectToRead(booksCommand, redirectAttributes);
	}

	private RedirectView redirectToRead(BooksCommand booksCommand, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(BOOKS_COMMAND, booksCommand);
		return new RedirectView(URL_ACTION_READ);
	}

}
