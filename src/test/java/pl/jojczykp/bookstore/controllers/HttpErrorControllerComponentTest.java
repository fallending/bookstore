package pl.jojczykp.bookstore.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class HttpErrorControllerComponentTest {

	private static final String ORIGINAL_URL = "/original/url";
	private static final String ORIGINAL_URL_REQUEST_ATTRIBUTE = "javax.servlet.forward.request_uri";

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private WebApplicationContext wac;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
	}

	@Test
	public void shouldRenderViewForErrorOnGet() throws Exception {
		final int httpErrorId = 404;

		whenErrorControllerGetPerformedWith(ORIGINAL_URL, httpErrorId);

		thenExpectStatusOK();
		thenExpectSelectedView("httpError");
		thenExpectHttpErrorCommandWith(ORIGINAL_URL, httpErrorId);
	}

	@Test
	public void shouldRenderViewForErrorOnPost() throws Exception {
		final int httpErrorId = 502;

		whenErrorControllerPostPerformedWith(ORIGINAL_URL, httpErrorId);

		thenExpectStatusOK();
		thenExpectSelectedView("httpError");
		thenExpectHttpErrorCommandWith(ORIGINAL_URL, httpErrorId);
	}

	private void whenErrorControllerGetPerformedWith(String originalUrl, int httpErrorId) throws Exception {
		mvcMockPerformResult = mvcMock.perform(get("/httpError/" + httpErrorId)
				.requestAttr(ORIGINAL_URL_REQUEST_ATTRIBUTE, originalUrl));
	}

	private void whenErrorControllerPostPerformedWith(String originalUrl, int httpErrorId) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post("/httpError/" + httpErrorId)
				.requestAttr(ORIGINAL_URL_REQUEST_ATTRIBUTE, originalUrl));
	}

	private void thenExpectStatusOK() throws Exception {
		mvcMockPerformResult
				.andExpect(status().isOk());
	}

	private void thenExpectSelectedView(String viewName) throws Exception {
		mvcMockPerformResult
				.andExpect(view().name(viewName));
	}

	private void thenExpectHttpErrorCommandWith(String originalUrl, int httpErrorId) throws Exception {
		mvcMockPerformResult
				.andExpect(model().attribute("httpErrorCommand",
					hasBeanProperty("id", is(httpErrorId))))
				.andExpect(model().attribute("httpErrorCommand",
					hasBeanProperty("originalUrl", is(equalTo(originalUrl)))))
				.andExpect(model().attribute("httpErrorCommand",
					hasBeanProperty("description", is(equalTo(HttpStatus.valueOf(httpErrorId).getReasonPhrase())))));
	}

}
