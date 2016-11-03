package pl.jojczykp.bookstore.controllers.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jojczykp.bookstore.commands.books.DownloadBookCommand;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.services.books.DownloadBookService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.io.IOUtils.copy;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.bookstore.consts.BooksConsts.DOWNLOAD_BOOK_COMMAND;
import static pl.jojczykp.bookstore.consts.BooksConsts.URL_ACTION_DOWNLOAD;
import static pl.jojczykp.bookstore.utils.BlobUtils.blobInputStream;

@Controller
public class DownloadBookController {

	@Autowired private DownloadBookService downloadBookService;

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = URL_ACTION_DOWNLOAD, method = GET)
	@Transactional
	public void download(
			@ModelAttribute(DOWNLOAD_BOOK_COMMAND) DownloadBookCommand downloadBookCommand,
			HttpServletResponse response) throws IOException
	{
		setResponse(response, downloadBookService.download(downloadBookCommand));
	}

	private void setResponse(HttpServletResponse response, Book book) throws IOException {
		setResponseHeaders(response, book);
		setResponseBody(response, book);
	}

	private void setResponseHeaders(HttpServletResponse response, Book book) {
		response.setContentType(book.getBookFile().getContentType());
		response.setContentLength(book.getBookFile().getContentLength());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameFor(book) + "\"");
	}

	private String fileNameFor(Book book) {
		return book.getTitle() + "." + book.getBookFile().getFileType();
	}

	private void setResponseBody(HttpServletResponse response, Book book) throws IOException {
		copy(blobInputStream(book.getBookFile().getContent()), response.getOutputStream());
	}

}
