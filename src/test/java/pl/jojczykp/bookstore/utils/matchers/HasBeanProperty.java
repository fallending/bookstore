package pl.jojczykp.bookstore.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class HasBeanProperty extends BaseMatcher {

	private String propertyName;
	private Matcher valueMatcher;
	private boolean beanPropertyValueFound;
	private Object beanPropertyValue;

	@Factory
	public static Matcher hasBeanProperty(String propertyName, Matcher valueMatcher) {
		return new HasBeanProperty(propertyName, valueMatcher);
	}

	protected HasBeanProperty(String propertyName, Matcher valueMatcher) {
		this.propertyName = propertyName;
		this.valueMatcher = valueMatcher;
	}

	@Override
	public boolean matches(Object bean) {
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
	public void describeMismatch(Object bean, Description description) {
		if (beanPropertyValueFound) {
			description.appendText("found property value ").appendValue(beanPropertyValue);
		} else {
			description.appendText("property not found");
		}
	}

}
