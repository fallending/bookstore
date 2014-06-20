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

import com.google.protobuf.ByteString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.commands.books.DownloadBookCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;
import pl.jojczykp.bookstore.transfers.books.BookTO;

import static com.google.protobuf.ByteString.copyFrom;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class DownloadBookControllerComponentTest {

	private static final int ID = 7;
	private static final int OTHER_ID = 13;
	private static final String TITLE = "Some Book Title";
	private static final String CONTENT_TYPE = "content/type";
	private static final ByteString CONTENT = copyFrom(new byte[] {9, 8, 7, 6, 5, 4, 3, 2, 1});
	private static final ByteString EMPTY_CONTENT = ByteString.EMPTY;

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;

	@Autowired private BooksRepository booksRepository;

	@Mock private BookTO bookTO;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();

		MockitoAnnotations.initMocks(this);
		reset(booksRepository);
	}

	@Test
	public void shouldDownloadBook() throws Exception {
		givenBookReadFromRepositoryWith(ID, TITLE, CONTENT_TYPE, CONTENT);

		mvcMockPerformResult = mvcMock.perform(get("/books/download")
				.flashAttr("downloadBookCommand", downloadBookCommandWith(ID)));

		thenExpectStatusIsOk();
		thenExpectHeadersFor(TITLE, CONTENT_TYPE, CONTENT);
		thenExpectContent(CONTENT);
	}

	@Test
	public void shouldFailDownloadingNotExistingBook() throws Exception {
		givenBookReadFromRepositoryWith(ID, TITLE, CONTENT_TYPE, CONTENT);

		mvcMockPerformResult = mvcMock.perform(get("/books/download")
				.flashAttr("downloadBookCommand", downloadBookCommandWith(OTHER_ID)));

		thenExpectStatusIsNotFound();
		thenExpectContent(EMPTY_CONTENT);
	}

	private void givenBookReadFromRepositoryWith(int id, String title, String contentType, ByteString content) {
		given(booksRepository.find(id)).willReturn(bookTO);
		given(bookTO.getTitle()).willReturn(title);
		given(bookTO.getContentType()).willReturn(contentType);
		given(bookTO.getContent()).willReturn(content);
	}

	private DownloadBookCommand downloadBookCommandWith(int id) {
		DownloadBookCommand command = new DownloadBookCommand();
		command.setId(id);

		return command;
	}

	private void thenExpectStatusIsOk() throws Exception {
		mvcMockPerformResult.andExpect(status().isOk());
	}

	private void thenExpectStatusIsNotFound() throws Exception {
		mvcMockPerformResult.andExpect(status().isNotFound());
	}

	private void thenExpectHeadersFor(String title, String contentType, ByteString content) throws Exception {
		mvcMockPerformResult
			.andExpect(header().string("Content-Type", is(equalTo(contentType))))
			.andExpect(header().string("Content-Length", is(equalTo(Integer.toString(content.size())))))
			.andExpect(header().string("Content-Disposition", is(equalTo("attachment; filename=\"" + title + "\""))));
	}

	private void thenExpectContent(ByteString content) throws Exception {
		mvcMockPerformResult.andExpect(content().bytes(content.toByteArray()));
	}

}
