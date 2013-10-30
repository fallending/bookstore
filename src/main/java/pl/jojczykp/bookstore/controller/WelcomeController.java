package pl.jojczykp.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import static pl.jojczykp.bookstore.controller.BooksConsts.URL_ACTION_LIST;

@Controller
public class WelcomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public RedirectView redirectToWelcomePage() {
		return new RedirectView(URL_ACTION_LIST);
	}
}
