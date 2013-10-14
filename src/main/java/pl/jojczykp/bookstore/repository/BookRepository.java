package pl.jojczykp.bookstore.repository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.jojczykp.bookstore.domain.Book;

import java.util.List;

import static com.google.common.primitives.Ints.checkedCast;
import static org.hibernate.criterion.Projections.rowCount;

@Repository
@Transactional
public class BookRepository {

	@Autowired
	SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public int create(Book book) {
		return (int) getCurrentSession().save(book);
	}

	public Book get(int id) {
		return (Book) getCurrentSession().get(Book.class, id);
	}

	public List<Book> read(int offset, int size) {
		Criteria criteria = getCurrentSession().createCriteria(Book.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(size);

		return suppressUnchecked(criteria.list());
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> suppressUnchecked(List list) {
		return (List<T>) list;
	}

	public void update(Book book) {
		getCurrentSession().update(book);
	}

	public void delete(int id) {
		getCurrentSession().delete(new Book(id));
	}

	public int totalCount() {
		Long result = (Long) getCurrentSession().createCriteria(Book.class).setProjection(rowCount()).uniqueResult();
		return checkedCast(result);
	}
}
