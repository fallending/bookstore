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

package pl.jojczykp.bookstore.jmx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

@Service
@ManagedResource(
		objectName="custom.application.mbeans:name=ConfigMBean",
		description="Configuration Management Bean")
public class ConfigMBean {

	@Value("${default.request.mapping.logging.enabled}") private boolean requestMappingLoggingEnabled;

	@ManagedOperation(description="Checks if Controllers RequestMapping methods logging is enabled")
	public boolean isRequestMappingLoggingEnabled() {
		return requestMappingLoggingEnabled;
	}

	@ManagedOperation(description="Enable Controllers RequestMapping methods logging")
	public void enableRequestMappingLogging() {
		requestMappingLoggingEnabled = true;
	}

	@ManagedOperation(description="Disable Controllers RequestMapping methods logging")
	public void disableRequestMappingLogging() {
		requestMappingLoggingEnabled = false;
	}

}
