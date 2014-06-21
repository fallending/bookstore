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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.commands.errors.ExceptionCommand;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static pl.jojczykp.bookstore.consts.BooksConsts.EXCEPTION_VIEW;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
		return redirectToExceptionView(ex);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleAnyException(Exception ex) {
		return redirectToExceptionView(ex);
	}

	private ModelAndView redirectToExceptionView(Exception ex) {
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
