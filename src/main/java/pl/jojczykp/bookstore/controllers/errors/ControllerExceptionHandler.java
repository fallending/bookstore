package pl.jojczykp.bookstore.controllers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.commands.errors.ExceptionCommand;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static pl.jojczykp.bookstore.consts.BooksConsts.EXCEPTION_VIEW;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
		return redirectToExceptionView(ex);
	}

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
