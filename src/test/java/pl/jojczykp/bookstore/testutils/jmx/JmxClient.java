package pl.jojczykp.bookstore.testutils.jmx;


import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import static com.google.common.collect.ImmutableMap.of;
import static javax.management.remote.JMXConnector.CREDENTIALS;

public class JmxClient {

	private MBeanServerConnection mBeanServerConnection;
	private ObjectName objectName;

	public static String[] credentials(String username, String password) {
		return new String[] {username, password};
	}

	public JmxClient(String jmxServiceUrl, String[] credentials, String jmxObjectName) {
		try {
			MBeanServerConnectionFactoryBean factory = new MBeanServerConnectionFactoryBean();
			factory.setServiceUrl(jmxServiceUrl);
			factory.setEnvironmentMap(of(CREDENTIALS, credentials));
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
