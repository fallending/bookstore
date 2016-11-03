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
