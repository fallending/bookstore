/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.jojczykp.bookstore.testutils.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jojczykp.bookstore.entities.Book;

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
