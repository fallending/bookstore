package pl.jojczykp.bookstore.controller;

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
import pl.jojczykp.bookstore.command.BooksCommand;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/config-test-context.xml"
})
public class BooksControllerScrollTest {

	private static final String BOOKS_COMMAND = "booksCommand";

	private static final String URL_ACTION_PREV = "/books/prev";
	private static final String URL_ACTION_NEXT = "/books/next";
	private static final String URL_ACTION_SET_PAGE_SIZE = "/books/setPageSize";

	private static final int INITIAL_OFFSET = 25;
	private static final int INITIAL_SIZE = 15;

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
	}

	@Test
	public void shouldScrollPrev() throws Exception {
		mvcMockPerformResult = mvcMock.perform(post(URL_ACTION_PREV)
				.flashAttr(BOOKS_COMMAND, aBooksCommand(INITIAL_OFFSET, INITIAL_SIZE)));

		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.current.offset", equalTo(INITIAL_OFFSET - INITIAL_SIZE))))
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.current.size", equalTo(INITIAL_SIZE))));
	}

	@Test
	public void shouldScrollNext() throws Exception {
		mvcMockPerformResult = mvcMock.perform(post(URL_ACTION_NEXT)
				.flashAttr(BOOKS_COMMAND, aBooksCommand(INITIAL_OFFSET, INITIAL_SIZE)));

		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.current.offset", equalTo(INITIAL_OFFSET + INITIAL_SIZE))))
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.current.size", equalTo(INITIAL_SIZE))));
	}

	@Test
	public void shouldSetPageSize() throws Exception {
		final int anyOffset = 1;
		final int pageSize = 4;

		mvcMockPerformResult = mvcMock.perform(post(URL_ACTION_SET_PAGE_SIZE)
				.flashAttr(BOOKS_COMMAND, aBooksCommand(anyOffset, pageSize)));

		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.current.size", equalTo(pageSize))));
	}

	private BooksCommand aBooksCommand(int offset, int size) {
		BooksCommand command = new BooksCommand();
		command.getScroll().getCurrent().setOffset(offset);
		command.getScroll().getCurrent().setSize(size);

		return command;
	}

}
