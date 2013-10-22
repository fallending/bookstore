package pl.jojczykp.bookstore.utils;

import org.springframework.stereotype.Service;

import static java.lang.Math.max;

@Service
public class ScrollParamsLimiter {

	public ScrollParamsLimits computeLimitsFor(int offset, int size, int totalCount) {
		int limitedOffset = max(0, offset > totalCount ? totalCount : offset);
		int limitedSize = max(0, limitedOffset + size > totalCount ? totalCount - limitedOffset : size);

		if (offset < 0 && size <= totalCount) {
			limitedSize += offset;
		}

		return new ScrollParamsLimits(limitedOffset, limitedSize);
	}

}
