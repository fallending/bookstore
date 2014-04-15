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

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.commands.ExceptionCommand;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static pl.jojczykp.bookstore.controllers.BooksConsts.EXCEPTION_VIEW;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ModelAndView handleAnyException(Exception ex) {
		ModelAndView model = new ModelAndView(EXCEPTION_VIEW);
		model.addObject("exceptionCommand", anExceptionCommandFor(ex));

		return model;
	}

	private ExceptionCommand anExceptionCommandFor(Exception ex) {
		ExceptionCommand exceptionCommand = new ExceptionCommand();
		exceptionCommand.setMessage(ex.getMessage());
		exceptionCommand.setStackTraceAsString(getStackTrace(ex));

		return exceptionCommand;
	}
}
