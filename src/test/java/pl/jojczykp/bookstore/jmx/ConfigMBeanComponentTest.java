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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.jojczykp.bookstore.testutils.jmx.JmxClient;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static pl.jojczykp.bookstore.testutils.jmx.JmxClient.credentials;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:spring/applicationContext/jmx-context.xml",
		"classpath:spring/jmx-test-context.xml"
})
public class ConfigMBeanComponentTest {

	private static final String JMX_SERVICE_URL_PATTERN =
			"service:jmx:rmi://localhost/jndi/rmi://localhost:%d/%s";
	private static final String JMX_OBJECT_NAME = "custom.application.mbeans:name=ConfigMBean";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	@Autowired private AuthenticationManager authenticationManager;
	@Value("${jmx.port}") private int jmxPort;
	@Value("${jmx.connector.name}") private String jmxConnectorName;

	private JmxClient jmxClient;

	@Before
	public void givenJmxConnection() {
		givenAuthenticationMock();
		establishJmxConnection();
	}

	private void givenAuthenticationMock() {
		reset(authenticationManager);
		given(authenticationManager.authenticate(any(Authentication.class))).willAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				Authentication arg = (Authentication) invocation.getArguments()[0];
				return new UsernamePasswordAuthenticationToken(
						arg.getPrincipal(), arg.getCredentials(), newHashSet(new SimpleGrantedAuthority("ROLE_JMX")));
			}
		});
	}

	private void establishJmxConnection() {
		String jmxServiceUrl = String.format(JMX_SERVICE_URL_PATTERN, jmxPort, jmxConnectorName);
		jmxClient = new JmxClient(jmxServiceUrl, credentials(USERNAME, PASSWORD), JMX_OBJECT_NAME);
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
