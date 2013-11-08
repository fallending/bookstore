package pl.jojczykp.bookstore.utils.matchers;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.utils.matchers.HasBeanProperty.hasBeanProperty;

public class HasBeanPropertyTest {

	private class Bean {

		private String property;
		private Bean bean;

		public Bean(String property, Bean bean) {
			this.property = property;
			this.bean = bean;
		}

		public String getProperty() {
			return property;
		}

		public Bean getBean() {
			return bean;
		}
	}

	private Bean sampleBean;

	@Before
	public void setupSampleBean() {
		sampleBean = new Bean("propertyVal1", new Bean("propertyVal2", new Bean("propertyVal3", new Bean(null, null))));
	}

	@Test
	public void shouldMatchNullValue() {
		assertThat(sampleBean, hasBeanProperty("bean.bean.bean.property", nullValue()));
	}

	@Test
	public void shouldMatchIfMatches() {
		assertThat(sampleBean, hasBeanProperty("bean.bean.property", equalTo("propertyVal3")));
	}

	@Test
	public void shouldFailIfValueDoesNotMatch() {
		assertThat(sampleBean, not(hasBeanProperty("bean.bean.property", equalTo("notExistingProperty"))));
	}

	@Test
	public void shouldFailIfPropertyNotFound() {
		Bean bean = new Bean("propertyVal1", new Bean("propertyVal2", null));
		assertThat(bean, not(hasBeanProperty("bean.notExistingProperty", equalTo("propertyVal2"))));
	}

}
