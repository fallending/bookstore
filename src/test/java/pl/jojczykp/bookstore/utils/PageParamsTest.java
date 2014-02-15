package pl.jojczykp.bookstore.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class PageParamsTest {

	private static final int ANY_OFFSET = 7;
	private static final int ANY_SIZE = 12;

	@Test
	public void shouldHaveDefaultConstructor() {
		new PageParams();
	}

	@Test
	public void shouldCreateInstanceWithGivenOffset() {
		final int givenOffset = 12;

		PageParams testee = new PageParams(givenOffset, ANY_SIZE);

		assertThat(testee.getOffset(), equalTo(givenOffset));
	}

	@Test
	public void shouldCreateInstanceWithGivenSize() {
		final int givenSize = 6;

		PageParams testee = new PageParams(ANY_OFFSET, givenSize);

		assertThat(testee.getSize(), equalTo(givenSize));
	}

	@Test
	public void shouldSetOffset() {
		final int givenOffset = 7;
		PageParams testee = new PageParams();

		testee.setOffset(givenOffset);

		assertThat(testee.getOffset(), equalTo(givenOffset));
	}

	@Test
	public void shouldSetSize() {
		final int givenSize = 18;
		PageParams testee = new PageParams();

		testee.setSize(givenSize);

		assertThat(testee.getSize(), equalTo(givenSize));
	}

	@Test
	public void shouldHaveToStringWithDetailsForDiagnostic() {
		final int givenOffset = 12443;
		final int givenSize = 11876;
		PageParams testee = new PageParams(givenOffset, givenSize);

		String toStringResult = testee.toString();

		assertThat(toStringResult, containsString("offset=" + givenOffset));
		assertThat(toStringResult, containsString("size=" + givenSize));
	}
}
