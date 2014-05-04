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

package pl.jojczykp.bookstore.utils;

import org.springframework.stereotype.Service;
import pl.jojczykp.bookstore.commands.parts.PagerCommand;

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
