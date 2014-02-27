package pl.jojczykp.bookstore.controller;

import org.hibernate.ObjectNotFoundException;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.MessagesControllerTestUtils.thenExpectInfoOnlyMessage;
import static pl.jojczykp.bookstore.testutils.matchers.MessagesControllerTestUtils.thenExpectWarnOnlyMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/beans-test-context.xml")
public class BooksControllerDeleteTest {

	private static final int NOT_EXISTING_ID = 98;
	private static final int EXISTING_ID = 7;

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<Integer> idOfBookToRemove;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
		MockitoAnnotations.initMocks(this);
		setUpBookRepositoryMock();
	}

	private void setUpBookRepositoryMock() {
		reset(bookRepositoryMock);
		doThrow(ObjectNotFoundException.class).when(bookRepositoryMock).delete(NOT_EXISTING_ID);
	}

	@Test
	public void shouldDeleteBook() throws Exception {
		final int id1 = 9;
		final int id2 = 11;
		final BooksCommand command = aCommandToRemoveByIds(id1, id2);

		whenControllerDeletePerformedWithCommand(command);

		thenExpectDeletedBooksWithIds(id1, id2);
	}

	@Test
	public void shouldRedirectAfterDeletingExisting() throws Exception {
		final BooksCommand command = aCommandToRemoveByIds(EXISTING_ID);

		whenControllerDeletePerformedWithCommand(command);

		thenExpectHttpRedirect(command);
	}

	@Test
	public void shouldRedirectAfterDeletingNotExisting() throws Exception {
		final BooksCommand command = aCommandToRemoveByIds(NOT_EXISTING_ID);

		whenControllerDeletePerformedWithCommand(command);

		thenExpectHttpRedirect(command);
	}

	@Test
	public void shouldDisplayMessageAfterDeletingExisting() throws Exception {
		final BooksCommand command = aCommandToRemoveByIds(EXISTING_ID);

		whenControllerDeletePerformedWithCommand(command);

		thenExpectInfoOnlyMessage(mvcMockPerformResult, "Object deleted.");
	}

	@Test
	public void shouldDisplayMessageAfterDeletingNotExisting() throws Exception {
		final BooksCommand command = aCommandToRemoveByIds(NOT_EXISTING_ID);

		whenControllerDeletePerformedWithCommand(command);

		thenExpectWarnOnlyMessage(mvcMockPerformResult, "Object already deleted.");
	}

	private BooksCommand aCommandToRemoveByIds(int... ids) {
		BooksCommand command = new BooksCommand();
		for (int id : ids) {
			command.getBooks().add(aCommandToDeleteBookWithId(id));
		}

		return command;
	}

	private BookCommand aCommandToDeleteBookWithId(int id) {
		BookCommand bookCommand = new BookCommand();
		bookCommand.setChecked(true);
		bookCommand.setId(id);

		return bookCommand;
	}

	private void whenControllerDeletePerformedWithCommand(BooksCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/delete")
				.flashAttr("booksCommand", command));
	}

	private void thenExpectDeletedBooksWithIds(Integer... ids) {
		verify(bookRepositoryMock, times(ids.length)).delete(idOfBookToRemove.capture());
		assertThat(idOfBookToRemove.getAllValues(), hasItems(ids));
	}

	private void thenExpectHttpRedirect(BooksCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/read"))
				.andExpect(flash().attribute("booksCommand", sameInstance(command)));
	}

}
