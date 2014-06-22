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

package pl.jojczykp.bookstore.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.sql.Blob;

import static pl.jojczykp.bookstore.utils.BlobUtils.anEmptySerialBlob;

@Entity
@Table(name = "BOOK_FILES")
public class BookFile {

	private static final int ID_TO_BE_GENERATED = 0;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "FILE_TYPE", nullable = true)
	private String fileType;

	@Column(name = "CONTENT_TYPE", nullable = false)
	private String contentType;

	@Column(name = "CONTENT_LENGTH", nullable = false)
	private int contentLength;

	@Lob
	@Column(name = "CONTENT", nullable = false)
	private Blob content;

	public BookFile() {
		this.id = ID_TO_BE_GENERATED;
		this.fileType = "";
		this.contentType = "";
		this.contentLength = 0;
		this.content = anEmptySerialBlob();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		BookFile bookFile = (BookFile) o;

		return (id == bookFile.id);
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()
				+ "{id=" + id + ", fileType='" + fileType
				+ "', contentType='" + contentType + "', contentLength='" + contentLength + "'}";
	}

}
