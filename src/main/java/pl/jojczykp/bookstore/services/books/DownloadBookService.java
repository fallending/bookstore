package pl.jojczykp.bookstore.services.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.books.DownloadBookCommand;
import pl.jojczykp.bookstore.controllers.errors.ResourceNotFoundException;
import pl.jojczykp.bookstore.entities.Book;
import pl.jojczykp.bookstore.repositories.BooksRepository;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

@Service
public class DownloadBookService {

	@Autowired private BooksRepository booksRepository;

	public Book download(DownloadBookCommand downloadBookCommand) {
		try {
			Book book = booksRepository.find(parseInt(downloadBookCommand.getId()));
			verifyBookFound(downloadBookCommand.getId(), book);
			return book;
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException(exceptionMessageFor(downloadBookCommand.getId()), e);
		}
	}

	private void verifyBookFound(String id, Book book) {
		if (book == null) {
			throw new ResourceNotFoundException(exceptionMessageFor(id));
		}
	}

	private String exceptionMessageFor(String id) {
		return format("Content of book with id '%s' not found.", id);
	}

}
