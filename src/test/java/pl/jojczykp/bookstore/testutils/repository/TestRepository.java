package pl.jojczykp.bookstore.testutils.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jojczykp.bookstore.domain.Book;

import java.util.List;

import static pl.jojczykp.bookstore.utils.SuppressUnchecked.suppressUnchecked;

@Repository
public class TestRepository {

	public static final int ID_TO_GENERATE = 0;

	@Autowired private SessionFactory sessionFactory;

	public List<Book> getAllBooks() {
		return getAll(Book.class);
	}

	private List<Book> getAll(Class clazz) {
		return suppressUnchecked(getCurrentSession().createCriteria(clazz).list());
	}

	public void givenRepositoryWith(Object... objects) {
		for (Object object: objects) {
			getCurrentSession().persist(object);
		}
		getCurrentSession().flush();
		getCurrentSession().clear();
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
