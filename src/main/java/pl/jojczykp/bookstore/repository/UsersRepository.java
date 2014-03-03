package pl.jojczykp.bookstore.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.jojczykp.bookstore.domain.User;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
@Transactional
public class UsersRepository {

	@Autowired private SessionFactory sessionFactory;

	public int create(User user) {
		return (int) getCurrentSession().save(user);
	}

	public User get(int id) {
		return (User) getCurrentSession().get(User.class, id);
	}

	public User findByNameAndPassword(String name, String password) {
		return (User) getCurrentSession()
				.createCriteria(User.class)
				.add(eq("name", name))
				.add(eq("password", password))
				.uniqueResult();
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
