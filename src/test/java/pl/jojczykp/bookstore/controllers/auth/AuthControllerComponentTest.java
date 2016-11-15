package pl.jojczykp.bookstore.controllers.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.lang.Long.MAX_VALUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.security.crypto.bcrypt.BCrypt.gensalt;
import static org.springframework.security.crypto.bcrypt.BCrypt.hashpw;
import static org.springframework.security.web.WebAttributes.AUTHENTICATION_EXCEPTION;
import static org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices
															.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.controllers.auth.SomeSecuredTestController.SOME_SECURED_TEST_CONTROLLER_URL;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"classpath:spring/controllers-test-context.xml",
		"classpath:spring/authentication-provider-mock-context.xml",
		"classpath:spring/datasource-mock-context.xml",
		"classpath:spring/applicationContext/security-context.xml",
		"classpath:spring/dispatcherServletContext/resources-context.xml",
		"classpath:spring/dispatcherServletContext/views-context.xml"})
public class AuthControllerComponentTest {

	private static final String URL_LOGIN_PAGE = "/auth/loginPage";
	private static final String URL_LOGIN = "/auth/login";
	private static final String URL_LOGIN_SUCCESS = "/";
	private static final String URL_LOGOUT = "/auth/logout";
	private static final String URL_LOGOUT_PAGE = "/auth/logoutPage";

	private static final String VIEW_LOGIN_PAGE = "loginPage";
	private static final String VIEW_EXCEPTION_PAGE = "exception";

	private static final String USER_NAME = "user_name";
	private static final String PASSWORD = "password";
	private static final String TOKEN_SERIES = "tokenSeries";
	private static final String TOKEN_VALUE = "tokenValue";
	private static final boolean REMEMBER_ME = true;
	private static final boolean DONT_REMEMBER_ME = false;

	private static final String[] SOME_ROLES = {"SOME_ROLE_1", "SOME_ROLE_2"};

	private static final int IDX_USER_NAME = 1;
	private static final int IDX_TOKEN_SERIES = 2;
	private static final int IDX_TOKEN_VALUE = 3;
	private static final int IDX_TIMESTAMP = 4;

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private MockHttpSession session;
	@Autowired private WebApplicationContext wac;
	@Autowired private FilterChainProxy springSecurityFilterChain;
	@Autowired private UserDetailsService authenticationProvider;
	@Autowired private DataSource dataSource;

	@Mock private Connection connection;
	@Mock private PreparedStatement preparedStatement;
	@Mock private ResultSet resultSet;
	@Mock private UserDetails userDetails;
	@Mock private Authentication authentication;
	@Mock private SecurityContext securityContext;

	@Before
	public void setUp() throws Exception {
		givenMockitoMocksInjected();
		givenDataSourceConfigured();
		givenSpringWebContextConfigured();
	}

	private void givenMockitoMocksInjected() {
		MockitoAnnotations.initMocks(this);
		reset(authenticationProvider);
	}

	public void givenDataSourceConfigured() throws Exception {
		given(dataSource.getConnection()).willReturn(connection);
		given(connection.prepareStatement(anyString())).willReturn(preparedStatement);
		given(preparedStatement.executeQuery()).willReturn(resultSet);
	}

	private void givenSpringWebContextConfigured() {
		mvcMock = webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.alwaysDo(print())
				.build();
	}

	@Test
	public void shouldRedirectToLogInPageWhenNotLoggedIn() throws Exception {
		givenUserNotLoggedIn();

		whenPerformedGetForUrl(SOME_SECURED_TEST_CONTROLLER_URL);

		thenRedirectedTo("http://localhost" + URL_LOGIN_PAGE);
		thenAuthenticationProviderNotUsed();
	}

	@Test
	public void shouldGoFromLogInPageToLogInView() throws Exception {
		givenUserNotLoggedIn();

		whenPerformedGetForUrl(URL_LOGIN_PAGE);

		thenRenderView(VIEW_LOGIN_PAGE).andExpect(model().attribute("loggedOut", is(nullValue())));
		thenAuthenticationProviderNotUsed();
	}

