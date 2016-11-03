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
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.services.books.UpdateBookService;

import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
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
public class UpdateBookControllerComponentTest {

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;
	@Autowired private UpdateBookService updateBookService;

	private UpdateBookCommand updateBookCommand = new UpdateBookCommand();
	private DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		reset(updateBookService);
	}

	@Test
	public void shouldUpdate() throws Exception {
		given(updateBookService.update(eq(updateBookCommand), any(BindingResult.class)))
				.willReturn(displayBooksCommand);

		whenControllerUpdatePerformedWithCommand(updateBookCommand);

		thenExpectServiceInvokedFor(updateBookCommand);
		thenExpectHttpRedirectWith(displayBooksCommand);
	}

	private void whenControllerUpdatePerformedWithCommand(UpdateBookCommand updateBookCommand) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/update")
				.flashAttr("updateBookCommand", updateBookCommand));
	}

	private void thenExpectServiceInvokedFor(UpdateBookCommand updateBookCommand) {
		verify(updateBookService).update(eq(updateBookCommand), any(BindingResult.class));
		verifyNoMoreInteractions(updateBookService);
	}

	private void thenExpectHttpRedirectWith(DisplayBooksCommand displayBooksCommand) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/display"))
				.andExpect(flash().attribute("displayBooksCommand",
						sameInstance(displayBooksCommand)));
	}

}
