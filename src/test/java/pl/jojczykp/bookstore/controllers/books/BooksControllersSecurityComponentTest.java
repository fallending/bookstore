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

package pl.jojczykp.bookstore.controllers.books;

import junitparams.Parameters;
import org.junit.Test;
import pl.jojczykp.bookstore.consts.BooksConsts;
import pl.jojczykp.bookstore.testutils.controllers.security.SecutityControllersTestAbstract;

import static junitparams.JUnitParamsRunner.$;

public class BooksControllersSecurityComponentTest extends SecutityControllersTestAbstract {

	public static Object[] accessibleViaPost() {
		return cartesian(booksUrlsViaPost(), $(ROLE_USER, ROLE_ADMIN));
	}

	public static Object[] deniedViaPost() {
		return cartesian(booksUrlsViaPost(), $(ROLE_UNAUTHORIZED));
	}

	public static Object[] accessibleViaGet() {
		return cartesian(booksUrlsViaGet(), $(ROLE_USER, ROLE_ADMIN));
	}

	public static Object[] deniedViaGet() {
		return cartesian(booksUrlsViaGet(), $(ROLE_UNAUTHORIZED));
	}

	@Test
	@Parameters(method = "accessibleViaPost")
	public void shouldBeAccessibleViaPost(String url, String role) {
		verifyAccessibleViaPost(url, role);
	}

	@Test
	@Parameters(method = "deniedViaPost")
	public void shouldBeDeniedViaPost(String url, String role) {
		verifyDeniedViaPost(url, role);
	}

	@Test
	@Parameters(method = "accessibleViaGet")
	public void shouldBeAccessibleViaGet(String url, String role) {
		verifyAccessibleViaGet(url, role);
	}

	@Test
	@Parameters(method = "deniedViaGet")
	public void shouldBeDeniedViaGet(String url, String role) {
		verifyDeniedViaGet(url, role);
	}

	private static Object[] booksUrlsViaPost() {
		return $(
				BooksConsts.URL_ACTION_CREATE,
				BooksConsts.URL_ACTION_UPDATE,
				BooksConsts.URL_ACTION_DELETE,
				BooksConsts.URL_ACTION_SORT,
				BooksConsts.URL_ACTION_GO_TO_PAGE,
				BooksConsts.URL_ACTION_SET_PAGE_SIZE);
	}

	private static Object[] booksUrlsViaGet() {
		return $(
				BooksConsts.URL_ACTION_DISPLAY,
				BooksConsts.URL_ACTION_DOWNLOAD);
	}

}
