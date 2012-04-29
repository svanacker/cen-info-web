package org.cen.com.in;

import org.cen.com.ComData;

/**
 * Encapsulation of the data which is sent by the client.
 */
public class RawInData extends ComData {

	/**
	 * The raw Data which is sent by the client and received by the server.
	 */
	protected String rawData;

	/**
	 * Returns the raw data which is sent by the client.
	 * 
	 * @return
	 */
	public String getRawData() {
		return rawData;
	}

	/**
	 * Constructor
	 * 
	 * @param data
	 *            the data which is sent by the client.
	 */
	public RawInData(String data) {
		super();
		rawData = data;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[rawData=" + rawData + "]";
	}
}
