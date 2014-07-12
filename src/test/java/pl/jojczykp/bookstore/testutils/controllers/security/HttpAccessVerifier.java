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

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class HttpAccessVerifier {

	private RequestMethod method;
	private String url;
	private String role;
	private HttpStatus expectedStatus;

	public static HttpAccessVerifier allowFind() {
		return new HttpAccessVerifier(HttpStatus.FOUND);
	}

	public static HttpAccessVerifier allow() {
		return new HttpAccessVerifier(HttpStatus.ACCEPTED);
	}

	public static HttpAccessVerifier deny() {
		return new HttpAccessVerifier(HttpStatus.FORBIDDEN);
	}

	private HttpAccessVerifier(HttpStatus expectedStatus) {
		this.expectedStatus = expectedStatus;
	}

	public HttpAccessVerifier method(RequestMethod method) {
		this.method = method;
		return this;
	}

	public HttpAccessVerifier url(String url) {
		this.url = url;
		return this;
	}

	public HttpAccessVerifier role(String role) {
		this.role = role;
		return this;
	}

	public void verify(MockMvc mvcMock) {
		try {
			mvcMock.perform(requestFor(method, url)
					.with(user("someUser").roles(role)))
					.andExpect(status().is(expectedStatus.value()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private MockHttpServletRequestBuilder requestFor(RequestMethod method, String url) {
		switch (method) {
			case GET: return get(url);
			case POST: return post(url);
			default: throw new RuntimeException("No implementation for " + method);
		}
	}

}
