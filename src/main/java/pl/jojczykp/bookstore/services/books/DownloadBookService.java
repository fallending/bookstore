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
import pl.jojczykp.bookstore.commands.books.DownloadBookCommand;
import pl.jojczykp.bookstore.controllers.errors.ResourceNotFoundException;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

@Service
public class DownloadBookService {

	@Autowired private BooksRepository booksRepository;

	public Book download(DownloadBookCommand downloadBookCommand) {
		try {
			Book book = booksRepository.find(parseInt(downloadBookCommand.getId()));
			verifyBookFound(downloadBookCommand.getId(), book);
			return book;
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException(exceptionMessageFor(downloadBookCommand.getId()), e);
		}
	}

	private void verifyBookFound(String id, Book book) {
		if (book == null) {
			throw new ResourceNotFoundException(exceptionMessageFor(id));
		}
	}

	private String exceptionMessageFor(String id) {
		return format("Content of book with id '%s' not found.", id);
	}

}
