package org.cen.util;

public class Holder<T> {
	private T value;

	public Holder(T value) {
		super();
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
