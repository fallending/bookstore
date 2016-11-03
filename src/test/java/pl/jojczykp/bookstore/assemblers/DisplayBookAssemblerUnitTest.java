package pl.jojczykp.bookstore.assemblers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jojczykp.bookstore.commands.books.DisplayBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookBuilder.aBook;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;

@RunWith(MockitoJUnitRunner.class)
public class DisplayBookAssemblerUnitTest {

	private static final String KNOWN_FILE_TYPE = "txt";
	private static final String OTHER_KNOWN_FILE_TYPE = "pdf";
	private static final String UNKNOWN_FILE_TYPE = "someUnknownFileType";

	@InjectMocks private DisplayBookAssembler testee;

	@Test
	public void shouldAssemblyBookCommandObjectsListFromDomainObjectsList() {
		List<Book> domains = aDomainObjectsList();

		List<DisplayBookCommand> commands = testee.toCommands(domains);

		assertThat(commands.size(), equalTo(domains.size()));
		for (int i = 0; i < domains.size(); i++) {
			assertThatHaveEqualBookData(domains.get(i), commands.get(i));
		}
	}

	@Test
	public void shouldSetDefaultIconForUnknownFileType() {
		Book domain = aBook().withBookFile(aBookFile().withId(0).withFileType(UNKNOWN_FILE_TYPE).build()).build();

		List<DisplayBookCommand> commands = testee.toCommands(asList(domain));

		assertThat(commands.get(0).getIconName(), is(equalTo("unknown")));
	}

	private List<Book> aDomainObjectsList() {
		return asList(
				aBook().withId(1).withVersion(0).withTitle("A Title 001")
						.withBookFile(aBookFile().withId(0).withFileType(KNOWN_FILE_TYPE).build())
						.build(),
				aBook().withId(2).withVersion(1).withTitle("A Title 002")
						.withBookFile(aBookFile().withId(1).withFileType(OTHER_KNOWN_FILE_TYPE).build())
						.build());
	}

	private void assertThatHaveEqualBookData(Book domain, DisplayBookCommand command) {
		assertThat(domain.getId(), equalTo(command.getId()));
		assertThat(domain.getVersion(), equalTo(command.getVersion()));
		assertThat(domain.getTitle(), equalTo(command.getTitle()));
		assertThat(domain.getBookFile().getFileType(), is(equalTo(command.getIconName())));
	}

}
