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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.assemblers.DisplayBookAssembler;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.parts.PagerCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.utils.BooksCommandFactory;
import pl.jojczykp.bookstore.utils.PagerLimiter;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.consts.BooksConsts.DISPLAY_BOOKS_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.BOOKS_VIEW;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_LIST;

@Controller
public class DisplayBooksController {

	@Autowired private BooksCommandFactory booksCommandFactory;
	@Autowired private PagerLimiter pagerLimiter;
	@Autowired private BooksRepository booksRepository;
	@Autowired private DisplayBookAssembler displayBookAssembler;

	@ModelAttribute(DISPLAY_BOOKS_COMMAND)
	public DisplayBooksCommand getDefaultCommand() {
		return booksCommandFactory.create();
	}

	@RequestMapping(value = URL_ACTION_LIST, method = GET)
	public ModelAndView display(
			@ModelAttribute(DISPLAY_BOOKS_COMMAND) DisplayBooksCommand displayBooksCommand)
	{
		PagerCommand limitedPager = pagerLimiter.createLimited(
														displayBooksCommand.getPager(), booksRepository.totalCount());
		displayBooksCommand.setPager(limitedPager);

		List<Book> books = read(displayBooksCommand.getPager());
		displayBooksCommand.setBooks(displayBookAssembler.toCommands(books));

		return new ModelAndView(BOOKS_VIEW, aModelFor(displayBooksCommand));
	}

	private List<Book> read(PagerCommand pager) {
		int pageSize = pager.getPageSize();
		int pageNumber = pager.getPageNumber();
		int offset = (pageNumber - 1) * pageSize;

		return booksRepository.read(
					offset,
					pageSize,
					pager.getSorter().getColumn(),
					pager.getSorter().getDirection());
	}

	private ModelMap aModelFor(DisplayBooksCommand displayBooksCommand) {
		return new ModelMap().addAttribute(DISPLAY_BOOKS_COMMAND, displayBooksCommand);
	}

}
