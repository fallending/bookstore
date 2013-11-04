package pl.jojczykp.bookstore.utils;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ScrollParamsTest {

	private static final int ANY_OFFSET = 7;
	private static final int ANY_SIZE = 12;

	@Test
	public void shouldHaveDefaultConstructor() {
		new ScrollParams();
	}

	@Test
	public void shouldCreateInstanceWithGivenOffset() {
		final int givenOffset = 12;

		ScrollParams testee = new ScrollParams(givenOffset, ANY_SIZE);

		assertThat(testee.getOffset(), equalTo(givenOffset));
	}

	@Test
	public void shouldCreateInstanceWithGivenSize() {
		final int givenSize = 6;

		ScrollParams testee = new ScrollParams(ANY_OFFSET, givenSize);

		assertThat(testee.getSize(), equalTo(givenSize));
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

}
