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

package pl.jojczykp.bookstore.commands.common;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class MessagesCommand {

	private List<String> infos;
	private List<String> warns;
	private List<String> errors;

	public MessagesCommand() {
		infos = new ArrayList<>();
		warns = new ArrayList<>();
		errors = new ArrayList<>();
	}

	public void addInfos(String... messages) {
		infos.addAll(asList(messages));
	}

	public List<String> getInfos() {
		return infos;
	}

	public void addWarns(String... messages) {
		warns.addAll(asList(messages));
	}

	public List<String> getWarns() {
		return warns;
	}

	public void addErrors(String... messages) {
		errors.addAll(asList(messages));
	}

	public List<String> getErrors() {
		return errors;
	}

}
