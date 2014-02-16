package pl.jojczykp.bookstore.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/beans-mock-context.xml"
})
public class BooksControllerCreateTest {

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<Book> newBookCaptor;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
		reset(bookRepositoryMock);
	}

	@Test
	public void shouldCreateBook() throws Exception {
		final String someTitle = "someTitle";
		final BooksCommand command = aCommandWith(someTitle);

		whenControllerCreatePerformedWithCommand(command);

		thenExpectCreatedBookWith(someTitle);
	}

	@Test
	public void shouldRedirectAfterCreation() throws Exception {
		final String anyTitle = "anyTitle";
		final BooksCommand command = aCommandWith(anyTitle);

		whenControllerCreatePerformedWithCommand(command);

		thenExpectHttpRedirect(command);
	}

	@Test
	public void shouldDisplayMessageAfterCreation() throws Exception {
		final BooksCommand command = aCommandWith("anyTitle");

		whenControllerCreatePerformedWithCommand(command);

		thenExpectDisplayedMessage("Object created.");
	}

	private BooksCommand aCommandWith(String title) {
		BooksCommand command = new BooksCommand();
		command.getNewBook().setTitle(title);

		return command;
	}

	private void whenControllerCreatePerformedWithCommand(BooksCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/create")
				.flashAttr("booksCommand", command));
	}

	private void thenExpectCreatedBookWith(String title) {
		verify(bookRepositoryMock).create(newBookCaptor.capture());
		assertThat(newBookCaptor.getValue().getTitle(), equalTo(title));
	}

	private void thenExpectHttpRedirect(BooksCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/read"))
				.andExpect(flash().attribute("booksCommand", sameInstance(command)));
	}

	private void thenExpectDisplayedMessage(String expectedMessage) throws Exception {
		mvcMockPerformResult
				.andExpect(flash().attribute("booksCommand",
						hasBeanProperty("message", equalTo(expectedMessage))));
	}

}
