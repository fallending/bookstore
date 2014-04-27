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

package pl.jojczykp.bookstore.testutils.jmx;


import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class JmxClient {

	private MBeanServerConnection mBeanServerConnection;
	private ObjectName objectName;

	public JmxClient(String jmxServiceUrl, String jmxObjectName) {
		try {
			MBeanServerConnectionFactoryBean factory = new MBeanServerConnectionFactoryBean();
			factory.setServiceUrl(jmxServiceUrl);
			factory.afterPropertiesSet();
			mBeanServerConnection = factory.getObject();
			objectName = new ObjectName(jmxObjectName);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Object invoke(String name, Object... params) {
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
