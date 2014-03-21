package pl.jojczykp.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ExceptionThrowingTestController {

	public static final String THROW_EXCEPTION_CONTROLLER_URL = "/throw/some_exception";

	@RequestMapping(value = THROW_EXCEPTION_CONTROLLER_URL, method = GET)
	public ModelAndView throwException() {
		throw new RuntimeException("a controllers exception");
	}

}
