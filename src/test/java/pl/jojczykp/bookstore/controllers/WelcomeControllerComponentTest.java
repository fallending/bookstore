package pl.jojczykp.bookstore.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class WelcomeControllerComponentTest {

	private MockMvc mvcMock;
	@Autowired private WebApplicationContext wac;

	@Before
	public void setup() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
	}

	@Test
	public void shouldRedirectToWelcomeViewWithDefaultAttributesSet() throws Exception {
		mvcMock.perform(get("/"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/books/display"));
	}
}
