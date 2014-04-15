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

package pl.jojczykp.bookstore.testutils.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

public abstract class MessagesControllerTestUtils {

	private static final List<String> EMPTY_STRING_LIST = ImmutableList.of();

	private static final String BOOKS_COMMAND_ATTR = "booksCommand";

	public static void thenExpectInfoOnlyFlashMessages(ResultActions mvcMockPerformResult, String... expectedMsgs) {
		thenExpectFlashMessages(mvcMockPerformResult, asList(expectedMsgs), EMPTY_STRING_LIST, EMPTY_STRING_LIST);
	}

	public static void thenExpectWarnOnlyFlashMessages(ResultActions mvcMockPerformResult, String... expectedMsgs) {
		thenExpectFlashMessages(mvcMockPerformResult, EMPTY_STRING_LIST, asList(expectedMsgs), EMPTY_STRING_LIST);
	}

	public static void thenExpectErrorOnlyFlashMessages(ResultActions mvcMockPerformResult, String... expectedMsgs) {
		thenExpectFlashMessages(mvcMockPerformResult, EMPTY_STRING_LIST, EMPTY_STRING_LIST, asList(expectedMsgs));
	}

	public static void thenExpectNoFlashMessages(ResultActions mvcMockPerformResult) {
		thenExpectFlashMessages(mvcMockPerformResult, EMPTY_STRING_LIST, EMPTY_STRING_LIST, EMPTY_STRING_LIST);
	}

	public static void thenExpectFlashMessages(ResultActions mvcMockPerformResult,
											List<String> infos, List<String> warns, List<String> errors) {
		try {
			mvcMockPerformResult
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.infos", equalTo(infos))))
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.warns", equalTo(warns))))
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.errors", equalTo(errors))));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void thenExpectModelMessages(ResultActions mvcMockPerformResult,
											List<String> infos, List<String> warns, List<String> errors) {
		try {
			mvcMockPerformResult
				.andExpect(model().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.infos", equalTo(infos))))
				.andExpect(model().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.warns", equalTo(warns))))
				.andExpect(model().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.errors", equalTo(errors))));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
