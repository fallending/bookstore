package pl.jojczykp.bookstore.controllers.errors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.controllers.errors.ExceptionThrowingTestController.THROW_EXCEPTION_CONTROLLER_URL;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/controllers-test-context.xml")
public class ControllerExceptionHandlerComponentTest {

	private MockMvc mvcMock;
	@Autowired private WebApplicationContext wac;

	@Before
	public void setup() {
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
	}

	@Test
	public void shouldHandleAccessDeniedExceptionFromController() throws Exception {
		shouldHandleExceptionFromController(AccessDeniedException.class, HttpStatus.FORBIDDEN);
	}

	@Test
	public void shouldHandleResourceNotFoundExceptionFromController() throws Exception {
		shouldHandleExceptionFromController(ResourceNotFoundException.class, HttpStatus.NOT_FOUND);
	}

	@Test
	public void shouldHandleAnyExceptionFromController() throws Exception {
		shouldHandleExceptionFromController(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void shouldHandleExceptionFromController(Class<? extends Exception> exceptionClass, HttpStatus status)
																									throws Exception
	{
		mvcMock.perform(post(THROW_EXCEPTION_CONTROLLER_URL + "/" + exceptionClass.getName() + "/"))
				.andExpect(status().is(status.value()))
				.andExpect(view().name("exception"))
				.andExpect(model().attribute("exceptionCommand",
						hasBeanProperty("message", not(isEmptyOrNullString()))))
				.andExpect(model().attribute("exceptionCommand",
						hasBeanProperty("stackTraceAsString", not(isEmptyOrNullString()))));
	}

}
