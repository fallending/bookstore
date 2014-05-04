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

package pl.jojczykp.bookstore.validators;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.jojczykp.bookstore.commands.books.ChangePagerCommand;

@Service
public class BooksSetPageSizeValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (ChangePagerCommand.class.equals(clazz));
	}

	@Override
	public void validate(Object object, Errors errors) {
		ChangePagerCommand command = (ChangePagerCommand) object;
		int pageSize = command.getPager().getPageSize();

		if (pageSize <= 0) {
			errors.rejectValue("pager.pageSize", "pager.pageSize.notPositive",
					"Negative or zero page size is not allowed. Defaults used.");
		}
	}
}
