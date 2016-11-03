package pl.jojczykp.bookstore.controllers.auth;

import junitparams.Parameters;
import org.junit.Test;
import pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier;
import pl.jojczykp.bookstore.testutils.controllers.security.SecurityControllersTestAbstract;

import static junitparams.JUnitParamsRunner.$;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier.allowFind;

public class AuthControllerActionSecurityComponentTest extends SecurityControllersTestAbstract {

	public static Object[] rules() {
		return $(
				allowFind().method(POST).url("/auth/login").role(ROLE_USER),
				allowFind().method(POST).url("/auth/login").role(ROLE_ADMIN),
				allowFind().method(POST).url("/auth/login").role(ROLE_UNAUTHORIZED),

				allowFind().method(POST).url("/auth/logout").role(ROLE_USER),
				allowFind().method(POST).url("/auth/logout").role(ROLE_ADMIN),
				allowFind().method(POST).url("/auth/logout").role(ROLE_UNAUTHORIZED)
		);
	}

	@Test
	@Parameters(method = "rules")
	public void shouldHaveDesiredAccess(HttpAccessVerifier verifier) {
		verifier.verify(getMockedContext());
	}

}
