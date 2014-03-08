package pl.jojczykp.bookstore.controllers;

import org.hibernate.StaleObjectStateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.Errors;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.assemblers.BookAssembler;
import pl.jojczykp.bookstore.commands.BookCommand;
import pl.jojczykp.bookstore.commands.BooksCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.BooksUpdateValidator;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectErrorOnlyFlashMessages;
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectInfoOnlyFlashMessages;
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectWarnOnlyFlashMessages;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class BooksControllerUpdateTest {

	private static final String VALIDATOR_ERROR_MESSAGE = "An error message from validator.";

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BooksUpdateValidator booksUpdateValidator;
	@Autowired private BookAssembler bookAssembler;
	@Autowired private BooksRepository booksRepository;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<BooksCommand> booksCommandCaptor;
	@Captor private ArgumentCaptor<BookCommand> bookCommandCaptor;
	@Captor private ArgumentCaptor<Book> updatedBookCaptor;

	@Mock private StaleObjectStateException staleObjectStateException;
	@Mock private Book book;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
		reset(booksUpdateValidator);
		reset(bookAssembler);
		reset(booksRepository);
		given(bookAssembler.toDomain(any(BookCommand.class))).willReturn(book);
	}

	@Test
	public void shouldUpdateBook() throws Exception {
		final BooksCommand command = new BooksCommand();

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainInvokedFor(command.getUpdatedBook());
		thenExpectUpdateInvokedOnRepository();
		thenExpectInfoOnlyFlashMessages(mvcMockPerformResult, "Object updated.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldFailConcurrentlyUpdatingUpdatedBook() throws Exception {
		final BooksCommand command = new BooksCommand();
		givenObjectConcurrentlyUpdated();

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainInvokedFor(command.getUpdatedBook());
		thenExpectUpdateInvokedOnRepository();
		thenExpectWarnOnlyFlashMessages(mvcMockPerformResult,
				"Object updated or deleted by another user. Please try again with actual data.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldFailOnCommandValidationError() throws Exception {
		final BooksCommand command = new BooksCommand();
		givenNegativeValidation();

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainNotInvoked();
		thenExpectUpdateNotInvokedOnRepository();
		thenExpectErrorOnlyFlashMessages(mvcMockPerformResult, VALIDATOR_ERROR_MESSAGE);
		thenExpectHttpRedirectWith(command);
	}

	private void givenObjectConcurrentlyUpdated() {
		doThrow(staleObjectStateException).when(booksRepository).update(any(Book.class));
	}

	private void givenNegativeValidation() {
		doAnswer(validationError())
				.when(booksUpdateValidator).validate(anyObject(), any(Errors.class));
	}

	private Answer<Void> validationError() {
		return new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) {
				Errors errors = (Errors) invocation.getArguments()[1];
				errors.rejectValue("updatedBook.title", "updatedBook.title.empty", VALIDATOR_ERROR_MESSAGE);
				return null;
			}
		};
	}

	private void whenControllerUpdatePerformedWithCommand(BooksCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/update")
				.flashAttr("booksCommand", command));
	}

	private void thenExpectValidationInvokedFor(BooksCommand booksCommand) {
		verify(booksUpdateValidator).validate(booksCommandCaptor.capture(), any(Errors.class));
		assertThat(booksCommandCaptor.getValue(), is(sameInstance(booksCommand)));
		verifyNoMoreInteractions(booksUpdateValidator);
	}

	private void thenExpectAssemblingCommandToDomainInvokedFor(BookCommand bookCommand) {
		verify(bookAssembler).toDomain(bookCommandCaptor.capture());
		assertThat(bookCommandCaptor.getValue(), is(sameInstance(bookCommand)));
		verifyNoMoreInteractions(bookAssembler);
	}

	private void thenExpectAssemblingCommandToDomainNotInvoked() {
		verifyZeroInteractions(bookAssembler);
	}

	private void thenExpectUpdateInvokedOnRepository() {
		verify(booksRepository).update(updatedBookCaptor.capture());
		assertThat(updatedBookCaptor.getValue(), is(sameInstance(book)));
		verifyNoMoreInteractions(booksRepository);
	}

	private void thenExpectUpdateNotInvokedOnRepository() {
		verifyZeroInteractions(booksRepository);
	}

	private void thenExpectHttpRedirectWith(BooksCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/read"))
				.andExpect(flash().attribute("booksCommand", sameInstance(command)));
	}

}
