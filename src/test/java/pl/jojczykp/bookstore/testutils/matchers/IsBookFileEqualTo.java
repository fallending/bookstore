package pl.jojczykp.bookstore.testutils.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import pl.jojczykp.bookstore.entities.BookFile;

import java.util.Arrays;

import static pl.jojczykp.bookstore.utils.BlobUtils.blobBytes;

public final class IsBookFileEqualTo extends TypeSafeMatcher<BookFile> {

	private BookFile bookFile;

	@Factory
	public static Matcher<BookFile> isBookFileEqualTo(BookFile bookFile) {
		return new IsBookFileEqualTo(bookFile);
	}

	private IsBookFileEqualTo(BookFile bookFile) {
		this.bookFile = bookFile;
	}

	@Override
	protected boolean matchesSafely(BookFile item) {
		return bookFile.getId() == item.getId()
			&& bookFile.getFileType().equals(item.getFileType())
			&& bookFile.getContentType().equals(item.getContentType())
			&& Arrays.equals(blobBytes(bookFile.getContent()), blobBytes(item.getContent()));
	}

	@Override
	public void describeTo(Description description) {
		describe(bookFile, description);
	}

	@Override
	protected void describeMismatchSafely(BookFile item, Description mismatchDescription) {
		describe(item, mismatchDescription);
	}

	private void describe(BookFile bookFile, Description description) {
		description.appendText(bookFile.getClass().getSimpleName());
		description.appendText("{id=" + bookFile.getId());
		description.appendText(", fileType=" + bookFile.getFileType());
		description.appendText(", contentType=" + bookFile.getContentType());
		description.appendText(", contentLength=" + bookFile.getContentLength());
		description.appendText(", content=" + Arrays.toString(blobBytes(bookFile.getContent())));
		description.appendText("}");
	}

}
