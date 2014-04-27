/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

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
