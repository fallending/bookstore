package pl.jojczykp.bookstore.controllers.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class SomeSecuredTestController {

	public static final String SOME_SECURED_TEST_CONTROLLER_URL = "/some/secured/test/controller/url";

	@RequestMapping(value = SOME_SECURED_TEST_CONTROLLER_URL + "/{viewToRender}", method = GET)
	public String someControllerAction(@PathVariable String viewToRender) {
		return viewToRender;
	}

}
