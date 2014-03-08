package pl.jojczykp.bookstore.testutils.builders;

import pl.jojczykp.bookstore.domain.Book;

public abstract class BookBuilder {

	public static Book aBook(int id, int version, String title) {
		Book result = new Book();
		result.setId(id);
		result.setVersion(version);
		result.setTitle(title);

		return result;
	}
}
