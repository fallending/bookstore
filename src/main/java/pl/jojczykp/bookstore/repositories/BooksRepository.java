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
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package pl.jojczykp.bookstore.repositories;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.transfers.BookTO;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;

import java.util.List;

import static com.google.common.primitives.Ints.checkedCast;
import static java.util.Collections.emptyList;
import static org.hibernate.criterion.Projections.rowCount;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;
import static pl.jojczykp.bookstore.utils.PageSorter.orderBy;
import static pl.jojczykp.bookstore.utils.SuppressUnchecked.suppressUnchecked;

@Repository
@Transactional
public class BooksRepository {

	@Autowired private SessionFactory sessionFactory;

	public int create(Book book) {
		return (int) getCurrentSession().save(book);
	}

	public BookTO find(int id) {
		Book book = (Book) getCurrentSession().get(Book.class, id);
		return toTransferObjectOrNull(book);
	}

	private BookTO toTransferObjectOrNull(Book book) {
		if (book != null) {
			return new BookTO(book.getTitle(), book.getBookFile().getContentType(),
								blobBytes(book.getBookFile().getContent()));
		} else {
			return null;
		}
	}

	public List<Book> read(int offset, int size, PageSorterColumn sortColumn, PageSorterDirection sortDirection) {
		if (size <= 0) {
			return emptyList();
		} else {
			return readWithPositiveSize(offset, size, sortColumn, sortDirection);
		}
	}

	private List<Book> readWithPositiveSize(int offset, int size,
											PageSorterColumn sortColumn, PageSorterDirection sortDirection) {
		Criteria criteria = getCurrentSession().createCriteria(Book.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(size);
		criteria.addOrder(orderBy(sortColumn, sortDirection));

		return suppressUnchecked(criteria.list());
	}

	public void update(Book template) {
		getCurrentSession().merge(template);
	}

	public void delete(int id) {
		Book book = (Book) getCurrentSession().load(Book.class, id);
		getCurrentSession().delete(book);
	}

	public int totalCount() {
		Long result = (Long) getCurrentSession().createCriteria(Book.class).setProjection(rowCount()).uniqueResult();
		return checkedCast(result);
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
