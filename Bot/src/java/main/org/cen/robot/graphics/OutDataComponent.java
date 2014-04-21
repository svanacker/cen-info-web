package org.cen.robot.graphics;

import org.cen.com.out.OutData;
import org.cen.com.out.OutDataListener;


/**
 * The component which writes the instruction which are sent to the Javelin
 * @author svanacker
 * @version 11/03/2007
 */
public class OutDataComponent extends DataComponent implements OutDataListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public OutDataComponent() {
		super("Out Data");
	}


	public void onOutDataEvent(OutData outData) {
		String text = textArea.getText();
		text += "\n" + outData.getMessage();
		textArea.setText(text);
		
	}
}
