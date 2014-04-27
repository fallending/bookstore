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


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.jojczykp.bookstore.testutils.jmx.JmxClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:spring/applicationContext/jmx-context.xml",
		"classpath:spring/jmx-test-context.xml"
})
public class ConfigMBeanComponentTest {

	private static final String JMX_SERVICE_URL_PATTERN =
			"service:jmx:rmi://localhost/jndi/rmi://localhost:%d/%s";
	private static final String JMX_OBJECT_NAME = "custom.application.mbeans:name=ConfigMBean";

	@Value("${jmx.port}") private int jmxPort;
	@Value("${jmx.connector.name}") private String jmxConnectorName;
	private JmxClient jmxClient;

	@Before
	public void givenJmxConnection() {
		MockitoAnnotations.initMocks(this);
		establishJmxConnection();
	}

	private void establishJmxConnection() {
		String jmxServiceUrl = String.format(JMX_SERVICE_URL_PATTERN, jmxPort, jmxConnectorName);
		jmxClient = new JmxClient(jmxServiceUrl, JMX_OBJECT_NAME);
	}

	@Test
	public void shouldEnable() {
		enableRequestMappingLogging();
		boolean isEnabled = isRequestMappingLoggingEnabled();

		assertThat(isEnabled, is(true));
	}

	@Test
	public void shouldDisable() {
		disableRequestMappingLogging();
		boolean isEnabled = isRequestMappingLoggingEnabled();

		assertThat(isEnabled, is(false));
	}

	private void enableRequestMappingLogging() {
		jmxClient.invoke("enableRequestMappingLogging");
	}

	private Object disableRequestMappingLogging() {
		return jmxClient.invoke("disableRequestMappingLogging");
	}

	private boolean isRequestMappingLoggingEnabled() {
		return (boolean) jmxClient.invoke("isRequestMappingLoggingEnabled");
	}

}
