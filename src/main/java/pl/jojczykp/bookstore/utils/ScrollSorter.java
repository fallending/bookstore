package pl.jojczykp.bookstore.utils;

import org.hibernate.criterion.Order;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static pl.jojczykp.bookstore.utils.ScrollSorterColumn.BOOK_TITLE;
import static pl.jojczykp.bookstore.utils.ScrollSorterDirection.ASC;

public class ScrollSorter {

	private static final ScrollSorterColumn ANY_NOT_NULL_COLUMN = BOOK_TITLE;
	private static final ScrollSorterDirection ANY_NOT_NULL_DIRECTION = ASC;

	private ScrollSorterColumn column;
	private ScrollSorterDirection direction;

	public ScrollSorter() {
		this.column = ANY_NOT_NULL_COLUMN;
		this.direction = ANY_NOT_NULL_DIRECTION;
	}

	public ScrollSorterColumn getColumn() {
		return column;
	}

	public void setColumn(ScrollSorterColumn column) {
		this.column = column;
	}

	public ScrollSorterDirection getDirection() {
		return direction;
	}

	public void setDirection(ScrollSorterDirection direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{column=" + column.name() + ", direction=" + direction.name() + "}";
	}

	public static Order orderBy(ScrollSorterColumn column, ScrollSorterDirection direction) {
		return caseAwareOrderFor(column.isIgnoreCase(), directionAwareOrderFor(column, direction));
	}

	private static Order directionAwareOrderFor(ScrollSorterColumn column, ScrollSorterDirection direction) {
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
