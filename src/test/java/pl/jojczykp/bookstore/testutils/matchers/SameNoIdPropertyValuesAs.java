package pl.jojczykp.bookstore.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.persistence.Id;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.beans.PropertyUtil.propertyDescriptorsFor;
import static org.hamcrest.beans.SamePropertyValuesAs.PropertyMatcher;

/**
 * Bases on code of SameNoIdPropertyValuesAs class.
 */
public class SameNoIdPropertyValuesAs<T> extends TypeSafeDiagnosingMatcher<T> {

	private final T expectedBean;
	private final Set<String> propertyNames;
	private final List<PropertyMatcher> propertyMatchers;

	public SameNoIdPropertyValuesAs(T expectedBean) {
		PropertyDescriptor[] descriptors = noIdsPropertyDescriptors(expectedBean);
		this.expectedBean = expectedBean;
		this.propertyNames = propertyNamesFrom(descriptors);
		this.propertyMatchers = propertyMatchersFor(expectedBean, descriptors);
	}

	@Override
	public boolean matchesSafely(T bean, Description mismatch) {
		return isCompatibleType(bean, mismatch)
				&& hasNoExtraProperties(bean, mismatch)
				&& hasMatchingValues(bean, mismatch);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("same property values as " + expectedBean.getClass().getSimpleName())
				.appendList(" [", ", ", "]", propertyMatchers);
	}

	private static <T> PropertyDescriptor[] noIdsPropertyDescriptors(T expectedBean) {
		PropertyDescriptor[] descriptors = propertyDescriptorsFor(expectedBean, Object.class);
		descriptors = skipDescriptorsForIds(expectedBean, descriptors);
		return descriptors;
	}

	private static <T> PropertyDescriptor[] skipDescriptorsForIds(T expectedBean, PropertyDescriptor[] descriptors) {
		List<PropertyDescriptor> noIdDescriptors = new LinkedList<>();
		for (PropertyDescriptor descriptor : descriptors) {
			if (isNotIdProperty(expectedBean, descriptor)) {
				noIdDescriptors.add(descriptor);
			}
		}
		return noIdDescriptors.toArray(new PropertyDescriptor[noIdDescriptors.size()]);
	}

	private boolean isCompatibleType(T item, Description mismatchDescription) {
		if (!expectedBean.getClass().isAssignableFrom(item.getClass())) {
			mismatchDescription.appendText("is incompatible type: " + item.getClass().getSimpleName());
			return false;
		}
		return true;
	}


	private static <T> boolean isNotIdProperty(T expectedBean, PropertyDescriptor descriptor) {
		try {
			String fieldName = descriptor.getName();
			Field field = expectedBean.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return !field.isAnnotationPresent(Id.class);
		} catch (NoSuchFieldException ex) {
			throw new RuntimeException(ex);
		}
	}

	private boolean hasNoExtraProperties(T item, Description mismatchDescription) {
		Set<String> actualPropertyNames = propertyNamesFrom(noIdsPropertyDescriptors(item));
		actualPropertyNames.removeAll(propertyNames);
		if (!actualPropertyNames.isEmpty()) {
			mismatchDescription.appendText("has extra properties called " + actualPropertyNames);
			return false;
		}
		return true;
	}

	private boolean hasMatchingValues(T item, Description mismatchDescription) {
		for (PropertyMatcher propertyMatcher : propertyMatchers) {
			if (!propertyMatcher.matches(item)) {
				propertyMatcher.describeMismatch(item, mismatchDescription);
				return false;
			}
		}
		return true;
	}

	private static <T> List<PropertyMatcher> propertyMatchersFor(T bean, PropertyDescriptor[] descriptors) {
		List<PropertyMatcher> result = new ArrayList<>(descriptors.length);
		for (PropertyDescriptor propertyDescriptor : descriptors) {
			result.add(new PropertyMatcher(propertyDescriptor, bean));
		}
		return result;
	}

	private static Set<String> propertyNamesFrom(PropertyDescriptor[] descriptors) {
		HashSet<String> result = new HashSet<>();
		for (PropertyDescriptor propertyDescriptor : descriptors) {
			result.add(propertyDescriptor.getDisplayName());
		}
		return result;
	}

	@Factory
	public static <T> Matcher<T> sameNoIdPropertyValuesAs(T expectedBean) {
		return new SameNoIdPropertyValuesAs<>(expectedBean);
	}

}
