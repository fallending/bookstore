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
import pl.jojczykp.bookstore.command.BooksCommand;
import pl.jojczykp.bookstore.domain.Book;
import pl.jojczykp.bookstore.repository.BookRepository;
import pl.jojczykp.bookstore.utils.ScrollParams;
import pl.jojczykp.bookstore.utils.ScrollParamsLimiter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
		"classpath:spring/repository-mock-context.xml",
		"classpath:spring/scroll-params-limiter-mock-context.xml",
		"classpath:spring/config-test-context.xml"
})
public class BooksControllerListTest {

	private static final String BOOKS_COMMAND = "booksCommand";
	private static final String URL_ACTION_LIST = "/books/list";

	private static final int REPO_TOTAL_COUNT = 23;
	private static final int REPO_FIRST_RETURNED_RECORD_OFFSET = 14;
	private static final int REPO_RESULT_SIZE = 7;
	private static final List<Book> REPO_RESULT_DATA = new ArrayList<>();

	private static final int LIMITED_FIRST_RETURNED_RECORD_OFFSET = 16;
	private static final int LIMITED_RESULT_SIZE = 6;
	private static final ScrollParams LIMITED_SCROLL_PARAMS = limitedScrollParams();

	private static ScrollParams limitedScrollParams() {
		ScrollParams scrollParams = new ScrollParams();
		scrollParams.setOffset(LIMITED_FIRST_RETURNED_RECORD_OFFSET);
		scrollParams.setSize(LIMITED_RESULT_SIZE);

		return scrollParams;
	}

	@Autowired private BookRepository bookRepositoryMock;
	@Autowired private ScrollParamsLimiter scrollParamsLimiterMock;
	@Autowired private WebApplicationContext wac;
	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;

	@Value("${view.books.defaultOffset}") private int defaultOffset;
	@Value("${view.books.defaultSize}") private int defaultSize;

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
		given(scrollParamsLimiterMock.limit(any(ScrollParams.class), anyInt())).willReturn(LIMITED_SCROLL_PARAMS);
	}

	@Test
	public void shouldUseDefaultValueFromConfigWhenNoOffsetParameterGiven() throws Exception {
		mvcMock.perform(get(URL_ACTION_LIST))
				.andExpect(status().isOk())
				.andExpect(model().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.current.offset", equalTo(defaultOffset))));
	}

	@Test
	public void shouldUseDefaultValueFromConfigWhenNoSizeParameterGiven() throws Exception {
		mvcMock.perform(get(URL_ACTION_LIST))
				.andExpect(status().isOk())
				.andExpect(model().attribute(BOOKS_COMMAND,
						hasBeanProperty("scroll.current.size", equalTo(defaultSize))));
	}

	@Test
	public void shouldUseLimitationOfOffsetAndSizeParameters() throws Exception {
		whenControllerListPerformedWithCommand();
		thenExpectParametersLimitationUsage();
	}

	@Test
	public void shouldDirectToProperViewWithModelCreatedForRepositoryData() throws Exception {
		whenControllerListPerformedWithCommand();

		thenExpectBookRepositoryRead();
		thenExpectCorrectViewSelectedAndModelSet();
	}

	private void whenControllerListPerformedWithCommand() throws Exception {
		BooksCommand command = new BooksCommand();
		command.getScroll().getCurrent().setOffset(REPO_FIRST_RETURNED_RECORD_OFFSET);
		command.getScroll().getCurrent().setSize(REPO_RESULT_SIZE);

		mvcMockPerformResult = mvcMock.perform(get(URL_ACTION_LIST)
				.flashAttr(BOOKS_COMMAND, command));
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
		assertThat(offsetCaptor.getValue(), equalTo(LIMITED_FIRST_RETURNED_RECORD_OFFSET));
		assertThat(sizeCaptor.getValue(), equalTo(LIMITED_RESULT_SIZE));
	}

	private void thenExpectCorrectViewSelectedAndModelSet() throws Exception {
		mvcMockPerformResult
			.andExpect(status().isOk())
			.andExpect(view().name("books"))
			.andExpect(model().attribute(BOOKS_COMMAND, hasProperty("books", sameInstance(REPO_RESULT_DATA))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasProperty("newBook", instanceOf(Book.class))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.totalCount", equalTo(
					REPO_TOTAL_COUNT))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.current.size", equalTo(
					REPO_RESULT_SIZE))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.current.offset", equalTo(
					REPO_FIRST_RETURNED_RECORD_OFFSET))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.limited.size", equalTo(
					LIMITED_RESULT_SIZE))))
			.andExpect(model().attribute(BOOKS_COMMAND, hasBeanProperty("scroll.limited.offset", equalTo(
					LIMITED_FIRST_RETURNED_RECORD_OFFSET))));
	}

}
