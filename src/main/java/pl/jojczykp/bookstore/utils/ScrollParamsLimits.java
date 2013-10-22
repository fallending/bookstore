package pl.jojczykp.bookstore.utils;

public class ScrollParamsLimits {

	private int offset;
	private int size;

	public ScrollParamsLimits(int offset, int size) {
		this.offset = offset;
		this.size = size;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "[" + offset + ", " + size + "]";
	}
}
