package pl.jojczykp.bookstore.utils;

public class PageParams {

	private int offset;
	private int size;

	public PageParams() {
	}

	public PageParams(int offset, int size) {
		this.offset = offset;
		this.size = size;
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{offset=" + offset + ", size=" + size + "}";
	}
}
