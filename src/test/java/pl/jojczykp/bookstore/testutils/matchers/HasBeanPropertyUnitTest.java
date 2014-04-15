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

package pl.jojczykp.bookstore.testutils.matchers;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

public class HasBeanPropertyUnitTest {

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
