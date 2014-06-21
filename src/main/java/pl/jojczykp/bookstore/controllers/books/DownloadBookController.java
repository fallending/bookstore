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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jojczykp.bookstore.commands.books.DownloadBookCommand;
import pl.jojczykp.bookstore.controllers.errors.ResourceNotFoundException;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.transfers.BookTO;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.consts.BooksConsts.DOWNLOAD_BOOK_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DOWNLOAD;

@Controller
public class DownloadBookController {

	@Autowired private BooksRepository booksRepository;

	@RequestMapping(value = URL_ACTION_DOWNLOAD, method = GET)
	@ResponseBody
	public ResponseEntity<byte[]> downloadBook(
			@ModelAttribute(DOWNLOAD_BOOK_COMMAND) DownloadBookCommand downloadBookCommand)
	{
		try {
			BookTO book = booksRepository.find(parseInt(downloadBookCommand.getId()));
			return tryReturnBookForParsableId(downloadBookCommand.getId(), book);
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException(exceptionMessageFor(downloadBookCommand.getId()), e);
		}

	}

	private ResponseEntity<byte[]> tryReturnBookForParsableId(String id, BookTO book) {
		if (book != null) {
			return new ResponseEntity<>(book.getContent().toByteArray(), responseHeadersFor(book), HttpStatus.OK);
		} else {
			throw new ResourceNotFoundException(exceptionMessageFor(id));
		}
	}

	private String exceptionMessageFor(String id) {
		return format("Content of book with id '%s' not found.", id);
	}

	private HttpHeaders responseHeadersFor(BookTO book) {
		HttpHeaders responseHeaders = new HttpHeaders();

		responseHeaders.setContentType(parseMediaType(book.getContentType()));
		responseHeaders.setContentLength(book.getContent().size());
		responseHeaders.add("Content-Disposition", "attachment; filename=\"" + fileNameFor(book) + "\"");

		return responseHeaders;
	}

	private String fileNameFor(BookTO book) {
		return book.getTitle() + "." + book.getFileType();
	}

}
