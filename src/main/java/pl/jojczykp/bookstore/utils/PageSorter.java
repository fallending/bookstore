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
