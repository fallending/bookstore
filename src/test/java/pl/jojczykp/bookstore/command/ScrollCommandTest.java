package pl.jojczykp.bookstore.command;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.utils.ScrollParams;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ScrollCommandTest {

	private ScrollCommand testee;

	@Before
	public void setupInstance() {
		testee = new ScrollCommand();
	}

	@Test
	public void shouldBeSetUpByDefaultConstructor() {
		assertThat(testee.getCurrent(), notNullValue());
		assertThat(testee.getLimited(), notNullValue());
		assertThat(testee.getTotalCount(), equalTo(0));
	}

	@Test
	public void shouldSetCurrent() {
		final ScrollParams scrollParams = new ScrollParams();

		testee.setCurrent(scrollParams);

		assertThat(testee.getCurrent(), sameInstance(scrollParams));
	}

	@Test
	public void shouldSetLimited() {
		final ScrollParams scrollParams = new ScrollParams();

		testee.setLimited(scrollParams);

		assertThat(testee.getLimited(), sameInstance(scrollParams));
	}

	@Test
	public void shouldSetTotalCount() {
		final int totalCount = 3;

		testee.setTotalCount(totalCount);

		assertThat(testee.getTotalCount(), equalTo(totalCount));
	}

}
