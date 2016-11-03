package pl.jojczykp.bookstore.services.books;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.books.DeleteBooksCommand;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;
import pl.jojczykp.bookstore.commands.common.MessagesCommand;
import pl.jojczykp.bookstore.repositories.BooksRepository;

@Service
public class DeleteBookService {

	@Autowired private BooksRepository booksRepository;

	public DisplayBooksCommand delete(DeleteBooksCommand deleteBooksCommand) {
		DisplayBooksCommand displayBooksCommand = new DisplayBooksCommand();
		displayBooksCommand.setPager(deleteBooksCommand.getPager());

		for (Integer id : deleteBooksCommand.getIds()) {
			deleteBookFromRepository(id, displayBooksCommand.getMessages());
		}

		return displayBooksCommand;
	}

	private void deleteBookFromRepository(int bookId, MessagesCommand messagesContainer) {
		try {
			booksRepository.delete(bookId);
			messagesContainer.addInfos("Object deleted.");
		} catch (ObjectNotFoundException ex) {
			messagesContainer.addWarns("Object already deleted.");
		}
	}

}
