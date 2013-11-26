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
import pl.jojczykp.bookstore.command.BookCommand;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.repository.BookRepository;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
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
public class BooksControllerDelTest {

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<Integer> idOfBookToRemove;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
		reset(bookRepositoryMock);
	}

	@Test
	public void shouldDeleteBook() throws Exception {
		final int id1 = 9;
		final int id2 = 78;
		final BooksCommand command = aCommandToRemoveByIds(id1, id2);

		whenControllerDelPerformedWithCommand(command);

		thenExpectDeletedBooksWithIds(id1, id2);
	}

	@Test
	public void shouldRedirectAfterDeleting() throws Exception {
		final int anyId = 5;
		final BooksCommand command = aCommandToRemoveByIds(anyId);

		whenControllerDelPerformedWithCommand(command);

		thenExpectHttpRedirect(command);
	}

	private BooksCommand aCommandToRemoveByIds(int... ids) {
		BooksCommand command = new BooksCommand();
		for (int i = 0; i < ids.length; i++) {
			command.getBooks().add(aDeletableBookCommandForId(ids[i]));
		}

		return command;
	}

	private BookCommand aDeletableBookCommandForId(int id) {
		BookCommand bookCommand = new BookCommand();
		bookCommand.setChecked(true);
		bookCommand.setId(id);

		return bookCommand;
	}

	private void whenControllerDelPerformedWithCommand(BooksCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/del")
				.flashAttr("booksCommand", command));
	}

	private void thenExpectDeletedBooksWithIds(Integer... ids) {
		verify(bookRepositoryMock, times(ids.length)).delete(idOfBookToRemove.capture());
		assertThat(idOfBookToRemove.getAllValues(), hasItems(ids));
	}

	private void thenExpectHttpRedirect(BooksCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/list"))
				.andExpect(flash().attribute("booksCommand", sameInstance(command)));
	}


}
