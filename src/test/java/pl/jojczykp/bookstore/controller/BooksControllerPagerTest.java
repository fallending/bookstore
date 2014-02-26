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
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/book-command-validator-mock-context.xml",
		"classpath:spring/books-command-factory-mock-context.xml"
})
public class BooksControllerPagerTest {

	private static final String BOOKS_COMMAND = "booksCommand";

	private static final String URL_ACTION_SORT = "/books/sort";
	private static final String URL_ACTION_GO_TO_PAGE = "/books/goToPage";
	private static final String URL_ACTION_SET_PAGE_SIZE = "/books/setPageSize";

	private static final int PAGE_NUMBER = 2;
	private static final int PAGE_SIZE = 13;
	private static final int PAGES_COUNT = 4;
	private PageSorterColumn SORT_COLUMN = BOOK_TITLE;
	private PageSorterDirection SORT_DIRECTION = ASC;

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BookRepository bookRepository;
	@Autowired private WebApplicationContext wac;

	@Before
	public void setUp() {
		given(bookRepository.totalCount()).willReturn(PAGES_COUNT * PAGE_SIZE - 2);
		mvcMock = webAppContextSetup(wac).build();
	}

	@Test
	public void shouldSort() throws Exception {
		final PageSorterColumn sortColumn = BOOK_TITLE;
		final PageSorterDirection sortDirection = DESC;

		performUrlAction(URL_ACTION_SORT, aSortBooksCommand(sortColumn, sortDirection));

		assertThatSortedBy(sortColumn, sortDirection);
	}

	@Test
	public void shouldSetPageSize() throws Exception {
		final int pageSize = 9;

		performUrlAction(URL_ACTION_SET_PAGE_SIZE, aPageSizeBooksCommand(pageSize));

		assertThatPageSizeSetTo(pageSize);
	}

	@Test
	public void shouldGoToPage() throws Exception {
		final int pageNumber = 3;

		performUrlAction(URL_ACTION_GO_TO_PAGE, aPageNumberBooksCommand(pageNumber));

		assertThatScrolledToPage(pageNumber);
	}

	private void performUrlAction(String action, BooksCommand bookCommand) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post(action)
				.flashAttr(BOOKS_COMMAND, bookCommand));
	}

	private BooksCommand aPageNumberBooksCommand(int pageNumber) {
		return aBooksCommand(pageNumber, PAGE_SIZE, PAGES_COUNT, SORT_COLUMN, SORT_DIRECTION);
	}

	private BooksCommand aSortBooksCommand(PageSorterColumn sortColumn, PageSorterDirection sortDirection) {
		return aBooksCommand(PAGE_NUMBER, PAGE_SIZE, PAGES_COUNT, sortColumn, sortDirection);
	}

	private BooksCommand aPageSizeBooksCommand(int pageSize) {
		return aBooksCommand(PAGE_NUMBER, pageSize, PAGES_COUNT, SORT_COLUMN, SORT_DIRECTION);
	}

	private BooksCommand aBooksCommand(int pageNumber, int pageSize, int pagesCount,
									   PageSorterColumn sortColumn, PageSorterDirection sortDirection) {
		BooksCommand command = new BooksCommand();
		command.getPager().setPageNumber(pageNumber);
		command.getPager().setPageSize(pageSize);
		command.getPager().setPagesCount(pagesCount);
		command.getPager().getSorter().setColumn(sortColumn);
		command.getPager().getSorter().setDirection(sortDirection);

		return command;
	}

	private void assertThatScrolledToPage(int pageNumber) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.pageNumber", equalTo(pageNumber))))
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.pageSize", equalTo(PAGE_SIZE))))
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.pagesCount", equalTo(PAGES_COUNT))));
	}

	private void assertThatPageSizeSetTo(int pageSize) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.pageSize", equalTo(pageSize))));
	}

	private void assertThatSortedBy(PageSorterColumn sortColumn, PageSorterDirection sortDirection) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.sorter.column", equalTo(sortColumn))))
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.sorter.direction", equalTo(sortDirection))));
	}

}