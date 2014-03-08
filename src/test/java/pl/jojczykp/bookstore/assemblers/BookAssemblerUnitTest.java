package pl.jojczykp.bookstore.assemblers;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.commands.BookCommand;
import pl.jojczykp.bookstore.entities.Book;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.testutils.builders.BookBuilder.aBook;

public class BookAssemblerUnitTest {

	private static final int ID1 = 34;
	private static final int VERSION1 = 45;
	private static final String TITLE1 = "A Title 001";

	private static final int ID2 = 54;
	private static final int VERSION2 = 89;
	private static final String TITLE2 = "A Title 002";

	private BookAssembler testee;

	@Before
	public void setUpTestee() {
		testee = new BookAssembler();
	}

	@Test
	public void shouldAssemblyCommandObjectsListFromDomainObjectsList() {
		List<Book> domains = aDomainObjectsList();

		List<BookCommand> commands = testee.toCommands(domains);

		assertThat(commands.size(), equalTo(domains.size()));
		for (int i = 0; i < domains.size(); i++) {
			assertThatHaveEqualData(domains.get(i), commands.get(i));
		}
	}

	private List<Book> aDomainObjectsList() {
		return asList(
				aBook(ID1, VERSION1, TITLE1),
				aBook(ID2, VERSION2, TITLE2));
	}

	@Test
	public void shouldAssemblySingleDomainObjectFromCommandObject() {
		BookCommand command = aCommandObject();

		Book domain = testee.toDomain(command);

		assertThatHaveEqualData(domain, command);
	}

	private BookCommand aCommandObject() {
		BookCommand command = new BookCommand();
		command.setId(ID1);
		command.setVersion(VERSION1);
		command.setTitle(TITLE1);

		return command;
	}

	private void assertThatHaveEqualData(Book domain, BookCommand command) {
		assertThat(domain.getId(), equalTo(command.getId()));
		assertThat(domain.getVersion(), equalTo(command.getVersion()));
		assertThat(domain.getTitle(), equalTo(command.getTitle()));
	}

}
