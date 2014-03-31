package pl.jojczykp.bookstore.utils;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isPrivate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class SuppressUncheckedUnitTest {

	@Test
	public void shouldSuppressUncheckedReturnSameInstance() {
		List list = new ArrayList();

		List<?> result = SuppressUnchecked.suppressUnchecked(list);

		assertThat(result, is(sameInstance(list)));
	}

	@Test
	public void shouldHavePrivateConstructor() throws Exception {
		Constructor<SuppressUnchecked> constructor = SuppressUnchecked.class.getDeclaredConstructor();
		assertThat(isPrivate(constructor.getModifiers()), is(true));

		constructor.setAccessible(true);
		constructor.newInstance();
	}

}
