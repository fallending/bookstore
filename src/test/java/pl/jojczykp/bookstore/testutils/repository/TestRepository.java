package pl.jojczykp.bookstore.testutils.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.domain.User;

import java.util.List;

import static pl.jojczykp.bookstore.utils.SuppressUnchecked.suppressUnchecked;

@Repository
public class TestRepository {

	public static final int ID_TO_BE_GENERATED = 0;

	@Autowired private SessionFactory sessionFactory;

	public List<Book> getAllBooks() {
		return suppressUnchecked(getCurrentSession().createCriteria(Book.class).list());
	}

	public void givenRepositoryWith(Book... books) {
		for (Book book: books) {
			getCurrentSession().save(book);
		}
	}

	public void givenRepositoryWith(User... users) {
		for (User user : users) {
			getCurrentSession().save(user);
		}
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
