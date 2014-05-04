/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package pl.jojczykp.bookstore.controllers.security;

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
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pl.jojczykp.bookstore.security.CustomDbAuthenticationProvider;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.security.web.WebAttributes.AUTHENTICATION_EXCEPTION;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"classpath:spring/controllers-test-context.xml",
		"classpath:spring/authentication-provider-mock-context.xml",
		"classpath:spring/applicationContext/security-context.xml",
		"classpath:spring/dispatcherServletContext/resources-context.xml",
		"classpath:spring/dispatcherServletContext/views-context.xml"})
public class SecurityControllerComponentTest {

	private static final String URL_LOGIN_PAGE = "/security/loginPage";
	private static final String URL_LOGIN = "/security/login";
	private static final String URL_LOGIN_SUCCESS = "/";
	private static final String URL_LOGOUT = "/security/logout";
	private static final String URL_LOGOUT_PAGE = "/security/logoutPage";

	private static final String VIEW_LOGIN_PAGE = "loginPage";
	private static final String VIEW_EXCEPTION_PAGE = "exception";

	private static final String USER_NAME = "user_name";
	private static final String PASSWORD = "password";

	private static final String[] SOME_ROLES = {"SOME_ROLE_1", "SOME_ROLE_2"};

	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;
	@Autowired private MockHttpSession session;
	@Autowired private WebApplicationContext wac;
	@Autowired private FilterChainProxy springSecurityFilterChain;
	@Autowired private CustomDbAuthenticationProvider authenticationProvider;

	@Mock private UserDetails userDetails;
	@Mock private Authentication authentication;
	@Mock private SecurityContext securityContext;

	@Before
	public void setUp() {
		mvcMock = webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.alwaysDo(print())
				.build();
		MockitoAnnotations.initMocks(this);
		reset(authenticationProvider);
	}

	@Test
	public void shouldRedirectToLogInPageWhenNotLoggedIn() throws Exception {
		givenUserNotLoggedIn();

		whenPerformedGetForUrl(SomeSecuredTestController.SOME_SECURED_TEST_CONTROLLER_URL);

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

		whenPerformedLogInWith(USER_NAME, "wrong" + PASSWORD);

		thenUserLoadedByAuthenticationProvider(USER_NAME);
		thenBadCredentialsErrorInSession();
		thenUserIsNotLoggedIn();
		thenRedirectedTo(URL_LOGIN_PAGE);
	}

	@Test
	public void shouldPerformLogInSuccess() throws Exception {
		givenAuthenticatorProvidingUserDetails(USER_NAME, PASSWORD, SOME_ROLES);
		givenUserNotLoggedIn();

		whenPerformedLogInWith(USER_NAME, PASSWORD);

		thenUserLoadedByAuthenticationProvider(USER_NAME);
		thenNoBadCredentialsErrorInSession();
		thenUserIsLoggedIn();
		thenRedirectedTo(URL_LOGIN_SUCCESS);
	}

	@Test
	public void shouldLogOut() throws Exception {
		givenUserLoggedInWithRoles("ROLE_USER");

		whenPerformedGetForUrl(URL_LOGOUT);

		thenAuthenticationProviderNotUsed();
		thenSecurityContextWasCleared();
		thenUserIsNotLoggedIn();
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
				SomeSecuredTestController.SOME_SECURED_TEST_CONTROLLER_URL, VIEW_EXCEPTION_PAGE,
				"ROLE_USER");
	}

	@Test
	public void shouldUserControllersBeAccessibleForAdmin() throws Exception {
		shouldUrlBeAccessibleForUserInRoles(
				SomeSecuredTestController.SOME_SECURED_TEST_CONTROLLER_URL, VIEW_EXCEPTION_PAGE,
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

	private void givenUserLoggedInWithRoles(String... roles) {
		doReturn(grantedAuthoritiesFor(roles)).when(authentication).getAuthorities();
		given(authentication.isAuthenticated()).willReturn(true);
		given(securityContext.getAuthentication()).willReturn(authentication);
		session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
	}

	private void givenAuthenticatorProvidingUserDetails(String userName, String password, String... roles) {
		given(authenticationProvider.loadUserByUsername(userName)).willReturn(userDetails);
		given(userDetails.getUsername()).willReturn(userName);
		given(userDetails.getPassword()).willReturn(password);
		given(userDetails.isAccountNonExpired()).willReturn(true);
		given(userDetails.isAccountNonLocked()).willReturn(true);
		given(userDetails.isCredentialsNonExpired()).willReturn(true);
		given(userDetails.isEnabled()).willReturn(true);
		doReturn(grantedAuthoritiesFor(roles)).when(userDetails).getAuthorities();
	}

	private void whenPerformedGetForUrl(String url) throws Exception {
		mvcMockPerformResult = mvcMock.perform(get(url)
				.session(session));
	}

	private void whenPerformedLogInWith(String userName, String password) throws Exception {
		mvcMockPerformResult = mvcMock.perform(post(URL_LOGIN)
				.param("username", userName)
				.param("password", password)
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

	private void thenUserIsNotLoggedIn() {
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
		assertThat(authenticationFromSession.getCredentials(), is(nullValue()));
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
