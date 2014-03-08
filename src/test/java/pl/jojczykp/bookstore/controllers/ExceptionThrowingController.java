package pl.jojczykp.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionThrowingController {

	@RequestMapping(value = "/throw/some_exception")
	public ModelAndView throwException() {
		throw new RuntimeException("a controllers exception");
	}

}
