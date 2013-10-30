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
		ScrollParams scrollParams = new ScrollParams(offset, size, TOTAL_COUNT);

		testee.limit(scrollParams);

		assertThat(scrollParams.getOffset(), equalTo(offset));
		assertThat(scrollParams.getSize(), equalTo(size));
	}

	@Test
	public void shouldLimitSizeWhenAboveRange() {
		final int offset = 15;
		ScrollParams scrollParams = new ScrollParams(offset, TOTAL_COUNT + 2, TOTAL_COUNT);

		testee.limit(scrollParams);

		assertThat(scrollParams.getOffset(), equalTo(offset));
		assertThat(scrollParams.getSize(), equalTo(TOTAL_COUNT - offset));
	}

	@Test
	public void shouldLimitOffsetWhenBelowRange() {
		final int offset = -2;
		final int size = 10;
		ScrollParams scrollParams = new ScrollParams(offset, size, TOTAL_COUNT);

		testee.limit(scrollParams);

		assertThat(scrollParams.getOffset(), equalTo(0));
		assertThat(scrollParams.getSize(), equalTo(size + offset));
	}

	@Test
	public void shouldLimitOffsetAndSize() {
		ScrollParams scrollParams = new ScrollParams(-2, TOTAL_COUNT + 1, TOTAL_COUNT);

		testee.limit(scrollParams);

		assertThat(scrollParams.getOffset(), equalTo(0));
		assertThat(scrollParams.getSize(), equalTo(TOTAL_COUNT));
	}

	@Test
	public void shouldReturnEmptyResultWhenSizeIsNegative() {
		final int offset = 7;
		ScrollParams scrollParams = new ScrollParams(offset, -3, TOTAL_COUNT);

		testee.limit(scrollParams);

		assertThat(scrollParams.getOffset(), equalTo(offset));
		assertThat(scrollParams.getSize(), equalTo(0));
	}

}
