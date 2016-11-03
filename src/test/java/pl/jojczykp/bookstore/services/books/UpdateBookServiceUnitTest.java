package pl.jojczykp.bookstore.services.books;

import org.hibernate.StaleObjectStateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import pl.jojczykp.bookstore.assemblers.UpdateBookAssembler;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.UpdateBookValidator;

import java.util.HashMap;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class UpdateBookServiceUnitTest {

	private static final String VALIDATOR_ERROR_MESSAGE = "An error message from validator.";

	@Mock private UpdateBookValidator updateBookValidator;
	@Mock private UpdateBookAssembler updateBookAssembler;
	@Mock private BooksRepository booksRepository;

	@InjectMocks private UpdateBookService testee;

	@Captor private ArgumentCaptor<UpdateBookCommand> updateBookCommandCaptor;
	@Captor private ArgumentCaptor<Book> updatedBookCaptor;

	@Mock private StaleObjectStateException staleObjectStateException;
	@Mock private Book book;

	private BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "anObjectName");

	@Before
	public void setUp() {
		given(updateBookAssembler.toDomain(any(UpdateBookCommand.class))).willReturn(book);
	}

	@Test
	public void shouldUpdateBook() {
		final UpdateBookCommand command = new UpdateBookCommand();

		DisplayBooksCommand displayBooksCommand = testee.update(command, bindingResult);

		thenExpectValidationInvokedFor(command);
		thenExpectUpdateInvokedOnRepository();
		thenExpectInfoOnlyMessage(displayBooksCommand, "Title updated.");
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	@Test
	public void shouldFailConcurrentlyUpdatingUpdatedBook() {
		final UpdateBookCommand command = new UpdateBookCommand();
		givenObjectConcurrentlyUpdated();

		DisplayBooksCommand displayBooksCommand = testee.update(command, bindingResult);

		thenExpectValidationInvokedFor(command);
		thenExpectUpdateInvokedOnRepository();
		thenExpectWarnOnlyMessage(displayBooksCommand,
				"Object updated or deleted by another user. Please try again with actual data.");
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	@Test
	public void shouldFailOnCommandValidationError() {
		final UpdateBookCommand command = new UpdateBookCommand();
		givenNegativeValidation();

		DisplayBooksCommand displayBooksCommand = testee.update(command, bindingResult);

		thenExpectValidationInvokedFor(command);
		thenExpectErrorOnlyMessage(displayBooksCommand, VALIDATOR_ERROR_MESSAGE);
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	private void givenObjectConcurrentlyUpdated() {
		doThrow(staleObjectStateException).when(booksRepository).update(any(Book.class));
	}

	private void givenNegativeValidation() {
		doAnswer(validationError())
				.when(updateBookValidator).validate(anyObject(), any(Errors.class));
	}

	private Answer<Void> validationError() {
		return new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) {
				Errors errors = (Errors) invocation.getArguments()[1];
				errors.rejectValue("title", "title.empty", VALIDATOR_ERROR_MESSAGE);
				return null;
			}
		};
	}

	private void thenExpectValidationInvokedFor(UpdateBookCommand updateBookCommand) {
		verify(updateBookValidator).validate(updateBookCommandCaptor.capture(), any(Errors.class));
		assertThat(updateBookCommandCaptor.getValue(), is(sameInstance(updateBookCommand)));
		verifyNoMoreInteractions(updateBookValidator);
	}

	private void thenExpectUpdateInvokedOnRepository() {
		verify(booksRepository).update(updatedBookCaptor.capture());
		assertThat(updatedBookCaptor.getValue(), is(sameInstance(book)));
		verifyNoMoreInteractions(booksRepository);
	}

	private void thenExpectInfoOnlyMessage(DisplayBooksCommand displayBooksCommand, String message) {
		assertThat(displayBooksCommand.getMessages().getInfos(), contains(message));
		assertThat(displayBooksCommand.getMessages().getWarns(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getErrors(), emptyCollectionOf(String.class));
	}

	private void thenExpectWarnOnlyMessage(DisplayBooksCommand displayBooksCommand, String message) {
		assertThat(displayBooksCommand.getMessages().getInfos(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getWarns(), contains(message));
		assertThat(displayBooksCommand.getMessages().getErrors(), emptyCollectionOf(String.class));
	}

	private void thenExpectErrorOnlyMessage(DisplayBooksCommand displayBooksCommand, String message) {
		assertThat(displayBooksCommand.getMessages().getInfos(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getWarns(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getErrors(), contains(message));
	}

	private void thenExpectPagerPropagated(UpdateBookCommand command, DisplayBooksCommand displayBooksCommand) {
		assertThat(displayBooksCommand.getPager(), is(sameInstance(command.getPager())));
	}

}
