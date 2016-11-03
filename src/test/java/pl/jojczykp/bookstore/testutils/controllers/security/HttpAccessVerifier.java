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
