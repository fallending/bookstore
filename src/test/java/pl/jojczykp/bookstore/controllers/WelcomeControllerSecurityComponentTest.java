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
