package pl.jojczykp.bookstore.controllers.books;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.commands.books.DownloadBookCommand;
import pl.jojczykp.bookstore.controllers.errors.ResourceNotFoundException;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.entities.BookFile;
import pl.jojczykp.bookstore.services.books.DownloadBookService;

import static java.lang.String.format;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;
import static pl.jojczykp.bookstore.utils.BlobUtils.aSerialBlobWith;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class DownloadBookControllerComponentTest {

	private static final String ID = "7";
	private static final String TITLE = "Some Book Title";
	private static final String FILE_TYPE = "fileType";
	private static final String CONTENT_TYPE = "content/type";
	private static final byte[] CONTENT = {1, 2, 3, 4, 5};
	private static final String SERVICE_EXCEPTION_MESSAGE = "Service Exception Message";
	private static final Exception SERVICE_EXCEPTION = new ResourceNotFoundException(SERVICE_EXCEPTION_MESSAGE);

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;

	@Autowired private DownloadBookService downloadBookService;

	@Mock private Book book;
	@Mock private BookFile bookFile;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		initMocks(this);
		reset(downloadBookService);
	}

	@Test
	public void shouldDownload() throws Exception {
		DownloadBookCommand command = downloadBookCommandWith(ID);
		givenBookReturnedByService(command, TITLE, FILE_TYPE, CONTENT_TYPE, CONTENT);

		whenControllerDownloadPerformedWithCommand(command);

		thenExpectStatusIsOk();
		thenExpectHeadersFor(TITLE, FILE_TYPE, CONTENT.length, CONTENT_TYPE);
		thenExpectContent(CONTENT);
	}

	@Test
	public void shouldFailDownloadingOnServiceException() throws Exception {
		DownloadBookCommand command = downloadBookCommandWith(ID);
		givenExceptionReturnedByService(command);

		whenControllerDownloadPerformedWithCommand(command);

		thenExpectStatusIsNotFound();
		thenExpectViewName("exception");
		thenExpectCorrectExceptionCommand();
	}

	private DownloadBookCommand downloadBookCommandWith(String id) {
		DownloadBookCommand command = new DownloadBookCommand();
		command.setId(id);

		return command;
	}

	private void givenBookReturnedByService(DownloadBookCommand command, String title, String fileType,
											String contentType, byte[] content) {
		given(downloadBookService.download(command)).willReturn(book);
		given(book.getTitle()).willReturn(title);
		given(book.getBookFile()).willReturn(bookFile);
		given(bookFile.getFileType()).willReturn(fileType);
		given(bookFile.getContentType()).willReturn(contentType);
		given(bookFile.getContentLength()).willReturn(content.length);
		given(bookFile.getContent()).willReturn(aSerialBlobWith(content));
	}

	private void givenExceptionReturnedByService(DownloadBookCommand command) {
		given(downloadBookService.download(command)).willThrow(SERVICE_EXCEPTION);
	}

	private void whenControllerDownloadPerformedWithCommand(DownloadBookCommand command) throws Exception {
		mvcMockPerformResult = mvcMock.perform(get("/books/download")
				.flashAttr("downloadBookCommand", command));
	}

	private void thenExpectStatusIsOk() throws Exception {
		mvcMockPerformResult.andExpect(status().isOk());
	}

	private void thenExpectStatusIsNotFound() throws Exception {
		mvcMockPerformResult.andExpect(status().isNotFound());
	}

	private void thenExpectHeadersFor(String title, String fileType,
									int contentLength, String contentType) throws Exception {
		mvcMockPerformResult
			.andExpect(header().string("Content-Type", is(equalTo(contentType))))
			.andExpect(header().string("Content-Length", is(equalTo(Integer.toString(contentLength)))))
			.andExpect(header().string("Content-Disposition",
					is(equalTo(format("attachment; filename=\"%s.%s\"", title, fileType)))));
	}

	private void thenExpectContent(byte[] content) throws Exception {
		mvcMockPerformResult.andExpect(content().bytes(content));
	}

	private void thenExpectViewName(String viewName) throws Exception {
		mvcMockPerformResult
				.andExpect(view().name(viewName));
	}

	private void thenExpectCorrectExceptionCommand() throws Exception {
		mvcMockPerformResult
				.andExpect(modelExceptionCommand(allOf(
					hasBeanProperty("stackTraceAsString", not(isEmptyOrNullString())),
						hasBeanProperty("message", is(equalTo(SERVICE_EXCEPTION_MESSAGE))))));
	}

	private ResultMatcher modelExceptionCommand(Matcher<?> matcher) {
		return model().attribute("exceptionCommand", matcher);
	}

}
