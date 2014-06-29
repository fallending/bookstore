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