	@Test
	public void shouldPerformLogInFailure() throws Exception {
		givenAuthenticatorProvidingUserDetails(USER_NAME, PASSWORD, SOME_ROLES);
		givenUserNotLoggedIn();

		whenPerformedLogInWith(USER_NAME, "wrong" + PASSWORD, DONT_REMEMBER_ME);

		thenUserLoadedByAuthenticationProvider(USER_NAME);
		thenBadCredentialsErrorInSession();
		thenUserIsNotLoggedIn();
		thenRememberMeCookieIsSetToNull();
		thenSessionCookieIsNotSet();
		thenRedirectedTo(URL_LOGIN_PAGE);
	}

	@Test
	public void shouldPerformLogInSuccessByPasswordWithoutRememberMe() throws Exception {
		givenAuthenticatorProvidingUserDetails(USER_NAME, PASSWORD, SOME_ROLES);
		givenUserNotLoggedIn();

		whenPerformedLogInWith(USER_NAME, PASSWORD, DONT_REMEMBER_ME);

		thenUserLoadedByAuthenticationProvider(USER_NAME);
		thenNoBadCredentialsErrorInSession();
		thenUserIsLoggedIn();
		thenRememberMeCookieIsNotSet();
		thenRedirectedTo(URL_LOGIN_SUCCESS);
	}

	@Test
	public void shouldPerformLogInSuccessByPasswordWithRememberMe() throws Exception {
		givenAuthenticatorProvidingUserDetails(USER_NAME, PASSWORD, SOME_ROLES);
		givenUserNotLoggedIn();

		whenPerformedLogInWith(USER_NAME, PASSWORD, REMEMBER_ME);

		thenUserLoadedByAuthenticationProvider(USER_NAME);
		thenNoBadCredentialsErrorInSession();
		thenUserIsLoggedIn();
		thenRememberMeCookieIsSet();
		thenRedirectedTo(URL_LOGIN_SUCCESS);
	}

	@Test
	public void shouldPerformLogInSuccessByRememberMe() throws Exception {
		givenAuthenticatorProvidingUserDetails(USER_NAME, PASSWORD, SOME_ROLES);
		givenUserNotLoggedIn();
		givenValidRememberMeTokenInDb(USER_NAME, TOKEN_SERIES, TOKEN_VALUE);

		whenAccessingSecuredUrlHavingCookie(createCookie(TOKEN_SERIES, TOKEN_VALUE));

		thenUserLoadedByAuthenticationProvider(USER_NAME);
		thenNoBadCredentialsErrorInSession();
		thenUserIsLoggedIn();
		thenRememberMeCookieIsSet();
		thenRenderView(VIEW_EXCEPTION_PAGE);
	}

	@Test
	public void shouldLogOut() throws Exception {
		givenUserLoggedInWithRoles("ROLE_USER");

		whenPerformedGetForUrl(URL_LOGOUT);

		thenAuthenticationProviderNotUsed();
		thenSecurityContextWasCleared();
		thenUserIsNotLoggedIn();
		thenRememberMeCookieIsSetToNull();
		thenSessionCookieIsSetToNull();
		thenRedirectedTo(URL_LOGOUT_PAGE);
	}

	@Test
	public void shouldGoFromLogOutPageToLogInView() throws Exception {
		givenUserNotLoggedIn();

		whenPerformedGetForUrl(URL_LOGOUT_PAGE);

		thenRenderView(VIEW_LOGIN_PAGE).andExpect(model().attribute("loggedOut", is(true)));
		thenAuthenticationProviderNotUsed();
	}

	@Test
	public void shouldUserControllersBeAccessibleForUser() throws Exception {
		shouldUrlBeAccessibleForUserInRoles(
				SOME_SECURED_TEST_CONTROLLER_URL, VIEW_EXCEPTION_PAGE,
				"ROLE_USER");
	}

