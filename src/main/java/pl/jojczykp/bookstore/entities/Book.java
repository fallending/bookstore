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
