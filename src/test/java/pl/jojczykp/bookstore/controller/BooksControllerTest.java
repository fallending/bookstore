package pl.jojczykp.bookstore.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static java.lang.String.valueOf;
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

	private static final int TEST_OFFSET = 2;
	private static final int TEST_SIZE = 8;

	private static final int DEFAULT_OFFSET = 0;
	private static final int DEFAULT_SIZE = 10;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		mockMvc = webAppContextSetup(wac).build();
	}

	@Test
	public void shouldDisplayBooksListViewWithDefaultOffsetWhenNoOffsetGiven() throws Exception {
		mockMvc.perform(get("/books/list")
				.param("size", valueOf(TEST_SIZE)))
				.andExpect(status().isOk())
				.andExpect(view().name("booksList"))
				.andExpect(model().attribute("offset", DEFAULT_OFFSET))
				.andExpect(model().attribute("size", TEST_SIZE));
	}

	@Test
	public void shouldDisplayBooksListViewWithDefaultSizeWhenNoSizeGiven() throws Exception {
		mockMvc.perform(get("/books/list")
				.param("offset", valueOf(TEST_OFFSET)))
				.andExpect(status().isOk())
				.andExpect(view().name("booksList"))
				.andExpect(model().attribute("offset", TEST_OFFSET))
				.andExpect(model().attribute("size", DEFAULT_SIZE));
	}

	@Test
	public void shouldDisplayBooksListViewForParametersWhenAllGiven() throws Exception {
		mockMvc.perform(get("/books/list")
				.param("offset", valueOf(TEST_OFFSET))
				.param("size", valueOf(TEST_SIZE)))
				.andExpect(status().isOk())
				.andExpect(view().name("booksList"))
				.andExpect(model().attribute("offset", TEST_OFFSET))
				.andExpect(model().attribute("size", TEST_SIZE));
	}

}
