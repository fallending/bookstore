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

package pl.jojczykp.bookstore.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;

import java.util.ArrayList;
import java.util.List;

import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;

@Service
@ManagedResource(
		objectName="bookstore.mbeans:name=BooksManagementBean",
		description="Books Management Bean")
public class BooksManagementBean {

	@Autowired private BooksRepository booksRepository;

	@ManagedOperation(description="Get total number of books")
	public int totalCount() {
		return booksRepository.totalCount();
	}

	@ManagedOperation(description="Read list of books")
	@ManagedOperationParameters( {
			@ManagedOperationParameter(name = "offset", description = "Start offset (min=0)"),
			@ManagedOperationParameter(name = "size", description = "Number of books to return")
	})
	public List<String> read(int offset, int size) {
		List<String> result = new ArrayList<>();
		for(Book book: booksRepository.read(offset, size, BOOK_TITLE, ASC)) {
			result.add(book.toString());
		}

		return result;
	}

	@ManagedOperation(description="Update title of book with given id")
	@ManagedOperationParameters({
			@ManagedOperationParameter(name = "id", description = "Id of book to update"),
			@ManagedOperationParameter(name = "version", description = "New version of book (optimistic locking)"),
			@ManagedOperationParameter(name = "title", description = "New title")
	})
	public void update(int id, int version, String title) {
		Book book = new Book();
		book.setId(id);
		book.setVersion(version);
		book.setTitle(title);

		booksRepository.update(book);
	}

	@ManagedOperation(description="Delete book of given id")
	@ManagedOperationParameters({
			@ManagedOperationParameter(name = "id", description = "Id of book to delete")
	})
	public void delete(int id) {
		booksRepository.delete(id);
	}

}
