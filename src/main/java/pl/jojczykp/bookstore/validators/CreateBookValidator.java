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
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Service
public class CreateBookValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (CreateBookCommand.class.equals(clazz));
	}

	@Override
	public void validate(Object object, Errors errors) {
		rejectIfEmptyOrWhitespace(errors, "title", "title.empty", "Creating with empty title is not allowed.");
		rejectIfNoFileProvided(errors, "file", "file.empty", "Creating with no file is not allowed.");
	}

	private static void rejectIfNoFileProvided(Errors errors, String field, String errorCode, String defaultMessage) {
		MultipartFile value = (MultipartFile) errors.getFieldValue(field);
		if (value == null || !StringUtils.hasText(value.getOriginalFilename())) {
			errors.rejectValue(field, errorCode, defaultMessage);
		}
	}
}
