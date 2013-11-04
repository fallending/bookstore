package pl.jojczykp.bookstore.utils;

import org.springframework.stereotype.Service;

import static java.lang.Math.max;

@Service
public class ScrollParamsLimiter {

	public ScrollParams limit(ScrollParams scrollParams, int totalCount) {
		int offset = scrollParams.getOffset();
		int size = scrollParams.getSize();

		int limitedOffset = max(0, offset > totalCount ? totalCount : offset);
		int limitedSize = max(0, limitedOffset + size > totalCount ? totalCount - limitedOffset : size);

		if (offset < 0 && size <= totalCount) {
			limitedSize = max(0, limitedSize + offset);
		}

		return new ScrollParams(limitedOffset, limitedSize);
	}

}
