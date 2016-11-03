package pl.jojczykp.bookstore.controllers.errors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ExceptionThrowingTestController {

	public static final String THROW_EXCEPTION_CONTROLLER_URL = "/throw";

	@RequestMapping(value = THROW_EXCEPTION_CONTROLLER_URL + "/{exceptionClassName}/", method = POST)
	public ModelAndView throwException(@PathVariable String exceptionClassName) throws Exception {
		throw (Exception) Class.forName(exceptionClassName)
				.getConstructor(String.class).newInstance("some exception message");
	}

}
