package pl.jojczykp.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.controllers.BooksConsts.URL_ACTION_READ;

@Controller
public class WelcomeController {

	@RequestMapping(value = "/", method = GET)
	public RedirectView redirectToWelcomePage() {
		return new RedirectView(URL_ACTION_READ);
	}
}
