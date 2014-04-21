package org.cen.com;

/**
 * Exception to check the expected data lenght.
 */
public class IllegalComDataLengthException extends IllegalComDataException {

	private static final long serialVersionUID = 1L;

	public IllegalComDataLengthException(int expected, int actual) {
		super("Data length is : " + actual + " but expected is : " + expected);
	}
}
