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

package pl.jojczykp.bookstore.services.books;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import pl.jojczykp.bookstore.assemblers.CreateBookAssembler;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.validators.CreateBookValidator;

import java.util.HashMap;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class CreateBookServiceUnitTest {

	private static final String VALIDATOR_ERROR_MESSAGE = "An error message from validator.";

	@Mock private CreateBookValidator createBookValidator;
	@Mock private CreateBookAssembler createBookAssembler;
	@Mock private BooksRepository booksRepository;

	@InjectMocks private CreateBookService testee;

	@Captor private ArgumentCaptor<CreateBookCommand> createBookCommandCaptor;
	@Captor private ArgumentCaptor<Book> createdBookCaptor;

	@Mock private Book book;

	private BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "anObjectName");

	@Before
	public void setUp() {
		given(createBookAssembler.toDomain(any(CreateBookCommand.class))).willReturn(book);
	}

	@Test
	public void shouldCreateBook() throws Exception {
		final CreateBookCommand command = new CreateBookCommand();

		DisplayBooksCommand displayBooksCommand = testee.create(command, bindingResult);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainInvokedFor(command);
		thenExpectCreateInvokedOnRepository();
		thenExpectInfoOnlyMessage(displayBooksCommand, "Object created.");
		thenExpectPagerPropagated(command, displayBooksCommand);
	}

	@Test
	public void shouldFailOnCommandValidationError() throws Exception {
		final CreateBookCommand command = new CreateBookCommand();
		givenNegativeValidation();

		DisplayBooksCommand displayBooksCommand = testee.create(command, bindingResult);

		thenExpectValidationInvokedFor(command);
		thenExpectAssemblingCommandToDomainNotInvoked();
		thenExpectCreateNotInvokedOnRepository();
		thenExpectErrorOnlyMessage(displayBooksCommand, VALIDATOR_ERROR_MESSAGE);
	}

	private void givenNegativeValidation() {
		doAnswer(validationError())
				.when(createBookValidator).validate(anyObject(), any(Errors.class));
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

	private void thenExpectValidationInvokedFor(CreateBookCommand createBooksCommand) {
		verify(createBookValidator).validate(createBookCommandCaptor.capture(), any(Errors.class));
		assertThat(createBookCommandCaptor.getValue(), is(sameInstance(createBooksCommand)));
	}

	private void thenExpectAssemblingCommandToDomainInvokedFor(CreateBookCommand createBookCommand) {
		verify(createBookAssembler).toDomain(createBookCommandCaptor.capture());
		assertThat(createBookCommandCaptor.getValue(), is(sameInstance(createBookCommand)));
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

	private void thenExpectInfoOnlyMessage(DisplayBooksCommand displayBooksCommand, String message) {
		assertThat(displayBooksCommand.getMessages().getInfos(), contains(message));
		assertThat(displayBooksCommand.getMessages().getWarns(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getErrors(), emptyCollectionOf(String.class));
	}

	private void thenExpectErrorOnlyMessage(DisplayBooksCommand displayBooksCommand, String message) {
		assertThat(displayBooksCommand.getMessages().getInfos(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getWarns(), emptyCollectionOf(String.class));
		assertThat(displayBooksCommand.getMessages().getErrors(), contains(message));
	}

	private void thenExpectPagerPropagated(CreateBookCommand command, DisplayBooksCommand displayBooksCommand) {
		assertThat(displayBooksCommand.getPager(), is(sameInstance(command.getPager())));
	}

}
