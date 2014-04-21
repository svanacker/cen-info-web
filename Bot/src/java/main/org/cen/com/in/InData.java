package org.cen.com.in;

import org.cen.com.ComData;

/**
 * Objects which represents the data which comes from the Javelin and which must
 * be decrypted.
 * 
 * @author svanacker
 * @version 22/02/2007
 */
public abstract class InData extends ComData {

	public InData() {
		super();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
