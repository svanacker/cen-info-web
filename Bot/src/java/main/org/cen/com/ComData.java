package org.cen.com;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The base class of all Data which communicated through the serial PORT (in or
 * out).
 * 
 * @author svanacker
 * @version 22/02/2007
 */
public abstract class ComData {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * The date for the creation of comData.
	 */
	protected Date creationDate;

	/**
	 * Constructor.
	 */
	public ComData() {
		creationDate = new Date();
	}

	/**
	 * Returns the creation date of the COM data.
	 * 
	 * @return the creation date of the COM data
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Returns the creation date of the COM data.
	 * 
	 * @return the creation date of the COM data
	 */
	public String getCreationDateAsText() {
		return dateFormat.format(creationDate);
	}

	/**
	 * Utility method for view.
	 * 
	 * @return the description of the data
	 */
	public String getDescription() {
		return toString();
	}
}
