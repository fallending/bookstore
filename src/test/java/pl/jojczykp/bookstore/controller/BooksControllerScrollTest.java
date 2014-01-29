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
import pl.jojczykp.bookstore.utils.ScrollSorterColumn;
import pl.jojczykp.bookstore.utils.ScrollSorterDirection;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;
import static pl.jojczykp.bookstore.utils.ScrollSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.ScrollSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.ScrollSorterDirection.DESC;

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
	private static final String URL_ACTION_SORT = "/books/sort";
	private static final String URL_ACTION_SET_PAGE_SIZE = "/books/setPageSize";

	private static final int INITIAL_OFFSET = 25;
	private static final int INITIAL_SIZE = 15;
	private ScrollSorterColumn ANY_SORT_COLUMN = BOOK_TITLE;
	private ScrollSorterDirection ANY_SORT_DIRECTION = ASC;

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
				.flashAttr(BOOKS_COMMAND,
						aBooksCommand(INITIAL_OFFSET, INITIAL_SIZE, ANY_SORT_COLUMN, ANY_SORT_DIRECTION)));

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
				.flashAttr(BOOKS_COMMAND,
						aBooksCommand(INITIAL_OFFSET, INITIAL_SIZE, ANY_SORT_COLUMN, ANY_SORT_DIRECTION)));

		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.current.offset", equalTo(INITIAL_OFFSET + INITIAL_SIZE))))
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.current.size", equalTo(INITIAL_SIZE))));
	}

	@Test
	public void shouldSort() throws Exception {
		final int anyOffset = 3;
		final int anyPageSize = 5;
		final ScrollSorterColumn sortColumn = BOOK_TITLE;
		final ScrollSorterDirection sortDirection = ASC;

		mvcMockPerformResult = mvcMock.perform(post(URL_ACTION_SORT)
				.flashAttr(BOOKS_COMMAND, aBooksCommand(anyOffset, anyPageSize, sortColumn, sortDirection)));

		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.sorter.column", equalTo(sortColumn))))
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.sorter.direction", equalTo(sortDirection))));
	}

	@Test
	public void shouldSetPageSize() throws Exception {
		final int anyOffset = 1;
		final int pageSize = 4;
		final ScrollSorterColumn anySortColumn = BOOK_TITLE;
		final ScrollSorterDirection anySortDirection = DESC;

		mvcMockPerformResult = mvcMock.perform(post(URL_ACTION_SET_PAGE_SIZE)
				.flashAttr(BOOKS_COMMAND, aBooksCommand(anyOffset, pageSize, anySortColumn, anySortDirection)));

		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.current.size", equalTo(pageSize))));
	}

	private BooksCommand aBooksCommand(int offset, int size,
										ScrollSorterColumn sortColumn, ScrollSorterDirection sortDirection) {
		BooksCommand command = new BooksCommand();
		command.getScroll().getCurrent().setOffset(offset);
		command.getScroll().getCurrent().setSize(size);
		command.getScroll().getSorter().setColumn(sortColumn);
		command.getScroll().getSorter().setDirection(sortDirection);

		return command;
	}

}
