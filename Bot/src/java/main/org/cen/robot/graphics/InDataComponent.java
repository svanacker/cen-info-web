package org.cen.robot.graphics;

import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;

/**
 * This class is a text area with scrollBars which enables to see which
 * instructions are sent to the Javelin
 * 
 * @author svanacker
 * @version 10/03/2007
 */
public class InDataComponent extends DataComponent implements InDataListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 */
	public InDataComponent() {
		super("In Data");
	}

	public void onInData(InData data) {
		textArea.setText(textArea.getText() + "\n" + data.toString());
	}
}
