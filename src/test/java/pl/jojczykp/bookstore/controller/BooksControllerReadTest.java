package pl.jojczykp.bookstore.controller;

import org.hamcrest.core.IsSame;
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
import pl.jojczykp.bookstore.assembler.BookAssembler;
import pl.jojczykp.bookstore.command.BookCommand;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.BooksCommandFactory;
import pl.jojczykp.bookstore.utils.PageParams;
import pl.jojczykp.bookstore.utils.PageParamsLimiter;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/assembler-mock-context.xml",
		"classpath:spring/page-params-limiter-mock-context.xml",
		"classpath:spring/beans-mock-context.xml"
})
public class BooksControllerReadTest {

	private static final String BOOKS_COMMAND = "booksCommand";
	private static final String URL_ACTION_READ = "/books/read";

	private static final int REPO_TOTAL_COUNT = 23;
	private static final int REPO_FIRST_RETURNED_RECORD_OFFSET = 14;
	private static final int REPO_RESULT_SIZE = 7;
	private static final List<Book> REPO_RESULT_DATA = new ArrayList<>();

	private static final List<BookCommand> ASSEMBLER_RESULT_DATA = new ArrayList<>();

	private static final int LIMITED_FIRST_RETURNED_RECORD_OFFSET = 16;
	private static final int LIMITED_RESULT_SIZE = 6;
	private static final PageSorterColumn SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection SORT_DIRECTION = PageSorterDirection.DESC;
	private static final PageParams LIMITED_PAGE_PARAMS = limitedPageParams();

	private static PageParams limitedPageParams() {
		PageParams pageParams = new PageParams();
		pageParams.setOffset(LIMITED_FIRST_RETURNED_RECORD_OFFSET);
		pageParams.setSize(LIMITED_RESULT_SIZE);

		return pageParams;
	}

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BooksCommandFactory booksCommandFactory;
	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private BookAssembler bookAssemblerMock;
	@Autowired private PageParamsLimiter pageParamsLimiterMock;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<List<Book>> assembledListCaptor;
	@Captor private ArgumentCaptor<PageParams> pageParamsCaptor;
	@Captor private ArgumentCaptor<Integer> totalCountCaptor;
	@Captor private ArgumentCaptor<Integer> offsetCaptor;
	@Captor private ArgumentCaptor<Integer> sizeCaptor;
	@Captor private ArgumentCaptor<PageSorterColumn> pageSorterColumnCaptor;
	@Captor private ArgumentCaptor<PageSorterDirection> pageSorterDirectionCaptor;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();

		MockitoAnnotations.initMocks(this);
		givenRepositoryMockConfigured();
		givenBookAssemblerMockConfigured();
		givenRepeatingPageParamsLimiterMockConfigured();
	}

	private void givenRepositoryMockConfigured() {
		reset(bookRepositoryMock);
		given(bookRepositoryMock.totalCount()).willReturn(REPO_TOTAL_COUNT);
		given(bookRepositoryMock
				.read(anyInt(), anyInt(), any(PageSorterColumn.class), any(PageSorterDirection.class)))
				.willReturn(REPO_RESULT_DATA);
	}

	private void givenBookAssemblerMockConfigured() {
		reset(bookAssemblerMock);
		given(bookAssemblerMock.toCommands(REPO_RESULT_DATA)).willReturn(ASSEMBLER_RESULT_DATA);
	}

	private void givenRepeatingPageParamsLimiterMockConfigured() {
		reset(pageParamsLimiterMock);
		given(pageParamsLimiterMock.limit(any(PageParams.class), anyInt())).willReturn(LIMITED_PAGE_PARAMS);
	}

	@Test
	public void shouldUseDefaultBooksCommandNoCommandPresent() throws Exception {
		final BooksCommand defaultCommand = new BooksCommand();
		given(booksCommandFactory.create()).willReturn(defaultCommand);

		mvcMock.perform(get(URL_ACTION_READ))
				.andExpect(status().isOk())
				.andExpect(model().attribute(BOOKS_COMMAND, is(sameInstance(defaultCommand))));
	}

	@Test
	public void shouldUseLimitationOfOffsetAndSizeParameters() throws Exception {
		whenControllerReadPerformedWithCommand();
		thenExpectParametersLimitationUsage();
	}

	@Test
	public void shouldDirectToProperViewWithModelCreatedForRepositoryData() throws Exception {
		whenControllerReadPerformedWithCommand();

		thenExpectBookRepositoryRead();
		thenExpectBookAssemblerInvoked();
		thenExpectCorrectViewSelectedAndModelSet();
	}

	private void whenControllerReadPerformedWithCommand() throws Exception {
		BooksCommand command = new BooksCommand();
		command.getPager().getCurrent().setOffset(REPO_FIRST_RETURNED_RECORD_OFFSET);
		command.getPager().getCurrent().setSize(REPO_RESULT_SIZE);
		command.getPager().getSorter().setColumn(SORT_COLUMN);
		command.getPager().getSorter().setDirection(SORT_DIRECTION);

		mvcMockPerformResult = mvcMock.perform(get(URL_ACTION_READ)
				.flashAttr(BOOKS_COMMAND, command));
	}

	private void thenExpectParametersLimitationUsage() {
		verify(pageParamsLimiterMock, times(1)).limit(pageParamsCaptor.capture(), totalCountCaptor.capture());
		assertThat(pageParamsCaptor.getValue().getOffset(), equalTo(REPO_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(pageParamsCaptor.getValue().getSize(), equalTo(REPO_RESULT_SIZE));
		assertThat(totalCountCaptor.getValue(), equalTo(REPO_TOTAL_COUNT));
	}

	private void thenExpectBookRepositoryRead() {
		verify(bookRepositoryMock).totalCount();
		verify(bookRepositoryMock).read(offsetCaptor.capture(), sizeCaptor.capture(),
				pageSorterColumnCaptor.capture(), pageSorterDirectionCaptor.capture());
		assertThat(offsetCaptor.getValue(), equalTo(LIMITED_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(sizeCaptor.getValue(), equalTo(LIMITED_RESULT_SIZE));
		assertThat(pageSorterColumnCaptor.getValue(), equalTo(SORT_COLUMN));
		assertThat(pageSorterDirectionCaptor.getValue(), equalTo(SORT_DIRECTION));
	}

	private void thenExpectBookAssemblerInvoked() {
		verify(bookAssemblerMock).toCommands(assembledListCaptor.capture());
		assertThat(assembledListCaptor.getValue(), IsSame.sameInstance(REPO_RESULT_DATA));
	}

	private void thenExpectCorrectViewSelectedAndModelSet() throws Exception {
		mvcMockPerformResult
			.andExpect(status().isOk())
			.andExpect(view().name("books"))
			.andExpect(model().attribute(BOOKS_COMMAND, hasProperty("books", sameInstance(ASSEMBLER_RESULT_DATA))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasProperty("newBook", instanceOf(BookCommand.class))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.totalCount", equalTo(
					REPO_TOTAL_COUNT))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.current.size", equalTo(
					REPO_RESULT_SIZE))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.current.offset", equalTo(
					REPO_FIRST_RETURNED_RECORD_OFFSET))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.limited.size", equalTo(
					LIMITED_RESULT_SIZE))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.limited.offset", equalTo(
					LIMITED_FIRST_RETURNED_RECORD_OFFSET))));
	}

}
