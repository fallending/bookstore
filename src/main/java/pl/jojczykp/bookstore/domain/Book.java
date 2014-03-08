package pl.jojczykp.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
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

	public Book() {
		this.id = 0;
		this.version = 0;
		this.title = "";
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
		return "Book{id=" + id + ", version=" + version + ", title='" + title + "'}";
	}

}
