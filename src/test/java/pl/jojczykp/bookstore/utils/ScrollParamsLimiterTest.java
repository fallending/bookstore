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

		ScrollParamsLimits limits = testee.computeLimitsFor(offset, size, TOTAL_COUNT);

		assertThat(limits.getOffset(), equalTo(offset));
		assertThat(limits.getSize(), equalTo(size));
	}

	@Test
	public void shouldLimitSizeWhenAboveRange() {
		final int offset = 15;

		ScrollParamsLimits limits = testee.computeLimitsFor(offset, TOTAL_COUNT + 2, TOTAL_COUNT);

		assertThat(limits.getOffset(), equalTo(offset));
		assertThat(limits.getSize(), equalTo(TOTAL_COUNT - offset));
	}

	@Test
	public void shouldLimitOffsetWhenBelowRange() {
		final int offset = -2;
		final int size = 10;

		ScrollParamsLimits limits = testee.computeLimitsFor(offset, size, TOTAL_COUNT);

		assertThat(limits.getOffset(), equalTo(0));
		assertThat(limits.getSize(), equalTo(size + offset));
	}

	@Test
	public void shouldLimitOffsetAndSize() {
		ScrollParamsLimits limits = testee.computeLimitsFor(-2, TOTAL_COUNT + 1, TOTAL_COUNT);

		assertThat(limits.getOffset(), equalTo(0));
		assertThat(limits.getSize(), equalTo(TOTAL_COUNT));
	}

	@Test
	public void shouldReturnEmptyResultWhenSizeIsNegative() {
		final int offset = 7;

		ScrollParamsLimits limits = testee.computeLimitsFor(offset, -3, TOTAL_COUNT);

		assertThat(limits.getOffset(), equalTo(offset));
		assertThat(limits.getSize(), equalTo(0));
	}

}
