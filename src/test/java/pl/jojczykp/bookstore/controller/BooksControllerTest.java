package pl.jojczykp.bookstore.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;
import pl.jojczykp.bookstore.utils.ScrollParamsLimits;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/scroll-params-limiter-mock-context.xml"
})
public class BooksControllerTest {

	private static final int DEFAULT_PARAM_OFFSET = 0;
	private static final int DEFAULT_PARAM_SIZE = 10;

	private static final int REPO_TOTAL_COUNT = 23;
	private static final int REPO_FIRST_RETURNED_RECORD_OFFSET = 14;
	private static final int REPO_RESULT_SIZE = 7;
	private static final List<Book> REPO_RESULT_DATA = new ArrayList<>();

	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private ScrollParamsLimiter scrollParamsLimiterMock;
	@Autowired private WebApplicationContext wac;
	private MockMvc mockMvc;
	private ResultActions mockMvcPerformResult;

	@Before
	public void setUp() {
		mockMvc = webAppContextSetup(wac).build();
		givenRepositoryMockConfigured();
		givenRepeatingScrollParamsLimiter();

	}

	private void givenRepositoryMockConfigured() {
		reset(bookRepositoryMock);
		given(bookRepositoryMock.totalCount()).willReturn(REPO_TOTAL_COUNT);
		given(bookRepositoryMock.read(anyInt(), anyInt())).willReturn(REPO_RESULT_DATA);
	}

	private void givenRepeatingScrollParamsLimiter() {
		reset(scrollParamsLimiterMock);
		when(scrollParamsLimiterMock.computeLimitsFor(anyInt(), anyInt(), anyInt()))
				.thenAnswer(new Answer<ScrollParamsLimits>() {
					@Override
					public ScrollParamsLimits answer(InvocationOnMock invocation) {
						Object[] args = invocation.getArguments();
						int offset = (int) args[0];
						int size = (int) args[1];
						return new ScrollParamsLimits(offset, size);
					}
				});
	}

	@Test
	public void shouldUseDefaultValueWhenNoOffsetParameterGiven() throws Exception {
		mockMvc.perform(get("/books/list"))
				.andExpect(model().attribute("offset", equalTo(DEFAULT_PARAM_OFFSET)));
	}

	@Test
	public void shouldUseDefaultValueWhenNoSizeParameterGiven() throws Exception {
		mockMvc.perform(get("/books/list"))
				.andExpect(model().attribute("size", equalTo(DEFAULT_PARAM_SIZE)));
	}

	@Test
	public void shouldUseLimitationOfOffsetAndSizeParameters() throws Exception {
		whenControllerPerformedWithParams();
		thenExpectParametersLimitationUsage();
	}

	@Test
	public void shouldDirectToProperViewWithModelCreatedForRepositoryData() throws Exception {
		whenControllerPerformedWithParams();

		thenExpectBookRepositoryAccess();
		thenExpectCorrectViewSelectedAndModelSet();
	}

	private void whenControllerPerformedWithParams() throws Exception {
		mockMvcPerformResult = mockMvc.perform(get("/books/list")
				.param("offset", valueOf(REPO_FIRST_RETURNED_RECORD_OFFSET))
				.param("size", valueOf(REPO_RESULT_SIZE)));
	}

	private void thenExpectParametersLimitationUsage() {
		ArgumentCaptor<Integer> offsetCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> totalCountCaptor = ArgumentCaptor.forClass(Integer.class);

		verify(scrollParamsLimiterMock, times(1))
				.computeLimitsFor(offsetCaptor.capture(), sizeCaptor.capture(), totalCountCaptor.capture());
		assertThat(totalCountCaptor.getValue(), equalTo(REPO_TOTAL_COUNT));
		assertThat(offsetCaptor.getValue(), equalTo(REPO_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(sizeCaptor.getValue(), equalTo(REPO_RESULT_SIZE));
	}

	private void thenExpectBookRepositoryAccess() {
		ArgumentCaptor<Integer> offsetCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);

		verify(bookRepositoryMock).totalCount();
		verify(bookRepositoryMock).read(offsetCaptor.capture(), sizeCaptor.capture());
		assertThat(offsetCaptor.getValue(), equalTo(REPO_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(sizeCaptor.getValue(), equalTo(REPO_RESULT_SIZE));
	}

	private void thenExpectCorrectViewSelectedAndModelSet() {
		try {
			mockMvcPerformResult
					.andExpect(status().isOk())
					.andExpect(view().name("booksList"))
					.andExpect(model().attribute("offset", equalTo(REPO_FIRST_RETURNED_RECORD_OFFSET)))
					.andExpect(model().attribute("size", equalTo(REPO_RESULT_SIZE)))
					.andExpect(model().attribute("totalCount", equalTo(REPO_TOTAL_COUNT)))
					.andExpect(model().attribute("books", sameInstance(REPO_RESULT_DATA)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
