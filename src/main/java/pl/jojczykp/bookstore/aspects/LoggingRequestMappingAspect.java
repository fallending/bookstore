package pl.jojczykp.bookstore.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import pl.jojczykp.bookstore.jmx.ConfigMBean;

import static org.apache.log4j.Logger.getLogger;

@Aspect
public class LoggingRequestMappingAspect {

	private final Logger logger = getLogger(LoggingRequestMappingAspect.class);

	@Autowired private ConfigMBean configMBean;

	@Around("execution(@org.springframework.web.bind.annotation.RequestMapping * *.*(..))")
	//CHECKSTYLE.OFF: IllegalThrowsCheck - Rethrowing original Throwable from wrapped method if any
	public Object logRequestMappingMethod(ProceedingJoinPoint pjp) throws Throwable {
	//CHECKSTYLE.ON: IllegalThrowsCheck
		logBeforeIfEnabled(pjp);
		Object result = pjp.proceed();
		logAfterIfEnabled(pjp);
		return result;
	}

	private void logBeforeIfEnabled(ProceedingJoinPoint pjp) {
		if (configMBean.isRequestMappingLoggingEnabled()) {
			logger.info("Invoking " + pjp.toShortString());
		}
	}

	private void logAfterIfEnabled(ProceedingJoinPoint pjp) {
		if (configMBean.isRequestMappingLoggingEnabled()) {
			logger.info("Done " + pjp.toShortString());
		}
	}

}
