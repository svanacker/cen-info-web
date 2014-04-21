package org.cen.com;

/**
 * The Exception when the data from the serial Interface cannot be parsed.
 */
public class IllegalComDataException extends Exception {

	/** Used for serialization. */
	private static final long serialVersionUID = 1L;

	public IllegalComDataException() {

	}

	public IllegalComDataException(String message) {
		super(message);
	}

}
