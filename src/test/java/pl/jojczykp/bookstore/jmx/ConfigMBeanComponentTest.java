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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.jojczykp.bookstore.entities.Book;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:spring/applicationContext/jmx-context.xml",
		"classpath:spring/jmx-test-context.xml"
})
public class ConfigMBeanComponentTest {

	private static final String JMX_SERVICE_URL_PATTERN =
			"service:jmx:rmi://localhost/jndi/rmi://localhost:%d/bookstoreJmxConnector";
	private static final String JMX_OBJECT_NAME = "custom.application.mbeans:name=ConfigMBean";

	private MBeanServerConnection mBeanServerConnection;
	private ObjectName objectName;

	@Value("${jmx.port}") private int jmxPort;

	@Captor private ArgumentCaptor<Book> bookCaptor;

	@Before
	public void givenJmxConnection() throws Exception {
		MockitoAnnotations.initMocks(this);
		connect();
		createObjectName();
	}

	private void connect() throws IOException {
		MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
		bean.setServiceUrl(String.format(JMX_SERVICE_URL_PATTERN, jmxPort));
		bean.afterPropertiesSet();
		mBeanServerConnection = bean.getObject();
	}

	private void createObjectName() throws MalformedObjectNameException {
		objectName = new ObjectName(JMX_OBJECT_NAME);
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
		jmxInvoke("enableRequestMappingLogging");
	}

	private Object disableRequestMappingLogging() {
		return jmxInvoke("disableRequestMappingLogging");
	}

	private boolean isRequestMappingLoggingEnabled() {
		return (boolean) jmxInvoke("isRequestMappingLoggingEnabled");
	}

	private Object jmxInvoke(String name, Object... params) {
		String[] signature = signatureFor(params);
		try {
			return mBeanServerConnection.invoke(objectName, name, params, signature);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String[] signatureFor(Object[] params) {
		String[] signature = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			signature[i] = toClassString(params[i]);
		}

		return signature;
	}

	private String toClassString(Object param) {
		if (param.getClass() == Integer.class) {
			return int.class.toString();
		} else {
			return String.class.getName();
		}
	}

}
