package pl.jojczykp.bookstore.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DISPLAY;

@Controller
public class WelcomeController {

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/", method = GET)
	public RedirectView redirectToWelcomePage(HttpServletRequest request) {
		return new RedirectView(request.getContextPath() + URL_ACTION_DISPLAY);
	}

}
