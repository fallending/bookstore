package pl.jojczykp.bookstore.assembler;

import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.command.BookCommand;
import pl.jojczykp.bookstore.domain.Book;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookAssembler {

	public List<BookCommand> toCommands(List<Book> domains) {
		List<BookCommand> commands = new ArrayList<>(domains.size());
		for (Book domain : domains) {
			commands.add(toCommand(domain));
		}

		return commands;
	}

	private BookCommand toCommand(Book domain) {
		BookCommand command = new BookCommand();
		command.setChecked(false);
		command.setId(domain.getId());
		command.setVersion(domain.getVersion());
		command.setTitle(domain.getTitle());

		return command;
	}

	public Book toDomain(BookCommand command) {
		Book domain = new Book();
		domain.setId(command.getId());
		domain.setVersion(command.getVersion());
		domain.setTitle(command.getTitle());

		return domain;
	}
}
