package pl.jojczykp.bookstore.controllers.books;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.commands.books.DeleteBooksCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.services.books.DeleteBookService;

import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class DeleteBookControllerComponentTest {

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;
	@Autowired private DeleteBookService deleteBookService;

	private DeleteBooksCommand deleteBooksCommand = new DeleteBooksCommand();
	private DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		reset(deleteBookService);
	}

	@Test
	public void shouldDelete() throws Exception {
		given(deleteBookService.delete(eq(deleteBooksCommand))).willReturn(displayBooksCommand);

		whenControllerDeletePerformedWithCommand(deleteBooksCommand);

		thenExpectServiceInvokedFor(deleteBooksCommand);
		thenExpectHttpRedirectWith(displayBooksCommand);
	}

	private void whenControllerDeletePerformedWithCommand(DeleteBooksCommand deleteBooksCommand) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/delete")
				.flashAttr("deleteBooksCommand", deleteBooksCommand));
	}

	private void thenExpectServiceInvokedFor(DeleteBooksCommand deleteBooksCommand) {
		verify(deleteBookService).delete(eq(deleteBooksCommand));
		verifyNoMoreInteractions(deleteBookService);
	}

	private void thenExpectHttpRedirectWith(DisplayBooksCommand displayBooksCommand) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/display"))
				.andExpect(flash().attribute("displayBooksCommand",
						sameInstance(displayBooksCommand)));
	}

}
