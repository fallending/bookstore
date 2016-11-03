package pl.jojczykp.bookstore.controllers.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.consts.AuthConsts.IS_LOGGED_OUT_ATTRIBUTE;
import static pl.jojczykp.bookstore.consts.AuthConsts.LOGIN_VIEW;
import static pl.jojczykp.bookstore.consts.AuthConsts.URL_PAGE_LOGIN;
import static pl.jojczykp.bookstore.consts.AuthConsts.URL_PAGE_LOGOUT;

@Controller
public class AuthController {

	@RequestMapping(value = URL_PAGE_LOGIN, method = GET)
	public ModelAndView loginPage() {
		return new ModelAndView(LOGIN_VIEW);
	}

	@RequestMapping(value = URL_PAGE_LOGOUT, method = GET)
	public ModelAndView logoutPage() {
		ModelMap modelMap = new ModelMap().addAttribute(IS_LOGGED_OUT_ATTRIBUTE, true);
		return new ModelAndView(LOGIN_VIEW, modelMap);
	}

}
