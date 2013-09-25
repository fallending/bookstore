package bookstore.mvc.model.repository;

import bookstore.mvc.model.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository {

	public Iterable<Book> get(int offset, int limit) {
		List<Book> books = new ArrayList<>();

		for (int i = offset; i < offset + limit; i++) {
			books.add(new Book("Title of Book #" + String.format("%03d", i)));
		}

		return books;
	}
}
