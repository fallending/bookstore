package pl.jojczykp.bookstore.controllers.errors;

import junitparams.Parameters;
import org.junit.Test;
import pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier;
import pl.jojczykp.bookstore.testutils.controllers.security.SecurityControllersTestAbstract;

import static junitparams.JUnitParamsRunner.$;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier.allow;

public class HttpErrorControllerSecurityComponentTest extends SecurityControllersTestAbstract {

	public static final String URL_HTTP_ERROR_403 = "/httpError/403.html";
	public static final String URL_HTTP_ERROR_404 = "/httpError/404.html";
	public static final String URL_HTTP_ERROR_405 = "/httpError/405.html";

	public static Object[] rules() {
		return $(
				allow().method(POST).url(URL_HTTP_ERROR_403).role(ROLE_ADMIN),
				allow().method(POST).url(URL_HTTP_ERROR_403).role(ROLE_USER),
				allow().method(POST).url(URL_HTTP_ERROR_403).role(ROLE_UNAUTHORIZED),
				allow().method(POST).url(URL_HTTP_ERROR_404).role(ROLE_ADMIN),
				allow().method(POST).url(URL_HTTP_ERROR_404).role(ROLE_USER),
				allow().method(POST).url(URL_HTTP_ERROR_404).role(ROLE_UNAUTHORIZED),
				allow().method(POST).url(URL_HTTP_ERROR_405).role(ROLE_ADMIN),
				allow().method(POST).url(URL_HTTP_ERROR_405).role(ROLE_USER),
				allow().method(POST).url(URL_HTTP_ERROR_405).role(ROLE_UNAUTHORIZED),

				allow().method(GET).url(URL_HTTP_ERROR_403).role(ROLE_ADMIN),
				allow().method(GET).url(URL_HTTP_ERROR_403).role(ROLE_USER),
				allow().method(GET).url(URL_HTTP_ERROR_403).role(ROLE_UNAUTHORIZED),
				allow().method(GET).url(URL_HTTP_ERROR_404).role(ROLE_ADMIN),
				allow().method(GET).url(URL_HTTP_ERROR_404).role(ROLE_USER),
				allow().method(GET).url(URL_HTTP_ERROR_404).role(ROLE_UNAUTHORIZED),
				allow().method(GET).url(URL_HTTP_ERROR_405).role(ROLE_ADMIN),
				allow().method(GET).url(URL_HTTP_ERROR_405).role(ROLE_USER),
				allow().method(GET).url(URL_HTTP_ERROR_405).role(ROLE_UNAUTHORIZED)
		);
	}

	@Test
	@Parameters(method = "rules")
	public void shouldBeHaveDesiredAccess(HttpAccessVerifier verifier) {
		verifier.verify(getMockedContext());
	}

}
