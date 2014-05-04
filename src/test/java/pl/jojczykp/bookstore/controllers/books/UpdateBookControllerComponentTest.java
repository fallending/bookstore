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

package pl.jojczykp.bookstore.controllers.books;

import org.hibernate.StaleObjectStateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.Errors;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.assemblers.UpdateBookAssembler;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.UpdateBookValidator;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
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
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectWarnOnlyFlashMessages;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class UpdateBookControllerComponentTest {

	private static final String VALIDATOR_ERROR_MESSAGE = "An error message from validator.";

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private UpdateBookValidator updateBookValidator;
	@Autowired private UpdateBookAssembler updateBookAssembler;
	@Autowired private BooksRepository booksRepository;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<UpdateBookCommand> updateBookCommandCaptor;
	@Captor private ArgumentCaptor<Book> updatedBookCaptor;

	@Mock private StaleObjectStateException staleObjectStateException;
	@Mock private Book book;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		MockitoAnnotations.initMocks(this);
		reset(updateBookValidator);
		reset(updateBookAssembler);
		reset(booksRepository);
		given(updateBookAssembler.toDomain(any(UpdateBookCommand.class))).willReturn(book);
	}

	@Test
	public void shouldUpdateBook() throws Exception {
		final UpdateBookCommand command = new UpdateBookCommand();

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainInvokedFor(command);
		thenExpectUpdateInvokedOnRepository();
		thenExpectInfoOnlyFlashMessages(mvcMockPerformResult, "Object updated.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldFailConcurrentlyUpdatingUpdatedBook() throws Exception {
		final UpdateBookCommand command = new UpdateBookCommand();
		givenObjectConcurrentlyUpdated();

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainInvokedFor(command);
		thenExpectUpdateInvokedOnRepository();
		thenExpectWarnOnlyFlashMessages(mvcMockPerformResult,
				"Object updated or deleted by another user. Please try again with actual data.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldFailOnCommandValidationError() throws Exception {
		final UpdateBookCommand command = new UpdateBookCommand();
		givenNegativeValidation();

		whenControllerUpdatePerformedWithCommand(command);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainNotInvoked();
		thenExpectUpdateNotInvokedOnRepository();
		thenExpectErrorOnlyFlashMessages(mvcMockPerformResult, VALIDATOR_ERROR_MESSAGE);
		thenExpectHttpRedirectWith(command);
	}

	private void givenObjectConcurrentlyUpdated() {
		doThrow(staleObjectStateException).when(booksRepository).update(any(Book.class));
	}

	private void givenNegativeValidation() {
		doAnswer(validationError())
				.when(updateBookValidator).validate(anyObject(), any(Errors.class));
	}

	private Answer<Void> validationError() {
		return new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) {
				Errors errors = (Errors) invocation.getArguments()[1];
				errors.rejectValue("title", "title.empty", VALIDATOR_ERROR_MESSAGE);
				return null;
			}
		};
	}

	private void whenControllerUpdatePerformedWithCommand(UpdateBookCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/update")
				.flashAttr("updateBookCommand", command));
	}

	private void thenExpectValidationInvokedFor(UpdateBookCommand updateBookCommand) {
		verify(updateBookValidator).validate(updateBookCommandCaptor.capture(), any(Errors.class));
		assertThat(updateBookCommandCaptor.getValue(), is(sameInstance(updateBookCommand)));
		verifyNoMoreInteractions(updateBookValidator);
	}

	private void thenExpectAssemblingCommandToDomainInvokedFor(UpdateBookCommand updateBookCommand) {
		verify(updateBookAssembler).toDomain(updateBookCommandCaptor.capture());
		assertThat(updateBookCommandCaptor.getValue(), is(sameInstance(updateBookCommand)));
		verifyNoMoreInteractions(updateBookAssembler);
	}

	private void thenExpectAssemblingCommandToDomainNotInvoked() {
		verifyZeroInteractions(updateBookAssembler);
	}

	private void thenExpectUpdateInvokedOnRepository() {
		verify(booksRepository).update(updatedBookCaptor.capture());
		assertThat(updatedBookCaptor.getValue(), is(sameInstance(book)));
		verifyNoMoreInteractions(booksRepository);
	}

	private void thenExpectUpdateNotInvokedOnRepository() {
		verifyZeroInteractions(booksRepository);
	}

	private void thenExpectHttpRedirectWith(UpdateBookCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/display"))
				.andExpect(flash().attribute("displayBooksCommand",
						hasBeanProperty("pager", sameInstance(command.getPager()))));
	}

}
