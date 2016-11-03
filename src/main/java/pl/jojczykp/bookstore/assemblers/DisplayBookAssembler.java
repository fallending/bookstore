package pl.jojczykp.bookstore.assemblers;

import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.books.DisplayBookCommand;
import pl.jojczykp.bookstore.entities.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Service
public class DisplayBookAssembler {

	private static final String DEFAULT_ICON_NAME = "unknown";

	private static final Set<String> fileTypesHavingIcons = newHashSet(
			"7z", "bin", "bmp", "doc", "eps", "gz", "htm", "html", "jpeg", "jpg", "pdf", "pps",
			"ps", "psd", "rar", "rtf", "tgz", "tif", "txt", "wps", "xls", "zip");

	public List<DisplayBookCommand> toCommands(List<Book> domains) {
		List<DisplayBookCommand> commands = new ArrayList<>(domains.size());
		for (Book domain : domains) {
			commands.add(toCommand(domain));
		}

		return commands;
	}

	private DisplayBookCommand toCommand(Book domain) {
		DisplayBookCommand command = new DisplayBookCommand();
		command.setId(domain.getId());
		command.setVersion(domain.getVersion());
		command.setTitle(domain.getTitle());
		command.setIconName(iconNameFor(domain.getBookFile().getFileType()));

		return command;
	}

	private String iconNameFor(String fileType) {
		return fileTypesHavingIcons.contains(fileType) ? fileType : DEFAULT_ICON_NAME;
	}

}
