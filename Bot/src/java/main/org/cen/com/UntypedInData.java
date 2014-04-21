package org.cen.com;

import org.cen.com.in.InData;

/**
 * Corresponds to a data which is not managed by the system.
 */
final public class UntypedInData extends InData {
	
	/** The data was sent to the system. */
	private final String data;

	/**
	 * Constructor
	 * @param data the data which was received and not managed by the system.
	 */
	public UntypedInData(String data) {
		super();
		this.data = data;
	}

	/**
	 * Return the data which was received and not managed by the system.
	 * @return the data which was received and not managed by the system.
	 */
	public String getData() {
		return data;
	}
}
