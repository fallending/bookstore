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

package pl.jojczykp.bookstore.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class HasBeanProperty extends TypeSafeMatcher<Object> {

	private String propertyName;
	private Matcher valueMatcher;
	private boolean beanPropertyValueFound;
	private Object beanPropertyValue;

	@Factory
	public static Matcher<Object> hasBeanProperty(String propertyName, Matcher valueMatcher) {
		return new HasBeanProperty(propertyName, valueMatcher);
	}

	protected HasBeanProperty(String propertyName, Matcher valueMatcher) {
		this.propertyName = propertyName;
		this.valueMatcher = valueMatcher;
	}

	@Override
	protected boolean matchesSafely(Object bean) {
		try {
			beanPropertyValueFound = true;
			return tryExtractPropertyValueFrom(bean);
		} catch(SpelEvaluationException e) {
			beanPropertyValueFound = false;
			return false;
		}
	}

	private boolean tryExtractPropertyValueFrom(Object bean) {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(propertyName);
		beanPropertyValue = exp.getValue(bean);

		return valueMatcher.matches(beanPropertyValue);
	}

	@Override
	public void describeTo(Description description) {
		description
				.appendText("property ")
				.appendValue(propertyName)
				.appendText(" = ")
				.appendDescriptionOf(valueMatcher)
				.appendText(" ");
	}

	@Override
	public void describeMismatchSafely(Object bean, Description description) {
		if (beanPropertyValueFound) {
			description.appendText("found property value ").appendValue(beanPropertyValue);
		} else {
			description.appendText("property not found");
		}
	}

}
