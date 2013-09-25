package bookstore.mvc.model.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookTest {

	@Test
	public void shouldCreateBookWithTitlePassedToConstructor() {
		final String title = "A Title";

		Book book = new Book(title);

		assertThat(book.getTitle(), is(title));
	}
}
