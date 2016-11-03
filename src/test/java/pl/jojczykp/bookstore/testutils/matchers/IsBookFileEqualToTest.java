package pl.jojczykp.bookstore.testutils.matchers;

import org.junit.Before;
import org.junit.Test;
import pl.jojczykp.bookstore.entities.BookFile;
import pl.jojczykp.bookstore.utils.BlobUtils;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;
import static pl.jojczykp.bookstore.testutils.matchers.IsBookFileEqualTo.isBookFileEqualTo;


public class IsBookFileEqualToTest {

	private static final byte[] CONTENT = new byte[] {1, 2, 3, 4};
	private static final byte[] OTHER_CONTENT = new byte[] {5, 6, 7, 8};

	private BookFile bookFile1;
	private BookFile bookFile2;

	@Before
	public void setupData() {
		bookFile1 = aBookFile().withId(1).withFileType("FType").withContentType("CType").withContent(CONTENT).build();
		bookFile2 = aBookFile().withId(1).withFileType("FType").withContentType("CType").withContent(CONTENT).build();
	}

	@Test
	public void shouldMatch() {
		assertThat(bookFile1, isBookFileEqualTo(bookFile2));
	}

	@Test
	public void shouldNotMatchWhenDifferentId() {
		bookFile2.setId(0);
		assertThat(bookFile1, not(isBookFileEqualTo(bookFile2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentFileType() {
		bookFile2.setFileType("OtherFileType");
		assertThat(bookFile1, not(isBookFileEqualTo(bookFile2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentContentType() {
		bookFile2.setFileType("OtherContentType");
		assertThat(bookFile1, not(isBookFileEqualTo(bookFile2)));
	}

	@Test
	public void shouldNotMatchWhenDifferentContent() {
		bookFile2.setContent(BlobUtils.aSerialBlobWith(OTHER_CONTENT));
		assertThat(bookFile1, not(isBookFileEqualTo(bookFile2)));
	}

}
