package pl.jojczykp.bookstore.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParams;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/scroll-params-limiter-mock-context.xml"
})
public class BooksControllerAddTest {

	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private WebApplicationContext wac;
	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
	}

	@Test
	public void shouldAddBook() throws Exception {
		final Integer offset = 3;
		final Integer size = 11;
		final String title = "aTitle";

		whenControllerAddPerformedWithParam(offset, size, title);

		thenExpectCreatedBookWith(title);
	}

	@Test
	public void shouldRedirectAfterAdding() throws Exception {
		final Integer offset = 4;
		final Integer size = 9;
		final String title = "anotherTitle";

		whenControllerAddPerformedWithParam(offset, size, title);

		thenExpectHttpRedirect(offset, size);
	}

	private void whenControllerAddPerformedWithParam(Integer offset, Integer size, String title) throws Exception {
		final int totalCount = offset + size + 1;

		mvcMockPerformResult = mvcMock.perform(post("/books/add")
				.flashAttr("scrollParams", new ScrollParams(offset, size, totalCount))
				.flashAttr("newBook", new Book(title)));
	}

	private void thenExpectCreatedBookWith(String title) {
		ArgumentCaptor<Book> newBookCaptor = ArgumentCaptor.forClass(Book.class);

		verify(bookRepositoryMock).create(newBookCaptor.capture());
		assertThat(newBookCaptor.getValue().getTitle(), equalTo(title));
	}

	private void thenExpectHttpRedirect(Integer offset, Integer size) throws Exception {
		mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/list"))
				.andExpect(flash().attribute("scrollParams", hasProperty("offset", equalTo(offset))))
				.andExpect(flash().attribute("scrollParams", hasProperty("size", equalTo(size))));
	}

}
