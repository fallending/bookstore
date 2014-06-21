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

package pl.jojczykp.bookstore.transfers;

import com.google.protobuf.ByteString;

public class BookTO {

	private String title;

	private String fileType;

	private String contentType;

	private ByteString content;

	public BookTO(String title, String fileType, String contentType, ByteString content) {
		this.title = title;
		this.fileType = fileType;
		this.contentType = contentType;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public String getFileType() {
		return fileType;
	}

	public String getContentType() {
		return contentType;
	}

	public ByteString getContent() {
		return content;
	}

}
