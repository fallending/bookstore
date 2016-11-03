package pl.jojczykp.bookstore.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import pl.jojczykp.bookstore.entities.Book;

public final class IsBookEqualTo extends TypeSafeMatcher<Book> {

	private Book book;

	@Factory
	public static Matcher<Book> isBookEqualTo(Book book) {
		return new IsBookEqualTo(book);
	}

	private IsBookEqualTo(Book book) {
		this.book = book;
	}

	@Override
	protected boolean matchesSafely(Book item) {
		return book.getId() == item.getId()
			&& book.getVersion() == item.getVersion()
			&& book.getTitle().equals(item.getTitle())
			&& (bookFileIdOf(book).equals(bookFileIdOf(item)));
	}

	@Override
	public void describeTo(Description description) {
		describe(book, description);
	}

	@Override
	protected void describeMismatchSafely(Book item, Description mismatchDescription) {
		describe(item, mismatchDescription);
	}

	private void describe(Book book, Description description) {
		description.appendText(book.getClass().getSimpleName());
		description.appendText("{id=" + book.getId());
		description.appendText(", version=" + book.getVersion());
		description.appendText(", title='" + book.getTitle() + "'");
		description.appendText(", bookFileId=" + bookFileIdOf(book));
		description.appendText("}");
	}

	private String bookFileIdOf(Book book) {
		return (book.getBookFile() == null)
					? "<null>"
					: Integer.toString(book.getBookFile().getId());
	}

}
