package pl.jojczykp.bookstore.entities.builders;

import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.entities.BookFile;

public class BookBuilder {

	private Book template = new Book();

	public static BookBuilder aBook() {
		return new BookBuilder();
	}

	public BookBuilder withId(int id) {
		template.setId(id);
		return this;
	}

	public BookBuilder withVersion(int version) {
		template.setVersion(version);
		return this;
	}

	public BookBuilder withTitle(String title) {
		template.setTitle(title);
		return this;
	}

	public BookBuilder withBookFile(BookFile bookFile) {
		template.setBookFile(bookFile);
		return this;
	}

	public Book build() {
		Book constructed = new Book();
		constructed.setId(template.getId());
		constructed.setVersion(template.getVersion());
		constructed.setTitle(template.getTitle());
		constructed.setBookFile(template.getBookFile());

		return constructed;
	}

}
