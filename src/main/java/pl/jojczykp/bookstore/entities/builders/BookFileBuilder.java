package pl.jojczykp.bookstore.entities.builders;

import pl.jojczykp.bookstore.entities.BookFile;

import static pl.jojczykp.bookstore.utils.BlobUtils.aSerialBlobWith;

public class BookFileBuilder {

	private BookFile template = new BookFile();

	public static BookFileBuilder aBookFile() {
		return new BookFileBuilder();
	}

	public BookFileBuilder withId(int id) {
		template.setId(id);
		return this;
	}

	public BookFileBuilder withFileType(String fileType) {
		template.setFileType(fileType);
		return this;
	}

	public BookFileBuilder withContentType(String contentType) {
		template.setContentType(contentType);
		return this;
	}

	public BookFileBuilder withContent(byte[] content) {
		template.setContentLength(content.length);
		template.setContent(aSerialBlobWith(content));
		return this;
	}

	public BookFile build() {
		BookFile constructed = new BookFile();
		constructed.setId(template.getId());
		constructed.setFileType(template.getFileType());
		constructed.setContentType(template.getContentType());
		constructed.setContentLength(template.getContentLength());
		constructed.setContent(template.getContent());

		return constructed;
	}

}
