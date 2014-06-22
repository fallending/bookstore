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

package pl.jojczykp.bookstore.entities.builders;

import pl.jojczykp.bookstore.entities.BookFile;

import static pl.jojczykp.bookstore.utils.BlobUtils.aSerialBlobWith;

public class BookFileBuilder {

	private BookFile template = new BookFile();

	public static BookFileBuilder aBookFile() {
		return new BookFileBuilder();
	}

	public BookFileBuilder withId(int id) {
		template.setId(id);
		return this;
	}

	public BookFileBuilder withFileType(String fileType) {
		template.setFileType(fileType);
		return this;
	}

	public BookFileBuilder withContentType(String contentType) {
		template.setContentType(contentType);
		return this;
	}

	public BookFileBuilder withContent(byte[] content) {
		template.setContentLength(content.length);
		template.setContent(aSerialBlobWith(content));
		return this;
	}

	public BookFile build() {
		BookFile constructed = new BookFile();
		constructed.setId(template.getId());
		constructed.setFileType(template.getFileType());
		constructed.setContentType(template.getContentType());
		constructed.setContentLength(template.getContentLength());
		constructed.setContent(template.getContent());

		return constructed;
	}

}
