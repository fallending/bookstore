package pl.jojczykp.bookstore.testutils.controllers.security;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(JUnitParamsRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"classpath:spring/controllers-test-context.xml" ,
		"classpath:spring/controllers-mock-context.xml" ,
		"classpath:spring/authentication-provider-mock-context.xml",
		"classpath:spring/datasource-mock-context.xml",
		"classpath:spring/applicationContext/security-context.xml"})
public abstract class SecurityControllersTestAbstract {

	public static final String ROLE_USER = "USER";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_UNAUTHORIZED = "UNAUTHORIZED";

	private MockMvc mvcMock;

	@Autowired private WebApplicationContext wac;
	@Autowired private Filter springSecurityFilterChain;
	@Autowired private DataSource dataSource;

	@Before
	public void setUpContext() throws Exception {
		givenSpringBeansAutowired();
		givenDataSourceConfigured();
		givenSpringWebContextConfigured();
	}

	private void givenSpringBeansAutowired() throws Exception {
		new TestContextManager(getClass()).prepareTestInstance(this);
	}

	public void givenDataSourceConfigured() throws Exception {
		Connection connection = mock(Connection.class);
		given(dataSource.getConnection()).willReturn(connection);
		given(connection.prepareStatement(anyString())).willReturn(mock(PreparedStatement.class));
	}

	private void givenSpringWebContextConfigured() {
		mvcMock = webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.alwaysDo(print())
				.build();
	}

	public void verifyAccess(MockHttpServletRequestBuilder methodAndUrl, String roles, ResultMatcher resultMatcher) {
		try {
			mvcMock.perform(methodAndUrl
					.with(user("someUser").roles(roles)))
					.andExpect(resultMatcher);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public MockMvc getMockedContext() {
		return mvcMock;
	}

}
