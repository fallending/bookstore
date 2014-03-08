package pl.jojczykp.bookstore.testutils.matchers;

import org.junit.Test;

import javax.persistence.Id;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.testutils.matchers.SameNoIdPropertyValuesAs.sameNoIdPropertyValuesAs;

public class SameNoIdPropertyValuesAsTest {

	public static final String VALUE_1 = "value1";
	public static final String VALUE_2 = "value2";

	public class Bean {

		@Id
		private int id;
		private String property;

		public Bean(int id, String property) {
			this.id = id;
			this.property = property;
		}

		public int getId() {
			return id;
		}

		public String getProperty() {
			return property;
		}
	}

	@Test
	public void shouldMatchWhenIdsAndPropertiesAreEqual() {
		Bean bean1 = new Bean(1, VALUE_1);
		Bean bean2 = new Bean(1, VALUE_1);
		assertThat(bean1, sameNoIdPropertyValuesAs(bean2));
	}

	@Test
	public void shouldMatchWhenOnlyPropertiesAreEqual() {
		Bean bean1 = new Bean(1, VALUE_1);
		Bean bean2 = new Bean(2, VALUE_1);
		assertThat(bean1, sameNoIdPropertyValuesAs(bean2));
	}

	@Test
	public void shouldNotMatchWhenOnlyIdsAreEqual() {
		Bean bean1 = new Bean(1, VALUE_1);
		Bean bean2 = new Bean(1, VALUE_2);
		assertThat(bean1, not(sameNoIdPropertyValuesAs(bean2)));
	}

	@Test
	public void shouldNotMatchWhenNeitherIdsNorPropertiesAreEqual() {
		Bean bean1 = new Bean(1, VALUE_1);
		Bean bean2 = new Bean(2, VALUE_2);
		assertThat(bean1, not(sameNoIdPropertyValuesAs(bean2)));
	}

}
