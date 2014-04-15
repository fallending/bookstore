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

package pl.jojczykp.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.controllers.SecurityConsts.LOGIN_VIEW;
import static pl.jojczykp.bookstore.controllers.SecurityConsts.LOGOUT_ATTRIBUTE;
import static pl.jojczykp.bookstore.controllers.SecurityConsts.URL_PAGE_LOGIN;
import static pl.jojczykp.bookstore.controllers.SecurityConsts.URL_PAGE_LOGOUT;

@Controller
public class SecurityController {

	@RequestMapping(value = URL_PAGE_LOGIN, method = GET)
	public ModelAndView loginPage() {
		return new ModelAndView(LOGIN_VIEW);
	}

	@RequestMapping(value = URL_PAGE_LOGOUT, method = GET)
	public ModelAndView logoutPage() {
		ModelMap modelMap = new ModelMap().addAttribute(LOGOUT_ATTRIBUTE, true);
		return new ModelAndView(LOGIN_VIEW, modelMap);
	}

}
