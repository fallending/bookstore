package bookstore.mvc.model.repository;

import bookstore.mvc.model.domain.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class BookRepositoryTest {

	@Autowired private BookRepository repository;

	@Test
	public void shouldReturnFullListOfBooks() {
		final int limit = 15;

		Iterable<Book> books = repository.get(0, limit);

		assertThat(books, org.hamcrest.Matchers.<Book>iterableWithSize(limit));
	}
}
