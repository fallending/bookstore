package pl.jojczykp.bookstore.controller;

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
import pl.jojczykp.bookstore.command.BookCommand;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.validators.BookUpdateValidator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
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
import static pl.jojczykp.bookstore.testutils.matchers.MessagesControllerTestUtils.thenExpectErrorOnlyMessage;
import static pl.jojczykp.bookstore.testutils.matchers.MessagesControllerTestUtils.thenExpectInfoOnlyMessage;
import static pl.jojczykp.bookstore.testutils.matchers.MessagesControllerTestUtils.thenExpectWarnOnlyMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/config-test-context.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/book-command-validator-mock-context.xml",
		"classpath:spring/books-command-factory-mock-context.xml"
})
public class BooksControllerUpdateTest {

	private static final int SAMPLE_ID = 8;
	private static final String SAMPLE_TITLE = "sample title";
	private static final String VALIDATOR_ERROR_MESSAGE = "An error message from validator.";

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Mock private StaleObjectStateException staleObjectStateExceptionMock;
	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private BookUpdateValidator bookUpdateValidatorMock;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<Book> updatedBookCaptor;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
		reset(bookRepositoryMock);
		reset(bookUpdateValidatorMock);
	}

	@Test
	public void shouldUpdateBook() throws Exception {
		final BooksCommand command = aCommandWith(SAMPLE_ID, SAMPLE_TITLE);

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvoked();
		thenExpectUpdateInvokedOnRepository(SAMPLE_ID, SAMPLE_TITLE);
		thenExpectInfoOnlyMessage(mvcMockPerformResult, "Object updated.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldFailConcurrentlyUpdatingUpdatedBook() throws Exception {
		final BooksCommand command = aCommandWith(SAMPLE_ID, SAMPLE_TITLE);
		givenObjectConcurrentlyUpdated();

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvoked();
		thenExpectUpdateInvokedOnRepository(SAMPLE_ID, SAMPLE_TITLE);
		thenExpectWarnOnlyMessage(mvcMockPerformResult,
				"Object updated or deleted by another user. Please try again with actual data.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldFailOnCommandValidationError() throws Exception {
		final BooksCommand command = aCommandWith(SAMPLE_ID, SAMPLE_TITLE);
		givenNegativeValidation();

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvoked();
		thenExpectNoUpdateInvokedOnRepository();
		thenExpectErrorOnlyMessage(mvcMockPerformResult, VALIDATOR_ERROR_MESSAGE);
		thenExpectHttpRedirectWith(command);
	}

	private BooksCommand aCommandWith(int id, String title) {
		BooksCommand command = new BooksCommand();
		BookCommand book = new BookCommand();
		book.setId(id);
		book.setTitle(title);
		command.setUpdatedBook(book);

		return command;
	}

	private void givenObjectConcurrentlyUpdated() {
		doThrow(staleObjectStateExceptionMock).when(bookRepositoryMock).update(any(Book.class));
	}

	private void givenNegativeValidation() {
		doAnswer(validationError())
				.when(bookUpdateValidatorMock).validate(anyObject(), any(Errors.class));
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

	private void thenExpectValidationInvoked() {
		verify(bookUpdateValidatorMock).validate(anyObject(), any(Errors.class));
	}

	private void thenExpectUpdateInvokedOnRepository(int id, String title) {
		verify(bookRepositoryMock).update(updatedBookCaptor.capture());
		assertThat(updatedBookCaptor.getValue().getId(), equalTo(id));
		assertThat(updatedBookCaptor.getValue().getTitle(), equalTo(title));
		verifyNoMoreInteractions(bookRepositoryMock);
	}

	private void thenExpectNoUpdateInvokedOnRepository() {
		verifyZeroInteractions(bookRepositoryMock);
	}

	private void thenExpectHttpRedirectWith(BooksCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/read"))
				.andExpect(flash().attribute("booksCommand", sameInstance(command)));
	}

}
