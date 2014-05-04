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

package pl.jojczykp.bookstore.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.ListBooksCommand;

@Service
public class BooksCommandFactory {

	@Value("${view.books.defaultPageNumber}") private int defaultPageNumber;
	@Value("${view.books.defaultPageSize}") private int defaultPageSize;
	@Value("${view.books.defaultSortColumn}") private PageSorterColumn defaultSortColumn;
	@Value("${view.books.defaultSortDirection}") private PageSorterDirection defaultSortDirection;

	public ListBooksCommand create() {
		ListBooksCommand listBooksCommand = new ListBooksCommand();

		listBooksCommand.getPager().setPageNumber(defaultPageNumber);
		listBooksCommand.getPager().setPageSize(defaultPageSize);
		listBooksCommand.getPager().getSorter().setColumn(defaultSortColumn);
		listBooksCommand.getPager().getSorter().setDirection(defaultSortDirection);

		return listBooksCommand;
	}
}
