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

package pl.jojczykp.bookstore.controllers.security;

import junitparams.Parameters;
import org.junit.Test;
import pl.jojczykp.bookstore.testutils.controllers.security.SecutityControllersTestAbstract;

import static junitparams.JUnitParamsRunner.$;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityControllerSecurityActionComponentTest extends SecutityControllersTestAbstract {

	public static final Object[] ALL_ROLES = $(ROLE_USER, ROLE_ADMIN, ROLE_UNAUTHORIZED);

	public static Object[] loginActionAccessibleViaPost() {
		return cartesian($("/security/login"), ALL_ROLES);
	}

	public static Object[] logoutActionAccessibleViaPost() {
		return cartesian($("/security/logout"), ALL_ROLES);
	}

	@Test
	@Parameters(method = "loginActionAccessibleViaPost, logoutActionAccessibleViaPost")
	public void shouldLoginBeAccessibleViaPost(String url, String role) {
		verifyAccess(post(url), role, status().isFound());
	}

}
