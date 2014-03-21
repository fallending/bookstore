package pl.jojczykp.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.controllers.SecurityConsts.LOGIN_VIEW;
import static pl.jojczykp.bookstore.controllers.SecurityConsts.LOGOUT_ATTRIBUTE;
import static pl.jojczykp.bookstore.controllers.SecurityConsts.URL_PAGE_LOGIN;
import static pl.jojczykp.bookstore.controllers.SecurityConsts.URL_PAGE_LOGOUT;

@Controller
public class SecurityController {

	@RequestMapping(value = URL_PAGE_LOGIN, method = GET)
	public ModelAndView loginPage() {
		return new ModelAndView(LOGIN_VIEW);
	}

	@RequestMapping(value = URL_PAGE_LOGOUT, method = GET)
	public ModelAndView logoutPage() {
		ModelMap modelMap = new ModelMap().addAttribute(LOGOUT_ATTRIBUTE, true);
		return new ModelAndView(LOGIN_VIEW, modelMap);
	}

}
