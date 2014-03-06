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
public class SecurityRepository {

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
