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

package pl.jojczykp.bookstore.testutils.builders;

import com.google.protobuf.ByteString;
import pl.jojczykp.bookstore.entities.BookFile;

import java.sql.Blob;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static pl.jojczykp.bookstore.utils.BlobUtils.aSerialBlobWith;

public abstract class BookFileBuilder {

	public static BookFile aBookFile(int id, String content) {
		return aBookFile(id, "text/plain", aSerialBlobWith(copyFromUtf8(content)));
	}

	public static BookFile aBookFile(int id, String contentType, ByteString content) {
		return aBookFile(id, contentType, aSerialBlobWith(content));
	}

	public static BookFile aBookFile(int id, String contentType, Blob content) {
		BookFile result = new BookFile();
		result.setId(id);
		result.setContentType(contentType);
		result.setContent(content);

		return result;
	}

}
