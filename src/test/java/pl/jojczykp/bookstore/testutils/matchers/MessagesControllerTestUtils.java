package pl.jojczykp.bookstore.testutils.matchers;

import org.springframework.test.web.servlet.ResultActions;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static pl.jojczykp.bookstore.testutils.matchers.HasBeanProperty.hasBeanProperty;

public abstract class MessagesControllerTestUtils {

	private static final String BOOKS_COMMAND_ATTR = "booksCommand";

	public static void thenExpectInfoOnlyMessage(ResultActions mvcMockPerformResult, String expectedMessage) {
		try {
			mvcMockPerformResult
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.infos",
						equalTo(asList(expectedMessage)))))
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.warns", empty())))
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.errors", empty())));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void thenExpectWarnOnlyMessage(ResultActions mvcMockPerformResult, String expectedMessage) {
		try {
			mvcMockPerformResult
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.infos", empty())))
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.warns",
						equalTo(asList(expectedMessage)))))
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.errors", empty())));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void thenExpectErrorOnlyMessage(ResultActions mvcMockPerformResult, String expectedMessage) {
		try {
			mvcMockPerformResult
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.infos", empty())))
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.warns", empty())))
				.andExpect(flash().attribute(BOOKS_COMMAND_ATTR, hasBeanProperty("messages.errors",
						equalTo(asList(expectedMessage)))));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
