package pl.jojczykp.bookstore.utils;

import java.util.List;

public abstract class SuppressUnchecked {

	@SuppressWarnings("unchecked")
	public static <T> List<T> suppressUnchecked(List list) {
		return (List<T>) list;
	}

}
