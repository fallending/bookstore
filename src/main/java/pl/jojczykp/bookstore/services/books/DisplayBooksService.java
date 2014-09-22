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
import pl.jojczykp.bookstore.assemblers.DisplayBookAssembler;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.common.PagerCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.utils.PagerLimiter;

import java.util.List;

@Service
public class DisplayBooksService {

	@Autowired private BooksRepository booksRepository;
	@Autowired private PagerLimiter pagerLimiter;
	@Autowired private DisplayBookAssembler displayBookAssembler;

	public DisplayBooksCommand display(DisplayBooksCommand displayBooksCommand) {
		PagerCommand limitedPager = pagerLimiter.createLimited(
														displayBooksCommand.getPager(), booksRepository.totalCount());
		displayBooksCommand.setPager(limitedPager);

		List<Book> books = read(displayBooksCommand.getPager());
		displayBooksCommand.setBooks(displayBookAssembler.toCommands(books));

		return displayBooksCommand;
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

}
