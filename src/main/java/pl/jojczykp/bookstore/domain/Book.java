package pl.jojczykp.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "BOOK")
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
		this(0, 0, "");
	}

	public Book(String title) {
		this(0, 0, title);
	}

	public Book(int id) {
		this(id, 0, "");
	}

	public Book(int id, int version, String title) {
		this.id = id;
		this.version = version;
		this.title = title;
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

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
