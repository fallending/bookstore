package pl.jojczykp.bookstore.controllers.auth;

import junitparams.Parameters;
import org.junit.Test;
import pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier;
import pl.jojczykp.bookstore.testutils.controllers.security.SecurityControllersTestAbstract;

import static junitparams.JUnitParamsRunner.$;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier.allow;

public class AuthControllerPageSecurityComponentTest extends SecurityControllersTestAbstract {

	public static Object[] rules() {
		return $(
				allow().method(GET).url("/auth/loginPage").role(ROLE_USER),
				allow().method(GET).url("/auth/loginPage").role(ROLE_ADMIN),
				allow().method(GET).url("/auth/loginPage").role(ROLE_UNAUTHORIZED),

				allow().method(GET).url("/auth/logoutPage").role(ROLE_USER),
				allow().method(GET).url("/auth/logoutPage").role(ROLE_ADMIN),
				allow().method(GET).url("/auth/logoutPage").role(ROLE_UNAUTHORIZED)
		);
	}

	@Test
	@Parameters(method = "rules")
	public void shouldBeHaveDesiredAccess(HttpAccessVerifier verifier) {
		verifier.verify(getMockedContext());
	}

}
