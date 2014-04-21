package org.cen.robot.graphics;

import org.cen.com.out.OutData;
import org.cen.com.out.OutDataListener;

/**
 * This components enables to write the activity of server and client
 * @author svanacker
 * @version 11/03/2007
 */
public class LogComponent extends DataComponent implements OutDataListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	/**
	 * Constructor
	 *
	 */
	public LogComponent() {
		super("Log");
	}

	public void onOutDataEvent(OutData outData) {
		// TODO 
	}


}
