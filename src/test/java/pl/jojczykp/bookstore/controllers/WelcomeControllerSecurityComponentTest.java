package pl.jojczykp.bookstore.controllers;

import junitparams.Parameters;
import org.junit.Test;
import pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier;
import pl.jojczykp.bookstore.testutils.controllers.security.SecurityControllersTestAbstract;

import static junitparams.JUnitParamsRunner.$;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier.allow;

public class WelcomeControllerSecurityComponentTest extends SecurityControllersTestAbstract {

	private static final String URL = "/";

	public static Object[] rules() {
		return $(
				allow().method(GET).url(URL).role(ROLE_ADMIN),
				allow().method(GET).url(URL).role(ROLE_USER),
				allow().method(GET).url(URL).role(ROLE_UNAUTHORIZED)
		);
	}

	@Test
	@Parameters(method = "rules")
	public void shouldBeHaveDesiredAccess(HttpAccessVerifier verifier) {
		verifier.verify(getMockedContext());
	}

}
