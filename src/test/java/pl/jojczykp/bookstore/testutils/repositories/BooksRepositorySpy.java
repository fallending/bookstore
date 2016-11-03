package pl.jojczykp.bookstore.testutils.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.entities.BookFile;

import java.util.List;

import static pl.jojczykp.bookstore.utils.SuppressUnchecked.suppressUnchecked;

@Repository
public class BooksRepositorySpy {

	public static final int ID_TO_GENERATE = 0;
	public static final int FIRST_GENERATED_ID = 1;

	@Autowired private SessionFactory sessionFactory;

	public List<Book> getAllBooks() {
		return suppressUnchecked(getAll(Book.class));
	}

	public List<BookFile> getAllBookFiles() {
		return suppressUnchecked(getAll(BookFile.class));
	}

	private List<?> getAll(Class clazz) {
		return getCurrentSession().createCriteria(clazz).list();
	}

	public void givenRepositoryWith(Object... objects) {
		for (Object object: objects) {
			getCurrentSession().save(object);
		}
		getCurrentSession().flush();
		getCurrentSession().clear();
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
