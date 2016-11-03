package pl.jojczykp.bookstore.testutils.controllers.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MakeControllerAcceptAnyRequestsAspect {

	@Before("execution(@org.springframework.web.bind.annotation.RequestMapping * *.*(..))")
	public void acceptAnyRequest() {
		throw new MakeControllerAcceptAnyRequestException();
	}

}
