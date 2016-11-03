package pl.jojczykp.bookstore.jmx;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConfigMBeanUnitTest {

	private ConfigMBean testee;

	@Before
	public void setupTestee() {
		testee = new ConfigMBean();
	}

	@Test
	public void shouldEnable() {
		testee.enableRequestMappingLogging();

		assertThat(testee.isRequestMappingLoggingEnabled(), is(true));
	}

	@Test
	public void shouldDisable() {
		testee.disableRequestMappingLogging();

		assertThat(testee.isRequestMappingLoggingEnabled(), is(false));
	}

}
