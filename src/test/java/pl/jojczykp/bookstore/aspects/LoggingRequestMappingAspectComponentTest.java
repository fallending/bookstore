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

package pl.jojczykp.bookstore.aspects;

import org.apache.log4j.Appender;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.apache.log4j.Level.INFO;
import static org.apache.log4j.Logger.getLogger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.jojczykp.bookstore.aspects.ToBeWrappedWithAspectTestController.SOME_TO_BE_WRAPPED_CONTROLLER_URL;
import static pl.jojczykp.bookstore.aspects.ToBeWrappedWithAspectTestController.VIEW_NAME_FROM_WRAPPED_CONTROLLER;

import org.springframework.test.context.web.WebAppConfiguration;
import pl.jojczykp.bookstore.jmx.ConfigMBean;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"classpath:spring/controllers-test-context.xml",
		"classpath:spring/jmx-mock-context.xml",
		"classpath:spring/dispatcherServletContext/aspect-context.xml"})
public class LoggingRequestMappingAspectComponentTest {

	@Autowired private WebApplicationContext wac;
	private MockMvc mvcMock;
	private ResultActions mvcMockPerformResult;

	@Autowired private LoggingRequestMappingAspect testee;

	@Autowired private ConfigMBean configMBean;
	@Captor private ArgumentCaptor<LoggingEvent> loggingEvents;
	@Mock private Appender appenderMock;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvcMock = webAppContextSetup(wac)
				.alwaysDo(print())
				.build();
		givenMockedLoggingAppender();
	}

	private void givenMockedLoggingAppender() {
		getLogger(LoggingRequestMappingAspect.class).addAppender(appenderMock);
	}

	@After
	public void removeMockedLoggingAppender() {
		getLogger(LoggingRequestMappingAspect.class).removeAppender(appenderMock);
	}

	@Test
	public void shouldLogBeforeAndAfterIfEnabled() throws Exception {
		givenLoggingByAspectEnabled();

		whenRequestedGetUrl(SOME_TO_BE_WRAPPED_CONTROLLER_URL);

		thenExpectRequestCorrectlyProcessed();
		thenExpectWrappedFunctionalityExecuted();
		thenExpectLoggerInvokedBeforeAndAfter();
	}

	@Test
	public void shouldNotLogBeforeAndAfterIfDisabled() throws Exception {
		givenLoggingByAspectDisabled();

		whenRequestedGetUrl(SOME_TO_BE_WRAPPED_CONTROLLER_URL);

		thenExpectRequestCorrectlyProcessed();
		thenExpectWrappedFunctionalityExecuted();
		thenExpectLoggerNotInvoked();
	}

	private void givenLoggingByAspectEnabled() {
		given(configMBean.isRequestMappingLoggingEnabled()).willReturn(true);
	}

	private void givenLoggingByAspectDisabled() {
		given(configMBean.isRequestMappingLoggingEnabled()).willReturn(false);
	}

	private void whenRequestedGetUrl(String url) throws Exception {
		mvcMockPerformResult = mvcMock.perform(get(url));
	}

	private void thenExpectRequestCorrectlyProcessed() throws Exception {
		mvcMockPerformResult.andExpect(status().isOk())
				.andExpect(model().hasNoErrors());
	}

	private void thenExpectWrappedFunctionalityExecuted() throws Exception {
		mvcMockPerformResult.andExpect(view().name(VIEW_NAME_FROM_WRAPPED_CONTROLLER));
	}

	private void thenExpectLoggerNotInvoked() {
		verifyZeroInteractions(appenderMock);
	}

	private void thenExpectLoggerInvokedBeforeAndAfter() {
		verify(appenderMock, times(2)).doAppend(loggingEvents.capture());
		assertThatLoggedPrefixedInfo(0, "Invoking ");
		assertThatLoggedPrefixedInfo(1, "Done ");
	}

	private void assertThatLoggedPrefixedInfo(int index, String prefix) {
		LoggingEvent loggingEvent = loggingEvents.getAllValues().get(index);

		assertThat(loggingEvent.getLevel(), is(INFO));
		assertThat((String) loggingEvent.getMessage(), startsWith(prefix));
	}

}
