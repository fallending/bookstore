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

package pl.jojczykp.bookstore.controllers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.commands.HttpErrorCommand;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class HttpErrorController {

	@RequestMapping(value = "/httpError/{id}", method = {GET, POST})
	public ModelAndView handleHttpError(@PathVariable int id, HttpServletRequest request) {
		HttpErrorCommand httpErrorCommand = httpErrorCommandFor(id, request);
		return new ModelAndView("httpError", modelMapFor(httpErrorCommand));
	}

	private HttpErrorCommand httpErrorCommandFor(int id, HttpServletRequest request) {
		String originalUrl = (String) request.getAttribute("javax.servlet.forward.request_uri");

		HttpErrorCommand httpErrorCommand = new HttpErrorCommand();
		httpErrorCommand.setId(id);
		httpErrorCommand.setOriginalUrl(originalUrl);
		httpErrorCommand.setDescription(HttpStatus.valueOf(id).getReasonPhrase());

		return httpErrorCommand;
	}

	private ModelMap modelMapFor(HttpErrorCommand httpErrorCommand) {
		return new ModelMap().addAttribute("httpErrorCommand", httpErrorCommand);
	}

}
