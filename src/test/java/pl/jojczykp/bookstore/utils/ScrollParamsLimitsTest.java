package pl.jojczykp.bookstore.utils;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ScrollParamsLimitsTest {
	private static final int OFFSET = 12;
	private static final int SIZE = 27;

	private ScrollParamsLimits testee = new ScrollParamsLimits(OFFSET, SIZE);

	@Test
	public void shouldCreateInstanceWithGivenOffset() {
		assertThat(testee.getOffset(), equalTo(OFFSET));
	}

	@Test
	public void shouldCreateInstanceWithGivenSize() {
		assertThat(testee.getSize(), equalTo(SIZE));
	}
}
