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
