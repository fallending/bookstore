package pl.jojczykp.bookstore.services.books;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jojczykp.bookstore.assemblers.DisplayBookAssembler;
import pl.jojczykp.bookstore.commands.books.DisplayBookCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.common.MessagesCommand;
import pl.jojczykp.bookstore.commands.common.PagerCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;
import pl.jojczykp.bookstore.utils.PagerLimiter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.reset;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

@RunWith(MockitoJUnitRunner.class)
public class DisplayBooksServiceUnitTest {

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

	private static final List<DisplayBookCommand> ASSEMBLER_RESULT_DATA = new ArrayList<>();

	@Mock private BooksRepository booksRepository;
	@Mock private PagerLimiter pagerLimiter;
	@Mock private DisplayBookAssembler displayBookAssembler;

	@InjectMocks private DisplayBooksService testee;

	@Captor private ArgumentCaptor<List<Book>> assembledListCaptor;
	@Captor private ArgumentCaptor<PagerCommand> pagerCommandCaptor;
	@Captor private ArgumentCaptor<Integer> totalCountCaptor;
	@Captor private ArgumentCaptor<Integer> offsetCaptor;
	@Captor private ArgumentCaptor<Integer> sizeCaptor;
	@Captor private ArgumentCaptor<PageSorterColumn> pageSorterColumnCaptor;
	@Captor private ArgumentCaptor<PageSorterDirection> pageSorterDirectionCaptor;

	@Before
	public void setUp() {
		givenRepositoryMockConfigured();
		givenRepeatingPageParamsLimiterMockConfigured();
		givenBookAssemblerMockConfigured();
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
		reset(displayBookAssembler);
		given(displayBookAssembler.toCommands(REPO_DATA)).willReturn(ASSEMBLER_RESULT_DATA);
	}

	@Test
	public void shouldDisplay() {
		final List<String> infos = asList("info1", "info2", "info3");
		final List<String> warns = asList("warn1", "warn2", "warn3");
		final List<String> errors = asList("error1", "error2", "error3");
		DisplayBooksCommand command = aBooksCommand(aMessagesCommand(infos, warns, errors), aPagerCommand());

		DisplayBooksCommand displayBooksCommand = testee.display(command);

		assertThat(displayBooksCommand, is(sameInstance(command)));
		assertThat(displayBooksCommand.getBooks(), is(sameInstance(ASSEMBLER_RESULT_DATA)));
		assertThat(displayBooksCommand.getPager().getPageNumber(), is(equalTo(LIMITED_PAGE_NUMBER)));
		assertThat(displayBooksCommand.getPager().getPageSize(), is(equalTo(LIMITED_PAGE_SIZE)));
		assertThat(displayBooksCommand.getPager().getPagesCount(), is(equalTo(LIMITED_PAGES_COUNT)));
		assertThat(displayBooksCommand.getPager().getTotalCount(), is(equalTo(LIMITED_TOTAL_COUNT)));
		assertThat(displayBooksCommand.getPager().getSorter().getColumn(), is(equalTo(LIMITED_SORT_COLUMN)));
		assertThat(displayBooksCommand.getPager().getSorter().getDirection(), is(equalTo(LIMITED_SORT_DIRECTION)));
		assertThat(displayBooksCommand.getMessages().getInfos(), is(equalTo(infos)));
		assertThat(displayBooksCommand.getMessages().getWarns(), is(equalTo(warns)));
		assertThat(displayBooksCommand.getMessages().getErrors(), is(equalTo(errors)));
	}

	private static DisplayBooksCommand aBooksCommand(MessagesCommand messagesCommand, PagerCommand pagerCommand) {
		DisplayBooksCommand result = new DisplayBooksCommand();
		result.setMessages(messagesCommand);
		result.setPager(pagerCommand);

		return result;
	}

	private static MessagesCommand aMessagesCommand(List<String> infos, List<String> warns, List<String> errors) {
		MessagesCommand result = new MessagesCommand();
		result.addInfos(infos.toArray(new String[infos.size()]));
		result.addWarns(warns.toArray(new String[warns.size()]));
		result.addErrors(errors.toArray(new String[errors.size()]));

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

}
