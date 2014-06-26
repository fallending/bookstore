/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

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
