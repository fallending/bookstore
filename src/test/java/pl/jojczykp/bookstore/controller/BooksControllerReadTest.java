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
import pl.jojczykp.bookstore.assembler.BookAssembler;
import pl.jojczykp.bookstore.command.BookCommand;
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.command.MessagesCommand;
import pl.jojczykp.bookstore.command.PagerCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BooksRepository;
import pl.jojczykp.bookstore.utils.BooksCommandFactory;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;
import pl.jojczykp.bookstore.utils.PagerLimiter;

import java.util.ArrayList;
import java.util.List;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static java.util.Arrays.asList;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.controller.MessagesControllerTestUtils.thenExpectModelMessages;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class BooksControllerReadTest {

	private static final String BOOKS_COMMAND = "booksCommand";
	private static final String URL_ACTION_READ = "/books/read";

	private static final List<Book> REPO_DATA = new ArrayList<>();
	private static final int REPO_TOTAL_COUNT = 23;

	private static final int REQUESTED_PAGE_SIZE = 7;
	private static final int REQUESTED_PAGE_NUMBER = 14;
	private static final int REQUESTED_PAGES_COUNT = 234;
	private static final int REQUESTED_TOTAL_COUNT = 2;
	private static final PageSorterColumn REQUESTED_SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection REQUESTED_SORT_DIRECTION = DESC;

	private static final int LIMITED_PAGE_SIZE = 6;
	private static final int LIMITED_PAGE_NUMBER = 34;
	private static final int LIMITED_PAGES_COUNT = 4;
	private static final int LIMITED_TOTAL_COUNT = 543;
	private static final PageSorterColumn LIMITED_SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection LIMITED_SORT_DIRECTION = ASC;

	private static final List<BookCommand> ASSEMBLER_RESULT_DATA = new ArrayList<>();

	private MockMvc mvcMock;

	private ResultActions mvcMockPerformResult;

	@Autowired private BooksCommandFactory booksCommandFactory;
	@Autowired private BooksRepository booksRepository;
	@Autowired private BookAssembler bookAssembler;
	@Autowired private PagerLimiter pagerLimiter;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<List<Book>> assembledListCaptor;
	@Captor private ArgumentCaptor<PagerCommand> pagerCommandCaptor;
	@Captor private ArgumentCaptor<Integer> totalCountCaptor;
	@Captor private ArgumentCaptor<Integer> offsetCaptor;
	@Captor private ArgumentCaptor<Integer> sizeCaptor;
	@Captor private ArgumentCaptor<PageSorterColumn> pageSorterColumnCaptor;
	@Captor private ArgumentCaptor<PageSorterDirection> pageSorterDirectionCaptor;

	private BooksCommand defaultCommand = new BooksCommand();

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();

		MockitoAnnotations.initMocks(this);

		givenBooksCommandFactoryMockConfigured();
		givenRepositoryMockConfigured();
		givenRepeatingPageParamsLimiterMockConfigured();
		givenBookAssemblerMockConfigured();
	}

	private void givenBooksCommandFactoryMockConfigured() {
		reset(booksCommandFactory);
		given(booksCommandFactory.create()).willReturn(defaultCommand);
	}

	private void givenRepositoryMockConfigured() {
		reset(booksRepository);
		given(booksRepository.totalCount()).willReturn(REPO_TOTAL_COUNT);
		given(booksRepository
				.read(anyInt(), anyInt(), any(PageSorterColumn.class), any(PageSorterDirection.class)))
				.willReturn(REPO_DATA);
	}

	private void givenRepeatingPageParamsLimiterMockConfigured() {
		reset(pagerLimiter);
		given(pagerLimiter.createLimited(any(PagerCommand.class), anyInt())).willReturn(aLimitedPager());
	}

	private void givenBookAssemblerMockConfigured() {
		reset(bookAssembler);
		given(bookAssembler.toCommands(REPO_DATA)).willReturn(ASSEMBLER_RESULT_DATA);
	}

	@Test
	public void shouldUseDefaultBooksCommandWhenNoCommandPresent() throws Exception {
		whenControllerReadPerformedWithNoCommand();

		thenExpectBooksCommandFactoryInvoked();
		thenExpectProcessedCommandInstance(defaultCommand);
	}

	@Test
	public void shouldWalkThroughComponentsAndReturnData() throws Exception {
		BooksCommand command = aBooksCommandWithPager();

		whenControllerReadPerformedWith(command);

		thenExpectBooksCommandFactoryNotInvoked();
		thenExpectProcessedCommandInstance(command);
		thenExpectBooksRepositoryTotalCountTaken();
		thenExpectParametersLimitationUsage();
		thenExpectBooksRepositoryRead();
		thenExpectBookAssemblerInvoked();
		thenExpectCorrectViewSelectedAndModelSet();
	}

	@Test
	public void shouldPropagateMessages() throws Exception {
		final List<String> infos = asList("info1", "info2", "info3");
		final List<String> warns = asList("warn1", "warn2", "warn3");
		final List<String> errors = asList("error1", "error2", "error3");
		BooksCommand command = aBooksCommandWithMessages(infos, warns, errors);

		whenControllerReadPerformedWith(command);

		thenExpectModelMessages(mvcMockPerformResult, infos, warns, errors);
	}

	private void whenControllerReadPerformedWithNoCommand() throws Exception {
		mvcMockPerformResult = mvcMock.perform(get(URL_ACTION_READ));
	}

	private void whenControllerReadPerformedWith(BooksCommand booksCommand) throws Exception {
		mvcMockPerformResult = mvcMock.perform(get(URL_ACTION_READ)
				.flashAttr(BOOKS_COMMAND, booksCommand));
	}

	private ResultActions thenExpectProcessedCommandInstance(BooksCommand command) throws Exception {
		return mvcMockPerformResult
				.andExpect(model().attribute(BOOKS_COMMAND,
						is(sameInstance(command))));
	}

	private void thenExpectBooksCommandFactoryInvoked() {
		verify(booksCommandFactory).create();
		verifyNoMoreInteractions(booksCommandFactory);
	}

	private void thenExpectBooksCommandFactoryNotInvoked() {
		verifyZeroInteractions(booksCommandFactory);
	}

	private void thenExpectBooksRepositoryTotalCountTaken() {
		verify(booksRepository).totalCount();
	}

	private void thenExpectParametersLimitationUsage() {
		verify(pagerLimiter).createLimited(pagerCommandCaptor.capture(), totalCountCaptor.capture());
		assertPagersEqual(pagerCommandCaptor.getValue(), aPagerCommand());
		assertThat(totalCountCaptor.getValue(), equalTo(REPO_TOTAL_COUNT));
		verifyNoMoreInteractions(pagerLimiter);
	}

	private void thenExpectBooksRepositoryRead() {
		verify(booksRepository, times(1)).read(
				offsetCaptor.capture(),
				sizeCaptor.capture(),
				pageSorterColumnCaptor.capture(),
				pageSorterDirectionCaptor.capture());

		assertThat(offsetCaptor.getValue(), equalTo((LIMITED_PAGE_NUMBER - 1) * LIMITED_PAGE_SIZE));
		assertThat(sizeCaptor.getValue(), equalTo(LIMITED_PAGE_SIZE));
		assertThat(pageSorterColumnCaptor.getValue(), equalTo(LIMITED_SORT_COLUMN));
		assertThat(pageSorterDirectionCaptor.getValue(), equalTo(LIMITED_SORT_DIRECTION));

		verifyNoMoreInteractions(booksRepository);
	}

	private void thenExpectBookAssemblerInvoked() {
		verify(bookAssembler).toCommands(assembledListCaptor.capture());
		assertThat(assembledListCaptor.getValue(), sameInstance(REPO_DATA));
		verifyNoMoreInteractions(bookAssembler);
	}

	private void thenExpectCorrectViewSelectedAndModelSet() throws Exception {
		mvcMockPerformResult
			.andExpect(status().isOk())
			.andExpect(view().name("books"))
			.andExpect(model().attribute(BOOKS_COMMAND, hasProperty("books", is(sameInstance(ASSEMBLER_RESULT_DATA)))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasProperty("newBook", instanceOf(BookCommand.class))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.pageNumber", equalTo(
					LIMITED_PAGE_NUMBER))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.pageSize", equalTo(
					LIMITED_PAGE_SIZE))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.pagesCount", equalTo(
					LIMITED_PAGES_COUNT))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("pager.totalCount", equalTo(
					LIMITED_TOTAL_COUNT))));
	}

	private static BooksCommand aBooksCommandWithMessages(List<String> infos, List<String> warns, List<String> errors) {
		BooksCommand result = new BooksCommand();
		result.setMessages(aMessagesCommand(infos, warns, errors));

		return result;
	}

	private static MessagesCommand aMessagesCommand(List<String> infos, List<String> warns, List<String> errors) {
		MessagesCommand result = new MessagesCommand();
		result.addInfos(infos.toArray(new String[infos.size()]));
		result.addWarns(warns.toArray(new String[warns.size()]));
		result.addErrors(errors.toArray(new String[errors.size()]));

		return result;
	}

	private static BooksCommand aBooksCommandWithPager() {
		BooksCommand result = new BooksCommand();
		result.setPager(aPagerCommand());

		return result;
	}

	private static PagerCommand aPagerCommand() {
		PagerCommand result = new PagerCommand();
		result.setPageNumber(REQUESTED_PAGE_NUMBER);
		result.setPageSize(REQUESTED_PAGE_SIZE);
		result.setPagesCount(REQUESTED_PAGES_COUNT);
		result.setTotalCount(REQUESTED_TOTAL_COUNT);
		result.getSorter().setColumn(REQUESTED_SORT_COLUMN);
		result.getSorter().setDirection(REQUESTED_SORT_DIRECTION);

		return result;
	}

	private static PagerCommand aLimitedPager() {
		PagerCommand result = new PagerCommand();
		result.setPageNumber(LIMITED_PAGE_NUMBER);
		result.setPageSize(LIMITED_PAGE_SIZE);
		result.setPagesCount(LIMITED_PAGES_COUNT);
		result.setTotalCount(LIMITED_TOTAL_COUNT);
		result.getSorter().setColumn(LIMITED_SORT_COLUMN);
		result.getSorter().setDirection(LIMITED_SORT_DIRECTION);

		return result;
	}

	private static void assertPagersEqual(PagerCommand first, PagerCommand second) {
		assertThat(deepEquals(first, second), is(true));
	}

}
