package org.cen.com.in;

/**
 * Class which corresponds to a data which has no Header or which is not
 * recognized by the system.
 */
final public class UntypedInData extends InData {
	private final String data;

	public UntypedInData(String data) {
		super();
		this.data = data;
	}

	public String getData() {
		return data;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[data=" + data.toString() + "]";
	}
}
