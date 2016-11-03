package pl.jojczykp.bookstore.controllers.books;

import junitparams.Parameters;
import org.junit.Test;
import pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier;
import pl.jojczykp.bookstore.testutils.controllers.security.SecurityControllersTestAbstract;

import static junitparams.JUnitParamsRunner.$;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_CREATE;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DELETE;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DISPLAY;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DOWNLOAD;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_GO_TO_PAGE;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_SET_PAGE_SIZE;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_SORT;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_UPDATE;
import static pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier.allow;
import static pl.jojczykp.bookstore.testutils.controllers.security.HttpAccessVerifier.deny;

public class BooksControllersSecurityComponentTest extends SecurityControllersTestAbstract {

	public static Object[] rules() {
		return $(
			allow().method(POST).url(URL_ACTION_CREATE).role(ROLE_ADMIN),
			deny().method(POST).url(URL_ACTION_CREATE).role(ROLE_USER),
			deny().method(POST).url(URL_ACTION_CREATE).role(ROLE_UNAUTHORIZED),

			allow().method(POST).url(URL_ACTION_UPDATE).role(ROLE_ADMIN),
			deny().method(POST).url(URL_ACTION_UPDATE).role(ROLE_USER),
			deny().method(POST).url(URL_ACTION_UPDATE).role(ROLE_UNAUTHORIZED),

			allow().method(POST).url(URL_ACTION_DELETE).role(ROLE_ADMIN),
			deny().method(POST).url(URL_ACTION_DELETE).role(ROLE_USER),
			deny().method(POST).url(URL_ACTION_DELETE).role(ROLE_UNAUTHORIZED),

			deny().method(POST).url(URL_ACTION_SORT).role(ROLE_ADMIN),
			allow().method(POST).url(URL_ACTION_SORT).role(ROLE_USER),
			deny().method(POST).url(URL_ACTION_SORT).role(ROLE_UNAUTHORIZED),

			deny().method(POST).url(URL_ACTION_GO_TO_PAGE).role(ROLE_ADMIN),
			allow().method(POST).url(URL_ACTION_GO_TO_PAGE).role(ROLE_USER),
			deny().method(POST).url(URL_ACTION_GO_TO_PAGE).role(ROLE_UNAUTHORIZED),

			deny().method(POST).url(URL_ACTION_SET_PAGE_SIZE).role(ROLE_ADMIN),
			allow().method(POST).url(URL_ACTION_SET_PAGE_SIZE).role(ROLE_USER),
			deny().method(POST).url(URL_ACTION_SET_PAGE_SIZE).role(ROLE_UNAUTHORIZED),

			allow().method(GET).url(URL_ACTION_DISPLAY).role(ROLE_USER),
			deny().method(GET).url(URL_ACTION_DISPLAY).role(ROLE_ADMIN),
			deny().method(GET).url(URL_ACTION_DISPLAY).role(ROLE_UNAUTHORIZED),

			allow().method(GET).url(URL_ACTION_DOWNLOAD).role(ROLE_USER),
			deny().method(GET).url(URL_ACTION_DOWNLOAD).role(ROLE_ADMIN),
			deny().method(GET).url(URL_ACTION_DOWNLOAD).role(ROLE_UNAUTHORIZED)
		);
	}

	@Test
	@Parameters(method = "rules")
	public void shouldBeHaveDesiredAccess(HttpAccessVerifier verifier) {
		verifier.verify(getMockedContext());
	}

}
