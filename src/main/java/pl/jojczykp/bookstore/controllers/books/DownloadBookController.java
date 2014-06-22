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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jojczykp.bookstore.commands.books.DownloadBookCommand;
import pl.jojczykp.bookstore.controllers.errors.ResourceNotFoundException;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.apache.commons.io.IOUtils.copy;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.consts.BooksConsts.DOWNLOAD_BOOK_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DOWNLOAD;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobInputStream;

@Controller
public class DownloadBookController {

	@Autowired private BooksRepository booksRepository;

	@RequestMapping(value = URL_ACTION_DOWNLOAD, method = GET)
	@Transactional
	public void downloadBook(
			@ModelAttribute(DOWNLOAD_BOOK_COMMAND) DownloadBookCommand downloadBookCommand,
			HttpServletResponse response) throws IOException {
		try {
			Book book = booksRepository.find(parseInt(downloadBookCommand.getId()));
			verifyBookFound(downloadBookCommand.getId(), book);
			setResponse(response, book);
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException(exceptionMessageFor(downloadBookCommand.getId()), e);
		}

	}

	private void verifyBookFound(String id, Book book) {
		if (book == null) {
			throw new ResourceNotFoundException(exceptionMessageFor(id));
		}
	}

	private void setResponse(HttpServletResponse response, Book book) throws IOException {
		setResponseHeaders(response, book);
		setResponseBody(response, book);
	}

	private void setResponseHeaders(HttpServletResponse response, Book book) {
		response.setContentType(book.getBookFile().getContentType());
		response.setContentLength(book.getBookFile().getContentLength());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameFor(book) + "\"");
	}

	private String fileNameFor(Book book) {
		return book.getTitle() + "." + book.getBookFile().getFileType();
	}

	private void setResponseBody(HttpServletResponse response, Book book) throws IOException {
		copy(blobInputStream(book.getBookFile().getContent()), response.getOutputStream());
	}

	private String exceptionMessageFor(String id) {
		return format("Content of book with id '%s' not found.", id);
	}

}
