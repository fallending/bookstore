package pl.jojczykp.bookstore.utils;

public class ScrollParams {

	private int offset;
	private int size;
	private int totalCount;

	public ScrollParams() {
	}

	public ScrollParams(int offset, int size, int totalCount) {
		this.offset = offset;
		this.size = size;
		this.totalCount = totalCount;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public String toString() {
		return "[" + offset + ", " + size + ", " + totalCount + "]";
	}
}
