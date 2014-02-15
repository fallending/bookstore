package pl.jojczykp.bookstore.utils;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class PageParamsLimiterTest {

	private static final int TOTAL_COUNT = 23;

	private PageParamsLimiter testee;

	@Before
	public void setUpTestee() {
		testee = new PageParamsLimiter();
	}

	@Test
	public void shouldPropagateGivenParametersWhenNoLimitationNeeded() {
		final int offset = 5;
		final int size = 11;

		PageParams limited = testee.limit(new PageParams(offset, size), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(offset));
		assertThat(limited.getSize(), equalTo(size));
	}

	@Test
	public void shouldLimitSizeWhenAboveRange() {
		final int offset = 15;
		final int sizeAboveRange = TOTAL_COUNT + 2;

		PageParams limited = testee.limit(new PageParams(offset, sizeAboveRange), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(offset));
		assertThat(limited.getSize(), equalTo(TOTAL_COUNT - offset));
	}

	@Test
	public void shouldLimitOffsetWhenAboveRange() {
		final int offsetAboveRange = TOTAL_COUNT + 2;
		final int size = 2;

		PageParams limited = testee.limit(new PageParams(offsetAboveRange, size), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(TOTAL_COUNT));
		assertThat(limited.getSize(), equalTo(0));
	}

	@Test
	public void shouldLimitOffsetWhenBelowRange() {
		final int offsetBelowRange = -2;
		final int size = 10;

		PageParams limited = testee.limit(new PageParams(offsetBelowRange, size), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(0));
		assertThat(limited.getSize(), equalTo(size));
	}

	@Test
	public void shouldLimitOffsetAndSize() {
		final int offsetBelowRange = -2;
		final int sizeOverRange = TOTAL_COUNT + 1;

		PageParams limited = testee.limit(new PageParams(offsetBelowRange, sizeOverRange), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(0));
		assertThat(limited.getSize(), equalTo(TOTAL_COUNT));
	}

	@Test
	public void shouldReturnEmptyResultWhenSizeIsNegative() {
		final int offset = 7;
		final int negativeSize = -3;

		PageParams limited = testee.limit(new PageParams(offset, negativeSize), TOTAL_COUNT);

		assertThat(limited.getOffset(), equalTo(offset));
		assertThat(limited.getSize(), equalTo(0));
	}

}
