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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.jojczykp.bookstore.utils;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isPrivate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class SuppressUncheckedUnitTest {

	@Test
	public void shouldListSuppressUncheckedReturnSameInstance() {
		List list = new ArrayList();

		List<?> result = SuppressUnchecked.suppressUnchecked(list);

		assertThat(result, is(sameInstance(list)));
	}

	@Test
	public void shouldObjectSuppressUncheckedReturnSameInstance() {
		Object listAsObject = new ArrayList<>();

		List<String> result = SuppressUnchecked.suppressUnchecked(listAsObject);

		assertThat(result, is(sameInstance(listAsObject)));
	}

	@Test
	public void shouldHavePrivateConstructor() throws Exception {
		Constructor<SuppressUnchecked> constructor = SuppressUnchecked.class.getDeclaredConstructor();
		assertThat(isPrivate(constructor.getModifiers()), is(true));

		constructor.setAccessible(true);
		constructor.newInstance();
	}

}