	@Test
	public void shouldUserControllersBeAccessibleForAdmin() throws Exception {
		shouldUrlBeAccessibleForUserInRoles(
				SOME_SECURED_TEST_CONTROLLER_URL, VIEW_EXCEPTION_PAGE,
				"ROLE_ADMIN");
	}

	@Test
	public void shouldLoginPageResourcesBePubliclyAccessible() throws Exception {
		shouldResourceBePubliclyDownloadable("/css/public/common.css");
		shouldResourceBePubliclyDownloadable("/css/public/exception.css");
		shouldResourceBePubliclyDownloadable("/css/public/loginPage.css");
		shouldResourceBePubliclyDownloadable("/css/public/messages.css");

		shouldResourceBePubliclyDownloadable("/img/public/info.png");
		shouldResourceBePubliclyDownloadable("/img/public/warn.png");
		shouldResourceBePubliclyDownloadable("/img/public/error.png");
	}

	private void shouldUrlBeAccessibleForUserInRoles(String controllerUrl, String viewToRender, String... roles)
			throws Exception
	{
		givenUserLoggedInWithRoles(roles);

		whenPerformedGetForUrl(controllerUrl + "/" + viewToRender);

		thenRenderView(viewToRender);
	}

	private void shouldResourceBePubliclyDownloadable(String url) throws Exception {
		givenUserNotLoggedIn();

		whenPerformedGetForUrl(url);

		thenReceivedData();
	}

	private void givenUserNotLoggedIn() {
		session.removeAttribute(SPRING_SECURITY_CONTEXT_KEY);
	}

	private void givenValidRememberMeTokenInDb(String userName, String tokenSeries, String tokenValue)
			throws SQLException
	{
		given(resultSet.next()).willReturn(true, false);
		given(resultSet.getString(IDX_USER_NAME)).willReturn(userName);
		given(resultSet.getString(IDX_TOKEN_SERIES)).willReturn(tokenSeries);
		given(resultSet.getString(IDX_TOKEN_VALUE)).willReturn(tokenValue);
		given(resultSet.getTimestamp(IDX_TIMESTAMP)).willReturn(new Timestamp(MAX_VALUE/2));
		given(authenticationProvider.loadUserByUsername(USER_NAME)).willReturn(userDetails);
	}

	private void givenUserLoggedInWithRoles(String... roles) {
		doReturn(grantedAuthoritiesFor(roles)).when(authentication).getAuthorities();
		given(authentication.isAuthenticated()).willReturn(true);
		given(securityContext.getAuthentication()).willReturn(authentication);
		session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
	}

	private void givenAuthenticatorProvidingUserDetails(String userName, String password, String... roles) {
		given(authenticationProvider.loadUserByUsername(userName)).willReturn(userDetails);
		given(userDetails.getUsername()).willReturn(userName);
		given(userDetails.getPassword()).willReturn(encrypted(password));
		given(userDetails.isAccountNonExpired()).willReturn(true);
		given(userDetails.isAccountNonLocked()).willReturn(true);
		given(userDetails.isCredentialsNonExpired()).willReturn(true);
		given(userDetails.isEnabled()).willReturn(true);
		doReturn(grantedAuthoritiesFor(roles)).when(userDetails).getAuthorities();
	}

	private Cookie createCookie(String tokenSeries, String tokenValues) {
		return new Cookie(SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY,
				new String(Base64.encode((tokenSeries + ":" + tokenValues).getBytes()))
		);
	}

	private String encrypted(String password) {
		final int logRound = 10;
		return hashpw(password, gensalt(logRound));
	}

	private void whenPerformedGetForUrl(String url) throws Exception {
		mvcMockPerformResult = mvcMock.perform(get(url)
				.session(session));
	}

