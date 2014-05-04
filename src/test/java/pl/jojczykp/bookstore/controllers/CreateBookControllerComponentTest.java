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
import pl.jojczykp.bookstore.assemblers.CreateBookAssembler;
import pl.jojczykp.bookstore.commands.CreateBookCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.BooksCreateValidator;

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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectErrorOnlyFlashMessages;
import static pl.jojczykp.bookstore.testutils.controllers.MessagesControllerTestUtils.thenExpectInfoOnlyFlashMessages;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class CreateBookControllerComponentTest {

	private static final String VALIDATOR_ERROR_MESSAGE = "An error message from validator.";

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private BooksCreateValidator booksCreateValidator;
	@Autowired private CreateBookAssembler createBookAssembler;
	@Autowired private BooksRepository booksRepository;
	@Autowired private WebApplicationContext wac;

	@Captor private ArgumentCaptor<CreateBookCommand> createBookCommandCaptor;
	@Captor private ArgumentCaptor<Book> createdBookCaptor;

	@Mock private Book book;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		MockitoAnnotations.initMocks(this);
		reset(booksCreateValidator);
		reset(createBookAssembler);
		reset(booksRepository);
		given(createBookAssembler.toDomain(any(CreateBookCommand.class))).willReturn(book);
	}

	@Test
	public void shouldCreateBook() throws Exception {
		final CreateBookCommand command = new CreateBookCommand();

		whenControllerCreatePerformedWithCommand(command);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainInvokedFor(command);
		thenExpectCreateInvokedOnRepository();
		thenExpectInfoOnlyFlashMessages(mvcMockPerformResult, "Object created.");
		thenExpectHttpRedirectWith(command);
	}

	@Test
	public void shouldFailOnCommandValidationError() throws Exception {
		final CreateBookCommand command = new CreateBookCommand();
		givenNegativeValidation();

		whenControllerCreatePerformedWithCommand(command);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainNotInvoked();
		thenExpectCreateNotInvokedOnRepository();
		thenExpectErrorOnlyFlashMessages(mvcMockPerformResult, VALIDATOR_ERROR_MESSAGE);
		thenExpectHttpRedirectWith(command);
	}

	private void givenNegativeValidation() {
		doAnswer(validationError())
				.when(booksCreateValidator).validate(anyObject(), any(Errors.class));
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

	private void whenControllerCreatePerformedWithCommand(CreateBookCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/create")
				.flashAttr("createBookCommand", command));
	}

	private void thenExpectValidationInvokedFor(CreateBookCommand createBooksCommand) {
		verify(booksCreateValidator).validate(createBookCommandCaptor.capture(), any(Errors.class));
		assertThat(createBookCommandCaptor.getValue(), is(sameInstance(createBooksCommand)));
		verifyNoMoreInteractions(booksCreateValidator);
	}

	private void thenExpectAssemblingCommandToDomainInvokedFor(CreateBookCommand createBookCommand) {
		verify(createBookAssembler).toDomain(createBookCommandCaptor.capture());
		assertThat(createBookCommandCaptor.getValue(), is(sameInstance(createBookCommand)));
		verifyNoMoreInteractions(createBookAssembler);
	}

	private void thenExpectAssemblingCommandToDomainNotInvoked() {
		verifyZeroInteractions(createBookAssembler);
	}

	private void thenExpectCreateInvokedOnRepository() {
		verify(booksRepository).create(createdBookCaptor.capture());
		assertThat(createdBookCaptor.getValue(), is(sameInstance(book)));
		verifyNoMoreInteractions(booksRepository);
	}

	private void thenExpectCreateNotInvokedOnRepository() {
		verifyZeroInteractions(booksRepository);
	}

	private void thenExpectHttpRedirectWith(CreateBookCommand command) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/display"))
				.andExpect(flash().attribute("displayBooksCommand",
						hasBeanProperty("pager", sameInstance(command.getPager()))));
	}

}
