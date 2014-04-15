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

import org.hibernate.criterion.Order;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static pl.jojczykp.bookstore.utils.PageSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.PageSorterDirection.ASC;

public class PageSorter {

	private static final PageSorterColumn ANY_NOT_NULL_COLUMN = BOOK_TITLE;
	private static final PageSorterDirection ANY_NOT_NULL_DIRECTION = ASC;

	private PageSorterColumn column;
	private PageSorterDirection direction;

	public PageSorter() {
		this.column = ANY_NOT_NULL_COLUMN;
		this.direction = ANY_NOT_NULL_DIRECTION;
	}

	public PageSorterColumn getColumn() {
		return column;
	}

	public void setColumn(PageSorterColumn column) {
		this.column = column;
	}

	public PageSorterDirection getDirection() {
		return direction;
	}

	public void setDirection(PageSorterDirection direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{column=" + column.name() + ", direction=" + direction.name() + "}";
	}

	public static Order orderBy(PageSorterColumn column, PageSorterDirection direction) {
		return caseAwareOrderFor(column.isIgnoreCase(), directionAwareOrderFor(column, direction));
	}

	private static Order directionAwareOrderFor(PageSorterColumn column, PageSorterDirection direction) {
		if (direction == ASC) {
			return asc(column.getNameForQuery());
		} else {
			return desc(column.getNameForQuery());
		}
	}

	private static Order caseAwareOrderFor(boolean ignoreCase, Order order) {
		if (ignoreCase) {
			return order.ignoreCase();
		} else {
			return order;
		}
	}

}
