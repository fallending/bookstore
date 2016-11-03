package pl.jojczykp.bookstore.aspects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ToBeWrappedWithAspectTestController {

	public static final String SOME_TO_BE_WRAPPED_CONTROLLER_URL = "/some/to/be/wrapped/test/controller/url";
	public static final String VIEW_NAME_FROM_WRAPPED_CONTROLLER = "viewNameFromWrappedController";

	@RequestMapping(value = SOME_TO_BE_WRAPPED_CONTROLLER_URL, method = GET)
	public String someControllerAction() {
		return VIEW_NAME_FROM_WRAPPED_CONTROLLER;
	}

}
