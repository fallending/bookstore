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
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;
import pl.jojczykp.bookstore.services.books.CreateBookService;

import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class CreateBookControllerComponentTest {

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;
	@Autowired private CreateBookService createBookService;

	private CreateBookCommand createBookCommand = new CreateBookCommand();
	private DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		reset(createBookService);
		given(createBookService.create(eq(createBookCommand), any(BindingResult.class)))
				.willReturn(displayBooksCommand);
	}

	@Test
	public void shouldUseService() throws Exception {
		whenControllerCreatePerformedWithCommand(createBookCommand);

		thenExpectServiceInvokedFor(createBookCommand);
	}

	@Test
	public void shouldRedirect() throws Exception {
		whenControllerCreatePerformedWithCommand(createBookCommand);

		thenExpectHttpRedirectWith(displayBooksCommand);
	}

	private void whenControllerCreatePerformedWithCommand(CreateBookCommand createBookCommand) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/books/create")
				.flashAttr("createBookCommand", createBookCommand));
	}

	private void thenExpectServiceInvokedFor(CreateBookCommand createBookCommand) {
		verify(createBookService).create(eq(createBookCommand), any(BindingResult.class));
		verifyNoMoreInteractions(createBookService);
	}

	private void thenExpectHttpRedirectWith(DisplayBooksCommand displayBooksCommand) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/display"))
				.andExpect(flash().attribute("displayBooksCommand",
						sameInstance(displayBooksCommand)));
	}

}
