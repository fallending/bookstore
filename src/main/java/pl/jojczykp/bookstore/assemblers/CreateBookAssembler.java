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

package pl.jojczykp.bookstore.assemblers;

import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import java.io.IOException;
import java.util.Locale;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;

@Service
public class CreateBookAssembler {

	private static final int ID_TO_BE_SET_AUTOMATICALLY = 0;
	private static final int VERSION_TO_BE_SET_AUTOMATICALLY = 0;

	public Book toDomain(CreateBookCommand command) {
		Book domain = new Book();
		domain.setId(ID_TO_BE_SET_AUTOMATICALLY);
		domain.setVersion(VERSION_TO_BE_SET_AUTOMATICALLY);
		domain.setTitle(command.getTitle());
		domain.setBookFile(aBookFile()
				.withFileType(fileExtensionIn(command))
				.withContentType(fileContentTypeIn(command))
				.withContent(fileContentIn(command))
				.build());

		return domain;
	}

	private String fileExtensionIn(CreateBookCommand command) {
		return getExtension(command.getFile().getOriginalFilename()).toLowerCase(Locale.US);
	}

	private String fileContentTypeIn(CreateBookCommand command) {
		return command.getFile().getContentType();
	}

	private byte[] fileContentIn(CreateBookCommand command) {
		try {
			return command.getFile().getBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
