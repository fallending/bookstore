package bookstore.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToWelcomePage(ModelMap model) {
		model.addAttribute("offset", 0);
		model.addAttribute("limit", 10);

		return "redirect:books/list";
	}
}
