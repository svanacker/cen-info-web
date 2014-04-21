package org.cen.com.out;

import org.cen.com.ComData;

/**
 * Objects which represents the data which are exchange This object must be
 * subclassed to be used.
 * 
 * @author svanacker
 */
public abstract class OutData extends ComData {

	private boolean waitForAck;

	/**
	 * Constructor.
	 */
	public OutData() {
		this(true);
	}

	/**
	 * Constructor
	 * 
	 * @param waitForAck
	 *            the value of the waitForAck flag
	 * @see setWaitForAck
	 */
	public OutData(boolean waitForAck) {
		super();
		this.waitForAck = waitForAck;
	}

	/**
	 * Returns the arguments of the message.
	 * 
	 * @return the arguments of the message
	 */
	public String getArguments() {
		// No arguments by default
		return null;
	}

	/**
	 * Returns a string intended for debugging purpose and representing the data
	 * contained in this object.
	 * 
	 * @return a representation of the data contained in this object
	 */
	public String getDebugString() {
		// Default debug string (no data)
		return "[]";
	}

	/**
	 * The header which is the discriminator for each instruction that is send
	 * to the javelin.
	 * 
	 * @return the header of the message
	 */
	public abstract String getHeader();

	/**
	 * Returns the message which must be send to the client
	 * 
	 * @return the message which must be send to the client
	 */
	public String getMessage() {
		StringBuilder message = new StringBuilder();
		message.append(getHeader());
		String args = getArguments();
		if (args != null) {
			message.append(args);
		}
		return message.toString();
	}

	/**
	 * Retrieves the value of the flag indicating whether the COM service has to
	 * wait for acknowledgment after sending the data.
	 * 
	 * @return the value of the waitForAck flag
	 */
	public boolean getWaitForAck() {
		return waitForAck;
	}

	/**
	 * Defines whether the COM service has to wait for acknowledgment after
	 * sending the data.
	 * 
	 * @param waitForAck
	 *            the value of the waitForAck flag
	 */
	public void setWaitForAck(boolean waitForAck) {
		this.waitForAck = waitForAck;
	}

	// object implementation

	@Override
	public int hashCode() {
		int result = getMessage().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		OutData other = (OutData) obj;
		String message = getMessage();
		String otherMessage = other.getMessage();
		if (!message.equals(otherMessage)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + getDebugString();
	}

}
