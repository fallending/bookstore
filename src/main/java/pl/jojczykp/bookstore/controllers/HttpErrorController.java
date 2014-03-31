package pl.jojczykp.bookstore.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jojczykp.bookstore.commands.HttpErrorCommand;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class HttpErrorController {

	@RequestMapping(value = "/httpError/{id}", method = {GET, POST})
	public ModelAndView handleHttpError(@PathVariable int id, HttpServletRequest request) {
		HttpErrorCommand httpErrorCommand = httpErrorCommandFor(id, request);
		return new ModelAndView("httpError", modelMapFor(httpErrorCommand));
	}

	private HttpErrorCommand httpErrorCommandFor(int id, HttpServletRequest request) {
		String originalUrl = (String) request.getAttribute("javax.servlet.forward.request_uri");

		HttpErrorCommand httpErrorCommand = new HttpErrorCommand();
		httpErrorCommand.setId(id);
		httpErrorCommand.setOriginalUrl(originalUrl);
		httpErrorCommand.setDescription(HttpStatus.valueOf(id).getReasonPhrase());

		return httpErrorCommand;
	}

	private ModelMap modelMapFor(HttpErrorCommand httpErrorCommand) {
		return new ModelMap().addAttribute("httpErrorCommand", httpErrorCommand);
	}

}
