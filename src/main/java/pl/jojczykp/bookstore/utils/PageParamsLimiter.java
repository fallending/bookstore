package pl.jojczykp.bookstore.utils;

import org.springframework.stereotype.Service;

import static java.lang.Math.max;

@Service
public class PageParamsLimiter {

	public PageParams limit(PageParams pageParams, int totalCount) {
		int offset = pageParams.getOffset();
		int size = pageParams.getSize();

		int limitedOffset = max(0, offset > totalCount ? totalCount : offset);
		int limitedSize = max(0, limitedOffset + size > totalCount ? totalCount - limitedOffset : size);

		return new PageParams(limitedOffset, limitedSize);
	}

}
