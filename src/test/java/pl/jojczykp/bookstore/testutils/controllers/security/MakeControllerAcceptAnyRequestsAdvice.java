package pl.jojczykp.bookstore.testutils.controllers.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MakeControllerAcceptAnyRequestsAdvice {

	@ExceptionHandler(MakeControllerAcceptAnyRequestException.class)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public void handleAccessDeniedException() {
	}

}
