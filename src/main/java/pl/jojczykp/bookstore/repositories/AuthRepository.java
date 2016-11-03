package pl.jojczykp.bookstore.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.jojczykp.bookstore.entities.User;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
@Transactional
public class AuthRepository {

	@Autowired private SessionFactory sessionFactory;

	public User findByName(String name) {
		return (User) getCurrentSession()
				.createCriteria(User.class)
				.add(eq("name", name))
				.uniqueResult();
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
