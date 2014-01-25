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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/scroll-params-limiter-mock-context.xml",
		"classpath:spring/config-test-context.xml"
})
public class BooksControllerUpdateTest {

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<Book> updatedBookCaptor;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldUpdateBook() throws Exception {
		final int sampleId = 6;
		final String sampleTitle = "sampleTitle";
		final BooksCommand command = aCommandWith(sampleId, sampleTitle);

		whenControllerAddPerformedWithCommand(command);

		thenExpectUpdatedBookWith(sampleId, sampleTitle);
	}

	@Test
	public void shouldRedirectAfterUpdate() throws Exception {
		final int anyId = 234;
		final String anyTitle = "anyTitle";
		final BooksCommand command = aCommandWith(anyId, anyTitle);

		whenControllerAddPerformedWithCommand(command);

		thenExpectHttpRedirect(command);
	}

	private BooksCommand aCommandWith(int id, String title) {
		BooksCommand command = new BooksCommand();
		command.setUpdateBookId(id);
		command.setUpdateBookTitle(title);

		return command;
	}

	private void whenControllerAddPerformedWithCommand(BooksCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/update")
				.flashAttr("booksCommand", command));
	}

	private void thenExpectUpdatedBookWith(int id, String title) {
		verify(bookRepositoryMock).update(updatedBookCaptor.capture());
		assertThat(updatedBookCaptor.getValue().getId(), equalTo(id));
		assertThat(updatedBookCaptor.getValue().getTitle(), equalTo(title));
	}

	private void thenExpectHttpRedirect(BooksCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/list"))
				.andExpect(flash().attribute("booksCommand", sameInstance(command)));
	}

}
