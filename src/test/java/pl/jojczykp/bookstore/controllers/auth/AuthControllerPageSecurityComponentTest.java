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

package pl.jojczykp.bookstore.controllers.auth;

import junitparams.Parameters;
import org.junit.Test;
import pl.jojczykp.bookstore.testutils.controllers.security.SecutityControllersTestAbstract;

import static junitparams.JUnitParamsRunner.$;
import static pl.jojczykp.bookstore.consts.SecurityConsts.URL_PAGE_LOGIN;
import static pl.jojczykp.bookstore.consts.SecurityConsts.URL_PAGE_LOGOUT;

public class AuthControllerPageSecurityComponentTest extends SecutityControllersTestAbstract {

	public static Object[] loginPageAccessibleViaGet() {
		return cartesian($(URL_PAGE_LOGIN), $(ROLE_USER, ROLE_ADMIN, ROLE_UNAUTHORIZED));
	}

	public static Object[] logoutPageAccessibleViaGet() {
		return cartesian($(URL_PAGE_LOGOUT), $(ROLE_USER, ROLE_ADMIN, ROLE_UNAUTHORIZED));
	}

	@Test
	@Parameters(method = "loginPageAccessibleViaGet, logoutPageAccessibleViaGet")
	public void shouldBeAccessibleViaGet(String url, String role) {
		verifyAccessibleViaGet(url, role);
	}

}
