package pl.jojczykp.bookstore.services.books;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.commands.books.ChangePagerCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;
import pl.jojczykp.bookstore.validators.ChangePagerValidator;

import java.util.HashMap;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

@RunWith(MockitoJUnitRunner.class)
public class ChangeBooksPagerServiceUnitTest {

	private static final int PAGE_NUMBER = 2;
	private static final int PAGE_SIZE = 13;
	private static final int PAGES_COUNT = 4;
	private static final PageSorterColumn SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection SORT_DIRECTION = ASC;
	private static final String VALIDATOR_ERROR_MESSAGE = "Negative or zero page size is not allowed. Defaults used.";

	@Mock private BooksRepository booksRepository;
	@Mock private ChangePagerValidator changePagerValidator;
	@Mock private WebApplicationContext wac;

	@InjectMocks private ChangeBooksPagerService testee;

	@Value("${view.books.defaultPageSize}") private int defaultPageSize;

	private BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "anObjectName");

	@Before
	public void setUp() {
		given(booksRepository.totalCount()).willReturn(PAGES_COUNT * PAGE_SIZE - 2);
		reset(changePagerValidator);
	}

	@Test
	public void shouldSort() {
		final PageSorterColumn sortColumn = BOOK_TITLE;
		final PageSorterDirection sortDirection = DESC;
		final ChangePagerCommand command = aChangeSortCommand(sortColumn, sortDirection);

		DisplayBooksCommand displayBooksCommand = testee.sort(command, bindingResult);

		thenExpectValidationInvoked(command, bindingResult);
		thenExpectSortedBy(displayBooksCommand, sortColumn, sortDirection);
		thenExpectNoMessages(displayBooksCommand);
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	@Test
	public void shouldSetPageSize() {
		final int pageSize = 9;
		final ChangePagerCommand command = aChangePageSizeCommand(pageSize);

		DisplayBooksCommand displayBooksCommand = testee.setPageSize(command, bindingResult);

		thenExpectValidationInvoked(command, bindingResult);
		thenExpectPageSizeSetTo(displayBooksCommand, pageSize);
		thenExpectNoMessages(displayBooksCommand);
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	@Test
	public void shouldSetPageSizeFailAndBeSetToDefaultOnNotPositiveValue() {
		final int pageSize = -1;
		final ChangePagerCommand command = aChangePageSizeCommand(pageSize);
		givenNegativeValidation();

		DisplayBooksCommand displayBooksCommand = testee.sort(command, bindingResult);

		thenExpectValidationInvoked(command, bindingResult);
		thenExpectPageSizeSetTo(displayBooksCommand, defaultPageSize);
		thenExpectErrorOnlyMessages(displayBooksCommand, VALIDATOR_ERROR_MESSAGE);
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	@Test
	public void shouldGoToPage() {
		final int pageNumber = 3;
		final ChangePagerCommand command = aPageNumberPagerCommand(pageNumber);

		DisplayBooksCommand displayBooksCommand = testee.goToPage(command, bindingResult);

		assertThatScrolledToPage(displayBooksCommand, pageNumber);
		thenExpectNoMessages(displayBooksCommand);
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	private ChangePagerCommand aPageNumberPagerCommand(int pageNumber) {
		return aChangePagerCommand(pageNumber, PAGE_SIZE, PAGES_COUNT, SORT_COLUMN, SORT_DIRECTION);
	}

	private ChangePagerCommand aChangeSortCommand(PageSorterColumn sortColumn, PageSorterDirection sortDirection) {
		return aChangePagerCommand(PAGE_NUMBER, PAGE_SIZE, PAGES_COUNT, sortColumn, sortDirection);
	}

	private ChangePagerCommand aChangePageSizeCommand(int pageSize) {
		return aChangePagerCommand(PAGE_NUMBER, pageSize, PAGES_COUNT, SORT_COLUMN, SORT_DIRECTION);
	}

	private ChangePagerCommand aChangePagerCommand(int pageNumber, int pageSize, int pagesCount,
												   PageSorterColumn sortColumn, PageSorterDirection sortDirection) {
		ChangePagerCommand command = new ChangePagerCommand();
		command.getPager().setPageNumber(pageNumber);
		command.getPager().setPageSize(pageSize);
		command.getPager().setPagesCount(pagesCount);
		command.getPager().getSorter().setColumn(sortColumn);
		command.getPager().getSorter().setDirection(sortDirection);

		return command;
	}

	private void givenNegativeValidation() {
		doAnswer(validationError())
				.when(changePagerValidator).validate(anyObject(), any(Errors.class));
	}

	private Answer<Void> validationError() {
		return new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) {
				Errors errors = (Errors) invocation.getArguments()[1];
				errors.rejectValue("pager.pageSize", "pager.pageSize.notPositive", VALIDATOR_ERROR_MESSAGE);
				return null;
			}
		};
	}

	private void thenExpectValidationInvoked(ChangePagerCommand changePagerCommand, BindingResult bindingResult) {
		verify(changePagerValidator).validate(changePagerCommand, bindingResult);
		verifyNoMoreInteractions(changePagerValidator);
	}

	private void thenExpectErrorOnlyMessages(DisplayBooksCommand displayBooksCommand, String... messages) {
		assertThat(displayBooksCommand.getMessages().getInfos(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getWarns(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getErrors(), contains(messages));
	}

	private void thenExpectNoMessages(DisplayBooksCommand displayBooksCommand) {
		assertThat(displayBooksCommand.getMessages().getInfos(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getWarns(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getErrors(), emptyCollectionOf(String.class));
	}

	private void assertThatScrolledToPage(DisplayBooksCommand displayBooksCommand, int pageNumber) {
		assertThat(displayBooksCommand.getPager().getPageNumber(), is(equalTo(pageNumber)));
		assertThat(displayBooksCommand.getPager().getPageSize(), is(equalTo(PAGE_SIZE)));
		assertThat(displayBooksCommand.getPager().getPagesCount(), is(equalTo(PAGES_COUNT)));
	}

	private void thenExpectPageSizeSetTo(DisplayBooksCommand displayBooksCommand, int pageSize) {
		assertThat(displayBooksCommand.getPager().getPageSize(), is(equalTo(pageSize)));
	}

	private void thenExpectSortedBy(DisplayBooksCommand displayBooksCommand,
									PageSorterColumn sortColumn, PageSorterDirection sortDirection) {
		assertThat(displayBooksCommand.getPager().getSorter().getColumn(), is(equalTo(sortColumn)));
		assertThat(displayBooksCommand.getPager().getSorter().getDirection(), is(equalTo(sortDirection)));
	}

	private void thenExpectPagerPropagated(ChangePagerCommand command, DisplayBooksCommand displayBooksCommand) {
		assertThat(displayBooksCommand.getPager(), is(sameInstance(command.getPager())));
	}

}
