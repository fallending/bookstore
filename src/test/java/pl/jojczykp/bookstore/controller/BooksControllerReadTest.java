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
import pl.jojczykp.bookstore.utils.ScrollParams;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;
import pl.jojczykp.bookstore.utils.ScrollSorterColumn;
import pl.jojczykp.bookstore.utils.ScrollSorterDirection;

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
import static pl.jojczykp.bookstore.utils.ScrollSorterColumn.BOOK_TITLE;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/assembler-mock-context.xml",
		"classpath:spring/scroll-params-limiter-mock-context.xml",
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
	private static final ScrollSorterColumn SORT_COLUMN = BOOK_TITLE;
	private static final ScrollSorterDirection SORT_DIRECTION = ScrollSorterDirection.DESC;
	private static final ScrollParams LIMITED_SCROLL_PARAMS = limitedScrollParams();

	private static ScrollParams limitedScrollParams() {
		ScrollParams scrollParams = new ScrollParams();
		scrollParams.setOffset(LIMITED_FIRST_RETURNED_RECORD_OFFSET);
		scrollParams.setSize(LIMITED_RESULT_SIZE);

		return scrollParams;
	}

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BooksCommandFactory booksCommandFactory;
	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private BookAssembler bookAssemblerMock;
	@Autowired private ScrollParamsLimiter scrollParamsLimiterMock;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<List<Book>> assembledListCaptor;
	@Captor private ArgumentCaptor<ScrollParams> scrollParamsCaptor;
	@Captor private ArgumentCaptor<Integer> totalCountCaptor;
	@Captor private ArgumentCaptor<Integer> offsetCaptor;
	@Captor private ArgumentCaptor<Integer> sizeCaptor;
	@Captor private ArgumentCaptor<ScrollSorterColumn> scrollSorterColumnCaptor;
	@Captor private ArgumentCaptor<ScrollSorterDirection> scrollSorterDirectionCaptor;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();

		MockitoAnnotations.initMocks(this);
		givenRepositoryMockConfigured();
		givenBookAssemblerMockConfigured();
		givenRepeatingScrollParamsLimiterMockConfigured();
	}

	private void givenRepositoryMockConfigured() {
		reset(bookRepositoryMock);
		given(bookRepositoryMock.totalCount()).willReturn(REPO_TOTAL_COUNT);
		given(bookRepositoryMock
				.read(anyInt(), anyInt(), any(ScrollSorterColumn.class), any(ScrollSorterDirection.class)))
				.willReturn(REPO_RESULT_DATA);
	}

	private void givenBookAssemblerMockConfigured() {
		reset(bookAssemblerMock);
		given(bookAssemblerMock.toCommands(REPO_RESULT_DATA)).willReturn(ASSEMBLER_RESULT_DATA);
	}

	private void givenRepeatingScrollParamsLimiterMockConfigured() {
		reset(scrollParamsLimiterMock);
		given(scrollParamsLimiterMock.limit(any(ScrollParams.class), anyInt())).willReturn(LIMITED_SCROLL_PARAMS);
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
		command.getScroll().getCurrent().setOffset(REPO_FIRST_RETURNED_RECORD_OFFSET);
		command.getScroll().getCurrent().setSize(REPO_RESULT_SIZE);
		command.getScroll().getSorter().setColumn(SORT_COLUMN);
		command.getScroll().getSorter().setDirection(SORT_DIRECTION);

		mvcMockPerformResult = mvcMock.perform(get(URL_ACTION_READ)
				.flashAttr(BOOKS_COMMAND, command));
	}

	private void thenExpectParametersLimitationUsage() {
		verify(scrollParamsLimiterMock, times(1)).limit(scrollParamsCaptor.capture(), totalCountCaptor.capture());
		assertThat(scrollParamsCaptor.getValue().getOffset(), equalTo(REPO_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(scrollParamsCaptor.getValue().getSize(), equalTo(REPO_RESULT_SIZE));
		assertThat(totalCountCaptor.getValue(), equalTo(REPO_TOTAL_COUNT));
	}

	private void thenExpectBookRepositoryRead() {
		verify(bookRepositoryMock).totalCount();
		verify(bookRepositoryMock).read(offsetCaptor.capture(), sizeCaptor.capture(),
				scrollSorterColumnCaptor.capture(), scrollSorterDirectionCaptor.capture());
		assertThat(offsetCaptor.getValue(), equalTo(LIMITED_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(sizeCaptor.getValue(), equalTo(LIMITED_RESULT_SIZE));
		assertThat(scrollSorterColumnCaptor.getValue(), equalTo(SORT_COLUMN));
		assertThat(scrollSorterDirectionCaptor.getValue(), equalTo(SORT_DIRECTION));
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
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.totalCount", equalTo(
					REPO_TOTAL_COUNT))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.current.size", equalTo(
					REPO_RESULT_SIZE))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.current.offset", equalTo(
					REPO_FIRST_RETURNED_RECORD_OFFSET))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.limited.size", equalTo(
					LIMITED_RESULT_SIZE))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.limited.offset", equalTo(
					LIMITED_FIRST_RETURNED_RECORD_OFFSET))));
	}

}
