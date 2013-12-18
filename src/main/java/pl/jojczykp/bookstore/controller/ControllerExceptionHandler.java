package pl.jojczykp.bookstore.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.command.ExceptionCommand;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static pl.jojczykp.bookstore.controller.BooksConsts.EXCEPTION_VIEW;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ModelAndView handleAnyException(RuntimeException ex) {
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
