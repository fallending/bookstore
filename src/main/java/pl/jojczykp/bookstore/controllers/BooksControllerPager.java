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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.jojczykp.bookstore.commands.ListBooksCommand;
import pl.jojczykp.bookstore.commands.ChangePagerCommand;
import pl.jojczykp.bookstore.validators.BooksSetPageSizeValidator;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.controllers.BooksConsts.LIST_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.controllers.BooksConsts.CHANGE_PAGER_COMMAND;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_GO_TO_PAGE;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_LIST;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_SET_PAGE_SIZE;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_SORT;

@Controller
public class BooksControllerPager {

	@Autowired private BooksSetPageSizeValidator booksSetPageSizeValidator;

	@Value("${view.books.defaultPageSize}") private int defaultPageSize;

	@RequestMapping(value = URL_ACTION_SORT, method = POST)
	public RedirectView sort(
			@ModelAttribute(CHANGE_PAGER_COMMAND) ChangePagerCommand changePagerCommand,
			RedirectAttributes redirectAttributes)
	{
		ListBooksCommand listBooksCommand = new ListBooksCommand();
		listBooksCommand.setPager(changePagerCommand.getPager());

		return redirectToRead(listBooksCommand, redirectAttributes);
	}

	@RequestMapping(value = URL_ACTION_SET_PAGE_SIZE, method = POST)
	public RedirectView setPageSize(
			@ModelAttribute(CHANGE_PAGER_COMMAND) ChangePagerCommand changePagerCommand,
			RedirectAttributes redirectAttributes,
			BindingResult bindingResult)
	{
		booksSetPageSizeValidator.validate(changePagerCommand, bindingResult);

		ListBooksCommand listBooksCommand;
		if (bindingResult.hasErrors()) {
			listBooksCommand = processWhenSetPageSizeCommandInvalid(changePagerCommand, bindingResult);
		} else {
			listBooksCommand = processWhenSetPageSizeCommandValid(changePagerCommand);
		}

		return redirectToRead(listBooksCommand, redirectAttributes);
	}

	private ListBooksCommand processWhenSetPageSizeCommandInvalid(
							ChangePagerCommand changePagerCommand, BindingResult bindingResult) {
		ListBooksCommand listBooksCommand = new ListBooksCommand();
		listBooksCommand.setPager(changePagerCommand.getPager());

		listBooksCommand.getPager().setPageSize(defaultPageSize);
		for(ObjectError error: bindingResult.getAllErrors()) {
			listBooksCommand.getMessages().addErrors(error.getDefaultMessage());
		}

		return listBooksCommand;
	}

	private ListBooksCommand processWhenSetPageSizeCommandValid(ChangePagerCommand changePagerCommand) {
		ListBooksCommand listBooksCommand = new ListBooksCommand();
		listBooksCommand.setPager(changePagerCommand.getPager());

		listBooksCommand.getMessages().addInfos("Page size changed.");

		return listBooksCommand;
	}

	@RequestMapping(value = URL_ACTION_GO_TO_PAGE, method = POST)
	public RedirectView goToPage(
			@ModelAttribute(CHANGE_PAGER_COMMAND) ChangePagerCommand changePagerCommand,
			RedirectAttributes redirectAttributes)
	{
		ListBooksCommand listBooksCommand = new ListBooksCommand();
		listBooksCommand.setPager(changePagerCommand.getPager());

		return redirectToRead(listBooksCommand, redirectAttributes);
	}

	private RedirectView redirectToRead(ListBooksCommand listBooksCommand, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(LIST_BOOKS_COMMAND, listBooksCommand);
		return new RedirectView(URL_ACTION_LIST);
	}

}
