package pl.jojczykp.bookstore.utils;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ScrollParamsLimiterTest {

	private static int TOTAL_COUNT = 23;

	private ScrollParamsLimiter testee;

	@Before
	public void setUpTestee() {
		testee = new ScrollParamsLimiter();
	}

	@Test
	public void shouldPropagateGivenParametersWhenNoLimitationNeeded() {
		final int offset = 5;
		final int size = 11;

		ScrollParams limited = testee.limit(new ScrollParams(offset, size), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(offset));
		assertThat(limited.getSize(), equalTo(size));
	}

	@Test
	public void shouldLimitSizeWhenAboveRange() {
		final int offset = 15;

		ScrollParams limited = testee.limit(new ScrollParams(offset, TOTAL_COUNT + 2), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(offset));
		assertThat(limited.getSize(), equalTo(TOTAL_COUNT - offset));
	}

	@Test
	public void shouldLimitOffsetWhenBelowRange() {
		final int offset = -2;
		final int size = 10;

		ScrollParams limited = testee.limit(new ScrollParams(offset, size), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(0));
		assertThat(limited.getSize(), equalTo(size));
	}

	@Test
	public void shouldLimitOffsetAndSize() {
		ScrollParams limited = testee.limit(new ScrollParams(-2, TOTAL_COUNT + 1), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(0));
		assertThat(limited.getSize(), equalTo(TOTAL_COUNT));
	}

	@Test
	public void shouldReturnEmptyResultWhenSizeIsNegative() {
		final int offset = 7;

		ScrollParams limited = testee.limit(new ScrollParams(offset, -3), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(offset));
		assertThat(limited.getSize(), equalTo(0));
	}

}
