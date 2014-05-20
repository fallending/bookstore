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

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import pl.jojczykp.bookstore.commands.books.CreateBookCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CreateBookValidatorUnitTest {

	private static final String SOME_OBJECT_NAME = "someObjectName";
	private static final String SOME_VALID_TITLE = "some valid title";
	private static final String EMPTY_TITLE = "";
	private static final MultipartFile NO_FILE_SELECTED = new MockMultipartFile("file", "", "", new byte[0]);
	private static final MockMultipartFile SOME_VALID_FILE
							= new MockMultipartFile("file", "originalName", "text/plain", new byte[] {1, 2, 3});

	private CreateBookValidator testee;

	@Before
	public void setUpTestee() {
		testee = new CreateBookValidator();
	}

	@Test
	public void shouldSupportCorrectCommandClass() {
		final Class<?> clazz = CreateBookCommand.class;

		boolean supports = testee.supports(clazz);

		assertThat(supports, is(true));
	}

	@Test
	public void shouldNotSupportOtherClass() {
		final Class<?> clazz = Object.class;

		boolean supports = testee.supports(clazz);

		assertThat(supports, is(false));
	}

	@Test
	public void shouldPassWhenValidData() {
		final CreateBookCommand command = aCreateBooksCommandWith(SOME_VALID_TITLE, SOME_VALID_FILE);
		final Errors errors = new BeanPropertyBindingResult(command, SOME_OBJECT_NAME);

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(false));
	}

	@Test
	public void shouldFindErrorsWhenInvalidTitle() {
		final CreateBookCommand command = aCreateBooksCommandWith(EMPTY_TITLE, SOME_VALID_FILE);
		final Errors errors = new BeanPropertyBindingResult(command, SOME_OBJECT_NAME);

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getErrorCount(), is(equalTo(1)));
		assertThatFieldErrorHas(errors.getFieldError("title"),
				"title.empty", "Creating with empty title is not allowed.");
	}

	@Test
	public void shouldFindErrorsWhenNoFileSelected() {
		shouldFindErrorsFor(NO_FILE_SELECTED);
	}

	@Test
	public void shouldFindErrorsWhenNoFileDataPresent() {
		shouldFindErrorsFor(null);
	}

	private void shouldFindErrorsFor(MultipartFile file) {
		final CreateBookCommand command = aCreateBooksCommandWith(SOME_VALID_TITLE, file);
		final Errors errors = new BeanPropertyBindingResult(command, SOME_OBJECT_NAME);

		testee.validate(command, errors);

		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getErrorCount(), is(equalTo(1)));
		assertThatFieldErrorHas(errors.getFieldError("file"),
				"file.empty", "Creating with no file is not allowed.");
	}

	private void assertThatFieldErrorHas(FieldError fieldError, String fieldCode, String defaultMessage) {
		assertThat(fieldError.getCode(), is(equalTo(fieldCode)));
		assertThat(fieldError.getDefaultMessage(), is(equalTo(defaultMessage)));
	}

	private CreateBookCommand aCreateBooksCommandWith(String title, MultipartFile file) {
		CreateBookCommand createBookCommand = new CreateBookCommand();
		createBookCommand.setTitle(title);
		createBookCommand.setFile(file);

		return createBookCommand;
	}

}
