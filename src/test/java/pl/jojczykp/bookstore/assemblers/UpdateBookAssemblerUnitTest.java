package pl.jojczykp.bookstore.assemblers;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class UpdateBookAssemblerUnitTest {

	private static final int ID = 67;
	private static final int VERSION = 48;
	private static final String TITLE = "A Title";

	private UpdateBookAssembler testee;

	@Before
	public void setUpTestee() {
		testee = new UpdateBookAssembler();
	}

	@Test
	public void shouldAssemblySingleBookDomainObjectFromUpdateBookCommandObject() {
		UpdateBookCommand command = anUpdateBookCommandWith(ID, VERSION, TITLE);

		Book domain = testee.toDomain(command);

		assertThatHaveEqualData(domain, command);
	}

	private UpdateBookCommand anUpdateBookCommandWith(int id, int version, String title) {
		UpdateBookCommand command = new UpdateBookCommand();
		command.setId(id);
		command.setVersion(version);
		command.setTitle(title);

		return command;
	}

	private void assertThatHaveEqualData(Book domain, UpdateBookCommand command) {
		assertThat(domain.getId(), equalTo(command.getId()));
		assertThat(domain.getVersion(), equalTo(command.getVersion()));
		assertThat(domain.getTitle(), equalTo(command.getTitle()));
		assertThat(domain.getBookFile(), is(nullValue()));
	}

}
