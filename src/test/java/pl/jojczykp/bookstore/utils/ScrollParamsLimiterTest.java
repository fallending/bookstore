package pl.jojczykp.bookstore.utils;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ScrollParamsLimiterTest {

	private static final int TOTAL_COUNT = 23;

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
		final int sizeAboveRange = TOTAL_COUNT + 2;

		ScrollParams limited = testee.limit(new ScrollParams(offset, sizeAboveRange), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(offset));
		assertThat(limited.getSize(), equalTo(TOTAL_COUNT - offset));
	}

	@Test
	public void shouldLimitOffsetWhenAboveRange() {
		final int offsetAboveRange = TOTAL_COUNT + 2;
		final int size = 2;

		ScrollParams limited = testee.limit(new ScrollParams(offsetAboveRange, size), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(TOTAL_COUNT));
		assertThat(limited.getSize(), equalTo(0));
	}

	@Test
	public void shouldLimitOffsetWhenBelowRange() {
		final int offsetBelowRange = -2;
		final int size = 10;

		ScrollParams limited = testee.limit(new ScrollParams(offsetBelowRange, size), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(0));
		assertThat(limited.getSize(), equalTo(size));
	}

	@Test
	public void shouldLimitOffsetAndSize() {
		final int offsetBelowRange = -2;
		final int sizeOverRange = TOTAL_COUNT + 1;

		ScrollParams limited = testee.limit(new ScrollParams(offsetBelowRange, sizeOverRange), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(0));
		assertThat(limited.getSize(), equalTo(TOTAL_COUNT));
	}

	@Test
	public void shouldReturnEmptyResultWhenSizeIsNegative() {
		final int offset = 7;
		final int negativeSize = -3;

		ScrollParams limited = testee.limit(new ScrollParams(offset, negativeSize), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(offset));
		assertThat(limited.getSize(), equalTo(0));
	}

}