	private void whenPerformedLogInWith(String userName, String password, boolean rememberMe) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post(URL_LOGIN)
				.param("username", userName)
				.param("password", password)
				.param("rememberMe", Boolean.toString(rememberMe))
				.session(session));
	}

	private void whenAccessingSecuredUrlHavingCookie(Cookie cookie) throws Exception {
		mvcMockPerformResult = mvcMock.perform(
				get(SOME_SECURED_TEST_CONTROLLER_URL + "/" + VIEW_EXCEPTION_PAGE)
						.cookie(cookie)
						.session(session));
	}

	private ResultActions thenRedirectedTo(String url) throws Exception {
		return mvcMockPerformResult
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(url));
	}

	private void thenAuthenticationProviderNotUsed() {
		verifyZeroInteractions(authenticationProvider);
	}

	private ResultActions thenRenderView(String name) throws Exception {
		return mvcMockPerformResult
				.andExpect(status().isOk())
				.andExpect(view().name(name));
	}

	private void thenUserLoadedByAuthenticationProvider(String userName) {
		verify(authenticationProvider).loadUserByUsername(userName);
	}

	private void thenBadCredentialsErrorInSession() {
		BadCredentialsException securityLastException =
				(BadCredentialsException) newSession().getAttribute(AUTHENTICATION_EXCEPTION);

		assertThat(securityLastException.getMessage(), is(equalTo("Bad credentials")));
	}

	private void thenUserIsNotLoggedIn() throws Exception {
		assertThat(SecurityContextHolder.getContext().getAuthentication(), is(nullValue()));
		assertThat(newSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY), is(nullValue()));
	}

	private void thenNoBadCredentialsErrorInSession() {
		assertThat(newSession().getAttribute(AUTHENTICATION_EXCEPTION), is(nullValue()));
	}

	private void thenUserIsLoggedIn() {
		Authentication authenticationFromSession =
				((SecurityContext) newSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY)).getAuthentication();

		assertThat((UserDetails) authenticationFromSession.getPrincipal(), is(sameInstance(userDetails)));
		assertThat(toIterable(authenticationFromSession.getAuthorities()), containsInAnyOrder(SOME_ROLES));
		assertThat(authenticationFromSession.isAuthenticated(), is(true));
	}

	private void thenSessionCookieIsNotSet() throws Exception {
		mvcMockPerformResult
				.andExpect(cookie().doesNotExist("JSESSIONID"));
	}

	private void thenSessionCookieIsSetToNull() throws Exception {
		mvcMockPerformResult
				.andExpect(cookie().value("JSESSIONID", is(nullValue())));
	}

	private void thenRememberMeCookieIsNotSet() throws Exception {
		mvcMockPerformResult
				.andExpect(cookie().doesNotExist(SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY));
	}

	private void thenRememberMeCookieIsSetToNull() throws Exception {
		mvcMockPerformResult
				.andExpect(cookie().value(SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, is(nullValue())));
	}

	private void thenRememberMeCookieIsSet() throws Exception {
		mvcMockPerformResult
				.andExpect(cookie().exists(SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY));
	}

	private void thenSecurityContextWasCleared() {
		verify(securityContext).setAuthentication(null);
	}

	private void thenReceivedData() throws Exception {
		mvcMockPerformResult
				.andExpect(status().isOk())
				.andExpect(content().string(not(isEmptyOrNullString())));
	}

	private static Collection<SimpleGrantedAuthority> grantedAuthoritiesFor(String... roles) {
		Collection<SimpleGrantedAuthority> result = new HashSet<>();
		for (String role: roles) {
			result.add(new SimpleGrantedAuthority(role));
		}

		return result;
	}

	private Iterable<String> toIterable(Collection<? extends GrantedAuthority> authorities) {
		List<String> result = new ArrayList<>(authorities.size());
		for (GrantedAuthority authority : authorities) {
			result.add(authority.getAuthority());
		}

		return result;
	}

	private HttpSession newSession() {
		return mvcMockPerformResult.andReturn().getRequest().getSession();
	}

}
