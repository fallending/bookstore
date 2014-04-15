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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.jojczykp.bookstore.commands;

import java.util.ArrayList;
import java.util.List;

public class BooksCommand {

	private MessagesCommand messages;
	private PagerCommand pager;
	private BookCommand newBook;
	private BookCommand updatedBook;
	private List<BookCommand> books;

	public BooksCommand() {
		messages = new MessagesCommand();
		pager = new PagerCommand();
		newBook = new BookCommand();
		updatedBook = new BookCommand();
		books = new ArrayList<>();
	}

	public MessagesCommand getMessages() {
		return messages;
	}

	public void setMessages(MessagesCommand messages) {
		this.messages = messages;
	}

	public PagerCommand getPager() {
		return pager;
	}

	public void setPager(PagerCommand pager) {
		this.pager = pager;
	}

	public BookCommand getNewBook() {
		return newBook;
	}

	public void setNewBook(BookCommand newBook) {
		this.newBook = newBook;
	}

	public BookCommand getUpdatedBook() {
		return updatedBook;
	}

	public void setUpdatedBook(BookCommand updatedBook) {
		this.updatedBook = updatedBook;
	}

	public List<BookCommand> getBooks() {
		return books;
	}

	public void setBooks(List<BookCommand> books) {
		this.books = books;
	}

}
