/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package pl.jojczykp.bookstore.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.Errors;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.commands.ChangePagerCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.utils.PageSorterColumn;
import pl.jojczykp.bookstore.utils.PageSorterDirection;
import pl.jojczykp.bookstore.validators.BooksSetPageSizeValidator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectErrorOnlyFlashMessages;
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectInfoOnlyFlashMessages;
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectNoFlashMessages;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.DESC;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class BooksControllerPagerComponentTest {

	private static final String BOOKS_COMMAND = "booksCommand";

	private static final String URL_ACTION_SORT = "/books/sort";
	private static final String URL_ACTION_GO_TO_PAGE = "/books/goToPage";
	private static final String URL_ACTION_SET_PAGE_SIZE = "/books/setPageSize";

	private static final int PAGE_NUMBER = 2;
	private static final int PAGE_SIZE = 13;
	private static final int PAGES_COUNT = 4;
	private static final PageSorterColumn SORT_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection SORT_DIRECTION = ASC;
	private static final String VALIDATOR_ERROR_MESSAGE = "Negative or zero page size is not allowed. Defaults used.";

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BooksRepository booksRepository;
	@Autowired private BooksSetPageSizeValidator booksSetPageSizeValidator;
	@Autowired private WebApplicationContext wac;

	@Value("${view.books.defaultPageSize}") private int defaultPageSize;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		givenRepositoryMockConfigured();
		reset(booksSetPageSizeValidator);
	}

	@Test
	public void shouldSort() throws Exception {
		final PageSorterColumn sortColumn = BOOK_TITLE;
		final PageSorterDirection sortDirection = DESC;
		final ChangePagerCommand command = aChangeSortCommand(sortColumn, sortDirection);

		whenUrlActionPerformedWithCommand(URL_ACTION_SORT, command);

		thenExpectValidationNotInvoked();
		thenExpectSortedBy(sortColumn, sortDirection);
		thenExpectNoFlashMessages(mvcMockPerformResult);
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldSetPageSize() throws Exception {
		final int pageSize = 9;
		final ChangePagerCommand command = aChangePageSizeCommand(pageSize);

		whenUrlActionPerformedWithCommand(URL_ACTION_SET_PAGE_SIZE, command);

		thenExpectValidationInvoked();
		thenExpectPageSizeSetTo(pageSize);
		thenExpectInfoOnlyFlashMessages(mvcMockPerformResult, "Page size changed.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldSetPageSizeFailOnNotPositiveValue() throws Exception {
		final int pageSize = -1;
		final ChangePagerCommand command = aChangePageSizeCommand(pageSize);
		givenNegativeValidation();

		whenUrlActionPerformedWithCommand(URL_ACTION_SET_PAGE_SIZE, command);

		thenExpectValidationInvoked();
		thenExpectPageSizeSetTo(defaultPageSize);
		thenExpectErrorOnlyFlashMessages(mvcMockPerformResult, VALIDATOR_ERROR_MESSAGE);
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldGoToPage() throws Exception {
		final int pageNumber = 3;
		final ChangePagerCommand command = aPageNumberPagerCommand(pageNumber);

		whenUrlActionPerformedWithCommand(URL_ACTION_GO_TO_PAGE, command);

		thenExpectValidationNotInvoked();
		assertThatScrolledToPage(pageNumber);
		thenExpectNoFlashMessages(mvcMockPerformResult);
		thenExpectHttpRedirectWith(command);
	}

	private void givenRepositoryMockConfigured() {
		given(booksRepository.totalCount()).willReturn(PAGES_COUNT * PAGE_SIZE - 2);
	}

	private void whenUrlActionPerformedWithCommand(
											String action, ChangePagerCommand changePagerCommand) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post(action)
				.flashAttr("changePagerCommand", changePagerCommand));
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
				.when(booksSetPageSizeValidator).validate(anyObject(), any(Errors.class));
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

	private void thenExpectValidationInvoked() {
		verify(booksSetPageSizeValidator).validate(anyObject(), any(Errors.class));
		verifyNoMoreInteractions(booksSetPageSizeValidator);
	}

	private void thenExpectValidationNotInvoked() {
		verifyZeroInteractions(booksSetPageSizeValidator);
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

	private void thenExpectPageSizeSetTo(int pageSize) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.pageSize", equalTo(pageSize))));
	}

	private void thenExpectSortedBy(PageSorterColumn sortColumn, PageSorterDirection sortDirection) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.sorter.column", equalTo(sortColumn))))
				.andExpect(flash().attribute(BOOKS_COMMAND,
						hasBeanProperty("pager.sorter.direction", equalTo(sortDirection))));
	}

	private void thenExpectHttpRedirectWith(ChangePagerCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/read"))
				.andExpect(flash().attribute("booksCommand",
						hasBeanProperty("pager", sameInstance(command.getPager()))));
	}

}
