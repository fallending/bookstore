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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CascadeType.DELETE;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Entity
@DynamicUpdate
@Table(name = "BOOKS")
public class Book {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Version
	@Column(name = "VERSION")
	private int version;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@OneToOne(fetch = EAGER)
	@Cascade({SAVE_UPDATE, DELETE})
	@JoinColumn(name = "BOOK_FILE_ID", nullable = false)
	private BookFile bookFile;

	public Book() {
		this.id = 0;
		this.version = 0;
		this.title = "";
		this.bookFile = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BookFile getBookFile() {
		return bookFile;
	}

	public void setBookFile(BookFile bookFile) {
		this.bookFile = bookFile;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Book book = (Book) o;

		return (id == book.id);
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()
			+ "{id=" + id + ", version=" + version + ", title='" + title + "', bookFile=" + bookFile + "}";
	}

}
