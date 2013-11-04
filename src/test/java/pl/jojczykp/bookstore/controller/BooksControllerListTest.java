package pl.jojczykp.bookstore.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParams;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
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
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/scroll-params-limiter-mock-context.xml",
		"classpath:spring/config-test-context.xml"
})
public class BooksControllerListTest {

	private static final int REPO_TOTAL_COUNT = 23;
	private static final int REPO_FIRST_RETURNED_RECORD_OFFSET = 14;
	private static final int REPO_RESULT_SIZE = 7;
	private static final List<Book> REPO_RESULT_DATA = new ArrayList<>();

	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private ScrollParamsLimiter scrollParamsLimiterMock;
	@Autowired private WebApplicationContext wac;
	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;

	@Value("${view.books.defaultOffset}") int defaultOffset;
	@Value("${view.books.defaultSize}") int defaultSize;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac).build();
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
	}

	@Test
	public void shouldUseDefaultValueFromConfigWhenNoOffsetParameterGiven() throws Exception {
		mvcMock.perform(get("/books/list"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("scrollParams", hasProperty("offset", equalTo(defaultOffset))));
	}

	@Test
	public void shouldUseDefaultValueFromConfigWhenNoSizeParameterGiven() throws Exception {
		mvcMock.perform(get("/books/list"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("scrollParams", hasProperty("size", equalTo(defaultSize))));
	}

	@Test
	public void shouldUseLimitationOfOffsetAndSizeParameters() throws Exception {
		whenControllerListPerformedWithParams();
		thenExpectParametersLimitationUsage();
	}

	@Test
	public void shouldDirectToProperViewWithModelCreatedForRepositoryData() throws Exception {
		whenControllerListPerformedWithParams();

		thenExpectBookRepositoryRead();
		thenExpectCorrectViewSelectedAndModelSet();
	}

	private void whenControllerListPerformedWithParams() throws Exception {
		mvcMockPerformResult = mvcMock.perform(get("/books/list")
				.param("offset", valueOf(REPO_FIRST_RETURNED_RECORD_OFFSET))
				.param("size", valueOf(REPO_RESULT_SIZE)));
	}

	private void thenExpectParametersLimitationUsage() {
		ArgumentCaptor<ScrollParams> scrollParamsCaptor = ArgumentCaptor.forClass(ScrollParams.class);
		ArgumentCaptor<Integer> totalCountCaptor = ArgumentCaptor.forClass(Integer.class);

		verify(scrollParamsLimiterMock, times(1)).limit(scrollParamsCaptor.capture(), totalCountCaptor.capture());
		assertThat(scrollParamsCaptor.getValue().getOffset(), equalTo(REPO_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(scrollParamsCaptor.getValue().getSize(), equalTo(REPO_RESULT_SIZE));
		assertThat(totalCountCaptor.getValue(), equalTo(REPO_TOTAL_COUNT));
	}

	private void thenExpectBookRepositoryRead() {
		ArgumentCaptor<Integer> offsetCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);

		verify(bookRepositoryMock).totalCount();
		verify(bookRepositoryMock).read(offsetCaptor.capture(), sizeCaptor.capture());
		assertThat(offsetCaptor.getValue(), equalTo(REPO_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(sizeCaptor.getValue(), equalTo(REPO_RESULT_SIZE));
	}

	private void thenExpectCorrectViewSelectedAndModelSet() {
		try {
			mvcMockPerformResult
					.andExpect(status().isOk())
					.andExpect(view().name("books"))
					.andExpect(model().attribute("books", sameInstance(REPO_RESULT_DATA)))
					.andExpect(model().attribute("newBook", instanceOf(Book.class)))
					.andExpect(model().attribute("scrollParams", hasProperty("size", equalTo(REPO_RESULT_SIZE))))
					.andExpect(model().attribute("scrollParams", hasProperty("totalCount", equalTo(REPO_TOTAL_COUNT))))
					.andExpect(model().attribute("scrollParams", hasProperty("offset",
							equalTo(REPO_FIRST_RETURNED_RECORD_OFFSET))));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
