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
import pl.jojczykp.bookstore.commands.books.DisplayBookCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.services.books.DisplayBooksService;
import pl.jojczykp.bookstore.utils.BooksCommandFactory;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class DisplayBooksControllerComponentTest {

	private static final String DISPLAY_BOOKS_COMMAND = "displayBooksCommand";
	private static final String URL_ACTION_DISPLAY = "/books/display";

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;
	@Autowired private BooksCommandFactory booksCommandFactory;
	@Autowired private DisplayBooksService displayBooksService;

	private DisplayBooksCommand givenDisplayBooksCommand = new DisplayBooksCommand();
	private DisplayBooksCommand defaultDisplayBooksCommand = new DisplayBooksCommand();
	private DisplayBooksCommand resultDisplayBooksCommand = new DisplayBooksCommand();

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		reset(displayBooksService);
		reset(booksCommandFactory);
		given(booksCommandFactory.create()).willReturn(defaultDisplayBooksCommand);
	}

	@Test
	public void shouldUseServiceWithGivenBooksWhenGiven() throws Exception {
		given(displayBooksService.display(givenDisplayBooksCommand)).willReturn(resultDisplayBooksCommand);

		mvcMockPerformResult = mvcMock.perform(get(URL_ACTION_DISPLAY)
				.flashAttr(DISPLAY_BOOKS_COMMAND, givenDisplayBooksCommand));

		thenExpectServiceInvokedFor(givenDisplayBooksCommand);
		thenExpectCorrectViewSelectedAndModelSetWith(resultDisplayBooksCommand.getBooks());
	}

	@Test
	public void shouldUseServiceWithDefaultBooksWhenNotGiven() throws Exception {
		given(displayBooksService.display(defaultDisplayBooksCommand)).willReturn(resultDisplayBooksCommand);

		mvcMockPerformResult = mvcMock.perform(get(URL_ACTION_DISPLAY));

		thenExpectServiceInvokedFor(defaultDisplayBooksCommand);
		thenExpectCorrectViewSelectedAndModelSetWith(resultDisplayBooksCommand.getBooks());
	}

	private void thenExpectServiceInvokedFor(DisplayBooksCommand command) {
		verify(displayBooksService).display(eq(command));
		verifyNoMoreInteractions(displayBooksService);
	}

	private void thenExpectCorrectViewSelectedAndModelSetWith(List<DisplayBookCommand> books) throws Exception {
		mvcMockPerformResult
			.andExpect(status().isOk())
			.andExpect(view().name("books"))
			.andExpect(model().attribute(DISPLAY_BOOKS_COMMAND, hasProperty("books", is(sameInstance(books)))));
	}

}
