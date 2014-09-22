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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.commands.books.ChangePagerCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.services.books.ChangeBooksPagerService;

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
public class ChangeBooksPagerControllerComponentTest {

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;
	@Autowired private ChangeBooksPagerService changeBooksPagerService;

	@Value("${view.books.defaultPageSize}") private int defaultPageSize;

	private ChangePagerCommand changePagerCommand = new ChangePagerCommand();
	private DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		reset(changeBooksPagerService);
	}

	@Test
	public void shouldSort() throws Exception {
		given(changeBooksPagerService.sort(eq(changePagerCommand), any(BindingResult.class)))
				.willReturn(displayBooksCommand);

		whenServicePerformedWithUrlAndCommand("/books/sort", changePagerCommand);

		thenExpectServiceSortInvokedFor(changePagerCommand);
		thenExpectHttpRedirectWith(displayBooksCommand);
	}

	private void thenExpectServiceSortInvokedFor(ChangePagerCommand changePagerCommand) {
		verify(changeBooksPagerService).sort(eq(changePagerCommand), any(BindingResult.class));
		verifyNoMoreInteractions(changeBooksPagerService);
	}

	@Test
	public void shouldSetPageSize() throws Exception {
		given(changeBooksPagerService.setPageSize(eq(changePagerCommand), any(BindingResult.class)))
				.willReturn(displayBooksCommand);

		whenServicePerformedWithUrlAndCommand("/books/setPageSize", changePagerCommand);

		thenExpectServiceSetPageSizeInvokedFor(changePagerCommand);
		thenExpectHttpRedirectWith(displayBooksCommand);
	}

	private void thenExpectServiceSetPageSizeInvokedFor(ChangePagerCommand changePagerCommand) {
		verify(changeBooksPagerService).setPageSize(eq(changePagerCommand), any(BindingResult.class));
		verifyNoMoreInteractions(changeBooksPagerService);
	}

	@Test
	public void shouldGoToPage() throws Exception {
		given(changeBooksPagerService.goToPage(eq(changePagerCommand), any(BindingResult.class)))
				.willReturn(displayBooksCommand);

		whenServicePerformedWithUrlAndCommand("/books/goToPage", changePagerCommand);

		thenExpectServiceGoToPageInvokedFor(changePagerCommand);
		thenExpectHttpRedirectWith(displayBooksCommand);
	}

	private void thenExpectServiceGoToPageInvokedFor(ChangePagerCommand changePagerCommand) {
		verify(changeBooksPagerService).goToPage(eq(changePagerCommand), any(BindingResult.class));
		verifyNoMoreInteractions(changeBooksPagerService);
	}

	private void whenServicePerformedWithUrlAndCommand(String url, ChangePagerCommand changePagerCommand)
			throws Exception
	{
		mvcMockPerformResult = mvcMock.perform(post(url)
				.flashAttr("changePagerCommand", changePagerCommand));
	}

	private void thenExpectHttpRedirectWith(DisplayBooksCommand displayBooksCommand) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/display"))
				.andExpect(flash().attribute("displayBooksCommand",
						sameInstance(displayBooksCommand)));
	}

}
