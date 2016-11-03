package pl.jojczykp.bookstore.entities.builders;

import org.junit.Test;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.entities.BookFile;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookBuilder.aBook;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;

public class BookBuilderUnitTest {

	private static final int ID = 87;
	private static final int VERSION = 57;
	private static final String TITLE = "aFileType";
	private static final BookFile BOOK_FILE = aBookFile().build();

	@Test
	public void shouldBuildWithId() {
		Book book = aBook().withId(ID).build();

		assertThat(book.getId(), is(ID));
	}

	@Test
	public void shouldBuildWithVersion() {
		Book book = aBook().withVersion(VERSION).build();

		assertThat(book.getVersion(), is(VERSION));
	}

	@Test
	public void shouldBuildWithTitle() {
		Book book = aBook().withTitle(TITLE).build();

		assertThat(book.getTitle(), is(equalTo(TITLE)));
	}

	@Test
	public void shouldBuildWithBookFile() {
		Book book = aBook().withBookFile(BOOK_FILE).build();

		assertThat(book.getBookFile(), is(sameInstance(BOOK_FILE)));
	}

}
