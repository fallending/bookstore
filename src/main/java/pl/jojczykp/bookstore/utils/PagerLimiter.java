package pl.jojczykp.bookstore.utils;

import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.PagerCommand;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
public class PagerLimiter {

	public PagerCommand createLimited(PagerCommand requestPager, int totalCount) {
		int limitedPagesCount = divCap(totalCount, requestPager.getPageSize());
		int limitedPageNumber = requestPager.getPageNumber();

		limitedPageNumber = min(limitedPageNumber, limitedPagesCount);
		limitedPageNumber = max(limitedPageNumber, 1);

		int limitedPageSize = max(0, requestPager.getPageSize());

		return aPagerCommandWith(limitedPageNumber, limitedPageSize, limitedPagesCount, totalCount,
				requestPager.getSorter().getColumn(), requestPager.getSorter().getDirection());
	}

	private int divCap(int number, int div) {
		return number / div + (number % div > 0 ? 1 : 0);
	}

	private PagerCommand aPagerCommandWith(int pageNumber, int pageSize, int pagesCount, int totalCount,
										PageSorterColumn sortColumn, PageSorterDirection sortDirection) {
		PagerCommand limitedPager = new PagerCommand();
		limitedPager.setPageNumber(pageNumber);
		limitedPager.setPageSize(pageSize);
		limitedPager.setPagesCount(pagesCount);
		limitedPager.setTotalCount(totalCount);
		limitedPager.getSorter().setColumn(sortColumn);
		limitedPager.getSorter().setDirection(sortDirection);

		return limitedPager;
	}
}
