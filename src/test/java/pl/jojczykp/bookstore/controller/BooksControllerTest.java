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

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml"
})
public class BooksControllerTest {

	private static final int DEFAULT_OFFSET = 0;
	private static final int DEFAULT_SIZE = 10;

	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private WebApplicationContext wac;
	private MockMvc mockMvc;
	private ResultActions mockMvcPerformResult;
	private List<Book> mockedBooksList;

	private int repoOffset;
	private int repoSize;
	private int repoTotalCount;

	@Before
	public void setup() {
		mockMvc = webAppContextSetup(wac).build();
	}

	@Test
	public void shouldDisplayBooksListViewWithDefaultsOffsetWhenNoParameterGiven() throws Exception {
		repoOffset = 5;
		repoSize = 12;
		repoTotalCount = 23;
		givenRepositoryMockConfigured();

		mockMvc.perform(get("/books/list"))
				.andExpect(model().attribute("offset", equalTo(DEFAULT_OFFSET)));
	}

	@Test
	public void shouldDisplayBooksListViewWithDefaultsSizeWhenNoParameterGiven() throws Exception {
		repoOffset = 5;
		repoSize = 10;
		repoTotalCount = 23;
		givenRepositoryMockConfigured();

		mockMvc.perform(get("/books/list"))
				.andExpect(model().attribute("size", equalTo(DEFAULT_SIZE)));
	}

	@Test
	public void shouldDisplayBooksListViewForRegularParameters() {
		repoOffset = 5;
		repoSize = 11;
		repoTotalCount = 23;
		givenRepositoryMockConfigured();

		whenControllerPerformedWithParams(repoOffset, repoSize);

		thenExpectBookRepositoryAccessFor(repoOffset, repoSize);
		thenExpectCorrectViewSelectedAndModelSet();
	}

	@Test
	public void shouldDisplayBooksListViewWhenParamSizeAboveRange() {
		repoTotalCount = 20;
		repoOffset = 15;
		repoSize = repoTotalCount - repoOffset;
		givenRepositoryMockConfigured();
		final int paramSize = repoTotalCount + 1;

		whenControllerPerformedWithParams(repoOffset, paramSize);

		thenExpectBookRepositoryAccessFor(repoOffset, paramSize);
		thenExpectCorrectViewSelectedAndModelSet();
	}

	@Test
	public void shouldDisplayBooksListViewWhenParamOffsetBelowRange() {
		repoTotalCount = 20;
		repoOffset = 0;
		repoSize = 10;
		givenRepositoryMockConfigured();
		final int paramOffset = -2;

		whenControllerPerformedWithParams(paramOffset, repoSize);

		thenExpectBookRepositoryAccessFor(paramOffset, repoSize);
		thenExpectCorrectViewSelectedAndModelSet();
	}

	@Test
	public void shouldScrollToLastPageWhenOffsetAboveRangeForGivenSize() {
		repoTotalCount = 20;
		repoOffset = 12;
		repoSize = repoTotalCount - repoOffset;
		givenRepositoryMockConfigured();
		final int paramOffset = repoOffset + 2;

		whenControllerPerformedWithParams(paramOffset, repoSize);

		thenExpectBookRepositoryAccessFor(paramOffset, repoSize);
		thenExpectCorrectViewSelectedAndModelSet();
	}

	@Test
	public void shouldScrollToOnlyPageWhenSizeAboveAndOffsetBelowRange() {
		repoTotalCount = 20;
		repoOffset = 0;
		repoSize = repoTotalCount;
		givenRepositoryMockConfigured();
		final int paramOffset = -2;
		final int paramSize = repoTotalCount + 3;

		whenControllerPerformedWithParams(paramOffset, paramSize);

		thenExpectCorrectViewSelectedAndModelSet();
		thenExpectBookRepositoryAccessFor(paramOffset, paramSize);
	}

	@Test
	public void shouldReturnEmptyResultWhenSizeIsNegative() {
		repoTotalCount = 20;
		repoOffset = 1;
		repoSize = 0;
		givenRepositoryMockConfigured();
		final int paramSize = -2;

		whenControllerPerformedWithParams(repoOffset, paramSize);

		thenExpectBookRepositoryAccessFor(repoOffset, paramSize);
		thenExpectCorrectViewSelectedAndModelSet();
	}

	private void givenRepositoryMockConfigured() {
		mockedBooksList = createMockedBooksListOfSize(repoSize);
		reset(bookRepositoryMock);
		given(bookRepositoryMock.totalCount()).willReturn(repoTotalCount);
		given(bookRepositoryMock.read(anyInt(), anyInt())).willReturn(mockedBooksList);
	}

	private List<Book> createMockedBooksListOfSize(int size) {
		List<Book> books = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			books.add(new Book());
		}

		return books;
	}

	private void whenControllerPerformedWithParams(int paramOffset, int paramSize) {
		try {
			mockMvcPerformResult = mockMvc.perform(get("/books/list")
					.param("offset", valueOf(paramOffset))
					.param("size", valueOf(paramSize)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void thenExpectBookRepositoryAccessFor(int offset, int size) {
		ArgumentCaptor<Integer> offsetCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);

		verify(bookRepositoryMock).totalCount();
		verify(bookRepositoryMock).read(offsetCaptor.capture(), sizeCaptor.capture());
		assertThat(offsetCaptor.getValue(), equalTo(offset));
		assertThat(sizeCaptor.getValue(), equalTo(size));
	}

	private void thenExpectCorrectViewSelectedAndModelSet() {
		try {
			mockMvcPerformResult
					.andExpect(status().isOk())
					.andExpect(view().name("booksList"))
					.andExpect(model().attribute("offset", equalTo(repoOffset)))
					.andExpect(model().attribute("size", equalTo(repoSize)))
					.andExpect(model().attribute("totalCount", equalTo(repoTotalCount)))
					.andExpect(model().attribute("books", equalTo(mockedBooksList)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
