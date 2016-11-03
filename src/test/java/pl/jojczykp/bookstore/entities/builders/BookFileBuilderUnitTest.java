package pl.jojczykp.bookstore.entities.builders;

import org.junit.Test;
import pl.jojczykp.bookstore.entities.BookFile;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static pl.jojczykp.bookstore.entities.builders.BookFileBuilder.aBookFile;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;

public class BookFileBuilderUnitTest {

	private static final int ID = 7;
	private static final String FILE_TYPE = "aFileType";
	private static final String CONTENT_TYPE = "some-content/type";
	private static final byte[] CONTENT = {7, 5, 3, 0, 1};

	@Test
	public void shouldBuildWithId() {
		BookFile bookFile = aBookFile().withId(ID).build();

		assertThat(bookFile.getId(), is(ID));
	}

	@Test
	public void shouldBuildWithFileType() {
		BookFile bookFile = aBookFile().withFileType(FILE_TYPE).build();

		assertThat(bookFile.getFileType(), is(equalTo(FILE_TYPE)));
	}

	@Test
	public void shouldBuildWithContentType() {
		BookFile bookFile = aBookFile().withContentType(CONTENT_TYPE).build();

		assertThat(bookFile.getContentType(), is(equalTo(CONTENT_TYPE)));
	}

	@Test
	public void shouldBuildWithContentAndContentLength() {
		BookFile bookFile = aBookFile().withContent(CONTENT).build();

		assertThat(blobBytes(bookFile.getContent()), is(equalTo(CONTENT)));
		assertThat(bookFile.getContentLength(), is(CONTENT.length));
	}

}
