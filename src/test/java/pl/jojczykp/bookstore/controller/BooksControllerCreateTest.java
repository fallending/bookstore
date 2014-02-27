package pl.jojczykp.bookstore.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.validators.BooksCreateValidator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.MessagesControllerTestUtils.thenExpectErrorOnlyMessage;
import static pl.jojczykp.bookstore.testutils.matchers.MessagesControllerTestUtils.thenExpectInfoOnlyMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/config-test-context.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/book-command-validator-mock-context.xml",
		"classpath:spring/books-command-factory-mock-context.xml"
})
public class BooksControllerCreateTest {

	private static final String SAMPLE_TITLE = "sample title";
	private static final String VALIDATOR_ERROR_MESSAGE = "An error message from validator.";

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private BooksCreateValidator booksCreateValidatorMock;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<Book> newBookCaptor;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
		reset(bookRepositoryMock);
		reset(booksCreateValidatorMock);
	}

	@Test
	public void shouldCreateBook() throws Exception {
		final String someTitle = "someTitle";
		final BooksCommand command = aCommandWith(someTitle);

		whenControllerCreatePerformedWithCommand(command);

		thenExpectValidationInvoked();
		thenExpectCreateInvokedOnRepository(someTitle);
		thenExpectInfoOnlyMessage(mvcMockPerformResult, "Object created.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldFailOnCommandValidationError() throws Exception {
		final BooksCommand command = aCommandWith(SAMPLE_TITLE);
		givenNegativeValidation();

		whenControllerCreatePerformedWithCommand(command);

		thenExpectValidationInvoked();
		thenExpectNoCreateInvokedOnRepository();
		thenExpectErrorOnlyMessage(mvcMockPerformResult, VALIDATOR_ERROR_MESSAGE);
		thenExpectHttpRedirectWith(command);
	}

	private BooksCommand aCommandWith(String title) {
		BooksCommand command = new BooksCommand();
		command.getNewBook().setTitle(title);

		return command;
	}

	private void givenNegativeValidation() {
		doAnswer(validationError())
				.when(booksCreateValidatorMock).validate(anyObject(), any(Errors.class));
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

	private void whenControllerCreatePerformedWithCommand(BooksCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/create")
				.flashAttr("booksCommand", command));
	}

	private void thenExpectValidationInvoked() {
		verify(booksCreateValidatorMock).validate(anyObject(), any(Errors.class));
	}

	private void thenExpectCreateInvokedOnRepository(String title) {
		verify(bookRepositoryMock).create(newBookCaptor.capture());
		assertThat(newBookCaptor.getValue().getTitle(), equalTo(title));
	}

	private void thenExpectNoCreateInvokedOnRepository() {
		verifyZeroInteractions(bookRepositoryMock);
	}

	private void thenExpectHttpRedirectWith(BooksCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/read"))
				.andExpect(flash().attribute("booksCommand", sameInstance(command)));
	}

}
