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

package pl.jojczykp.bookstore.jmx;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static pl.jojczykp.bookstore.testutils.builders.BookBuilder.aBook;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.SuppressUnchecked.suppressUnchecked;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:spring/applicationContext/jmx-context.xml",
		"classpath:spring/repositories-mock-context.xml",
		"classpath:spring/jmx-test-context.xml",
		"classpath:spring/books-command-factory-test-context.xml"
})
public class BooksManagementBeanComponentTest {

	private static final String JMX_SERVICE_URL_PATTERN =
			"service:jmx:rmi://localhost/jndi/rmi://localhost:%d/bookstoreJmxConnector";
	private static final String JMX_OBJECT_NAME = "bookstore.mbeans:name=BooksManagementBean";

	private static final Book BOOK_1 = aBook(1, 1, "Book 1");
	private static final Book BOOK_2 = aBook(2, 2, "Book 2");
	private static final List<Book> REPO_DATA = asList(BOOK_1, BOOK_2);

	private MBeanServerConnection mBeanServerConnection;
	private ObjectName objectName;

	@Value("${jmx.port}") private int jmxPort;

	@Autowired private BooksRepository booksRepository;

	@Captor private ArgumentCaptor<Book> bookCaptor;

	@Before
	public void givenRepositoryMockConfigured() {
		reset(booksRepository);
		given(booksRepository.totalCount()).willReturn(REPO_DATA.size());
		given(booksRepository
				.read(anyInt(), anyInt(), any(PageSorterColumn.class), any(PageSorterDirection.class)))
				.willReturn(REPO_DATA);
	}

	@Before
	public void givenJmxConnection() throws Exception {
		MockitoAnnotations.initMocks(this);
		connect();
		createObjectName();
	}

	private void connect() throws IOException {
		MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
		bean.setServiceUrl(String.format(JMX_SERVICE_URL_PATTERN, jmxPort));
		bean.afterPropertiesSet();
		mBeanServerConnection = bean.getObject();
	}

	private void createObjectName() throws MalformedObjectNameException {
		objectName = new ObjectName(JMX_OBJECT_NAME);
	}

	@Test
	public void shouldGetTotalCount() {
		int totalCount = (Integer) jmxInvoke("totalCount");

		verify(booksRepository).totalCount();
		assertThat(totalCount, is(REPO_DATA.size()));
	}

	@Test
	public void shouldReadBook() {
		final int offset = 1;
		final int size = 2;

		final List<String> books = suppressUnchecked(jmxInvoke("read", offset, size));

		verify(booksRepository).read(offset, size, BOOK_TITLE, ASC);
		assertThat(books.toString(), is(equalTo(REPO_DATA.toString())));
	}

	@Test
	public void shouldUpdateBook() {
		final int id = 5;
		final int version = 3;
		final String newTitle = "New Title";

		jmxInvoke("update", id, version, newTitle);

		verify(booksRepository).update(bookCaptor.capture());
		assertThat(bookCaptor.getValue().getId(), is(equalTo(id)));
		assertThat(bookCaptor.getValue().getVersion(), is(equalTo(version)));
		assertThat(bookCaptor.getValue().getTitle(), is(equalTo(newTitle)));
	}

	@Test
	public void shouldDeleteBook() {
		final int id = 7;

		jmxInvoke("delete", id);

		verify(booksRepository).delete(id);
	}

	private Object jmxInvoke(String name, Object... params) {
		String[] signature = signatureFor(params);
		try {
			return mBeanServerConnection.invoke(objectName, name, params, signature);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String[] signatureFor(Object[] params) {
		String[] signature = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			signature[i] = toClassString(params[i]);
		}

		return signature;
	}

	private String toClassString(Object param) {
		if (param.getClass() == Integer.class) {
			return int.class.toString();
		} else {
			return String.class.getName();
		}
	}

}
