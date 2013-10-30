package pl.jojczykp.bookstore.utils;

import org.springframework.stereotype.Service;

import static java.lang.Math.max;

@Service
public class ScrollParamsLimiter {

	public void limit(ScrollParams scrollParams) {
		int offset = scrollParams.getOffset();
		int size = scrollParams.getSize();
		int totalCount = scrollParams.getTotalCount();

		int limitedOffset = max(0, offset > totalCount ? totalCount : offset);
		int limitedSize = max(0, limitedOffset + size > totalCount ? totalCount - limitedOffset : size);

		if (offset < 0 && size <= totalCount) {
			limitedSize += offset;
		}

		scrollParams.setOffset(limitedOffset);
		scrollParams.setSize(limitedSize);
	}

}
