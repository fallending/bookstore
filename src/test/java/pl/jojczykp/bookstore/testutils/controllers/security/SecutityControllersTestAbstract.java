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
import java.util.ArrayList;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(JUnitParamsRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"classpath:spring/controllers-test-context.xml" ,
		"classpath:spring/controllers-mock-context.xml" ,
		"classpath:spring/authentication-provider-mock-context.xml",
		"classpath:spring/applicationContext/security-context.xml"})
public abstract class SecutityControllersTestAbstract {

	public static final String ROLE_USER = "USER";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_UNAUTHORIZED = "UNAUTHORIZED";

	private MockMvc mvcMock;
	@Autowired private WebApplicationContext wac;
	@Autowired private Filter springSecurityFilterChain;

	@Before
	public void setUpContext() throws Exception {
		new TestContextManager(getClass()).prepareTestInstance(this);
		mvcMock = webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.alwaysDo(print())
				.build();
	}

	public void verifyAccessibleViaPost(String url, String role) {
		verifyAccess(post(url), role, status().isAccepted());
	}

	public void verifyDeniedViaPost(String url, String role) {
		verifyAccess(post(url), role, status().isForbidden());
	}

	public void verifyAccessibleViaGet(String url, String role) {
		verifyAccess(get(url), role, status().isAccepted());
	}

	public void verifyDeniedViaGet(String url, String role) {
		verifyAccess(get(url), role, status().isForbidden());
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

	public static Object[] cartesian(Object[] elements1, Object[] elements2) {
		List<Object[]> result = new ArrayList<>(elements1.length * elements2.length);
		for (Object element1 : elements1) {
			for (Object element2 : elements2) {
				result.add($(element1, element2));
			}
		}

		return result.toArray();
	}

}
