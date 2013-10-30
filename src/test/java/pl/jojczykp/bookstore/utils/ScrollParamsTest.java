package pl.jojczykp.bookstore.utils;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ScrollParamsTest {

	private static final int ANY_OFFSET = 7;
	private static final int ANY_SIZE = 12;
	private static final int ANY_TOTAL_COUNT = 123;

	@Test
	public void shouldHaveDefaultConstructorForSpring() {
		new ScrollParams();
	}

	@Test
	public void shouldCreateInstanceWithGivenOffset() {
		final int givenOffset = 12;

		ScrollParams testee = new ScrollParams(givenOffset, ANY_SIZE, ANY_TOTAL_COUNT);

		assertThat(testee.getOffset(), equalTo(givenOffset));
	}

	@Test
	public void shouldCreateInstanceWithGivenSize() {
		final int givenSize = 6;

		ScrollParams testee = new ScrollParams(ANY_OFFSET, givenSize, ANY_TOTAL_COUNT);

		assertThat(testee.getSize(), equalTo(givenSize));
	}

	@Test
	public void shouldCreateInstanceWithGivenTotalCount() {
		final int givenTotalCount = 84;

		ScrollParams testee = new ScrollParams(ANY_OFFSET, ANY_SIZE, givenTotalCount);

		assertThat(testee.getTotalCount(), equalTo(givenTotalCount));
	}

	@Test
	public void shouldSetOffset() {
		final int givenOffset = 7;
		ScrollParams testee = new ScrollParams();

		testee.setOffset(givenOffset);

		assertThat(testee.getOffset(), equalTo(givenOffset));
	}

	@Test
	public void shouldSetSize() {
		final int givenSize = 18;
		ScrollParams testee = new ScrollParams();

		testee.setSize(givenSize);

		assertThat(testee.getSize(), equalTo(givenSize));
	}

	@Test
	public void shouldSetTotalCount() {
		final int givenTotalCount = 91;
		ScrollParams testee = new ScrollParams();

		testee.setTotalCount(givenTotalCount);

		assertThat(testee.getTotalCount(), equalTo(givenTotalCount));
	}
}
