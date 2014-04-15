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

import java.util.List;

public final class SuppressUnchecked {

	private SuppressUnchecked() {
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> suppressUnchecked(List list) {
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	public static List<String> suppressUnchecked(Object object) {
		return (List<String>) object;
	}

}
