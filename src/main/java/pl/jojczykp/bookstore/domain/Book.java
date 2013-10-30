package pl.jojczykp.bookstore.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOK")
public class Book {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "TITLE", nullable = false)
	private String title;

	public Book() {
		this(0, "");
	}

	public Book(String title) {
		this(0, title);
	}

	public Book(int id) {
		this(id, "");
	}

	public Book(int id, String title) {
		setId(id);
		setTitle(title);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return "Book{id=" + id + ", title='" + title + "'}";
	}
}
