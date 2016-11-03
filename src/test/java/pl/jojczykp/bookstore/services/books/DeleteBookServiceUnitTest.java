package pl.jojczykp.bookstore.services.books;

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jojczykp.bookstore.commands.books.DeleteBooksCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DeleteBookServiceUnitTest {

	private static final int EXISTING_ID_1 = 3;
	private static final int EXISTING_ID_2 = 7;
	private static final int EXISTING_ID_3 = 9;
	private static final int NOT_EXISTING_ID = 98;

	@Mock private BooksRepository booksRepository;

	@InjectMocks private DeleteBookService testee;

	@Captor private ArgumentCaptor<Integer> idOfBookToRemove;

	@Before
	public void setUp() {
		reset(booksRepository);
		doThrow(ObjectNotFoundException.class).when(booksRepository).delete(NOT_EXISTING_ID);
	}

	@Test
	public void shouldDeleteExistingBooks() {
		final DeleteBooksCommand command = aCommandToRemoveByIds(EXISTING_ID_1, EXISTING_ID_2, EXISTING_ID_3);

		DisplayBooksCommand displayBooksCommand = testee.delete(command);

		thenExpectDeleteInvokedOnRepository(EXISTING_ID_1, EXISTING_ID_2, EXISTING_ID_3);
		thenExpectInfoOnlyMessages(displayBooksCommand, "Object deleted.", "Object deleted.", "Object deleted.");
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	@Test
	public void shouldFailOnDeletingNotExistingBook() {
		final DeleteBooksCommand command = aCommandToRemoveByIds(NOT_EXISTING_ID);

		DisplayBooksCommand displayBooksCommand = testee.delete(command);

		thenExpectDeleteInvokedOnRepository(NOT_EXISTING_ID);
		thenExpectWarnOnlyMessages(displayBooksCommand, "Object already deleted.");
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	private DeleteBooksCommand aCommandToRemoveByIds(int... ids) {
		DeleteBooksCommand command = new DeleteBooksCommand();
		for (int id : ids) {
			command.getIds().add(id);
		}

		return command;
	}

	private void thenExpectDeleteInvokedOnRepository(Integer... ids) {
		verify(booksRepository, times(ids.length)).delete(idOfBookToRemove.capture());
		assertThat(idOfBookToRemove.getAllValues(), hasItems(ids));
		verifyNoMoreInteractions(booksRepository);
	}

	private void thenExpectInfoOnlyMessages(DisplayBooksCommand displayBooksCommand, String... messages) {
		assertThat(displayBooksCommand.getMessages().getInfos(), contains(messages));
		assertThat(displayBooksCommand.getMessages().getWarns(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getErrors(), emptyCollectionOf(String.class));
	}

	private void thenExpectWarnOnlyMessages(DisplayBooksCommand displayBooksCommand, String... messages) {
		assertThat(displayBooksCommand.getMessages().getInfos(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getWarns(), contains(messages));
		assertThat(displayBooksCommand.getMessages().getErrors(), emptyCollectionOf(String.class));
	}

	private void thenExpectPagerPropagated(DeleteBooksCommand command, DisplayBooksCommand displayBooksCommand) {
		assertThat(displayBooksCommand.getPager(), is(sameInstance(command.getPager())));
	}

}
