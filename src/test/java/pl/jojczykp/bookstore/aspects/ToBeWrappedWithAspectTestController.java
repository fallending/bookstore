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

package pl.jojczykp.bookstore.aspects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ToBeWrappedWithAspectTestController {

	public static final String SOME_TO_BE_WRAPPED_CONTROLLER_URL = "/some/to/be/wrapped/test/controller/url";
	public static final String VIEW_NAME_FROM_WRAPPED_CONTROLLER = "viewNameFromWrappedController";

	@RequestMapping(value = SOME_TO_BE_WRAPPED_CONTROLLER_URL, method = GET)
	public String someControllerAction() {
		return VIEW_NAME_FROM_WRAPPED_CONTROLLER;
	}

}
