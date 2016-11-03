package pl.jojczykp.bookstore.assemblers;

import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.books.UpdateBookCommand;
import pl.jojczykp.bookstore.entities.Book;

@Service
public class UpdateBookAssembler {

	public Book toDomain(UpdateBookCommand command) {
		Book domain = new Book();
		domain.setId(command.getId());
		domain.setVersion(command.getVersion());
		domain.setTitle(command.getTitle());

		return domain;
	}

}
