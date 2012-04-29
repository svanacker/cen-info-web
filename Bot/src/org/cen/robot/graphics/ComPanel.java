package org.cen.robot.graphics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * This panel enables to see the communication between Javelin and PC.
 * 
 * @author svanacker
 * @version 10/03/2007
 */
public class ComPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The component which log the data from the Javelin */
	protected InDataComponent inDataComponent;

	/** The component which are sent to the Javelin */
	protected OutDataComponent outDataComponent;

	/** The component which log the activity between the Javelin and the PC */
	protected LogComponent logComponent;

	private RawInDataComponent rawInDataComponent;

	public ComPanel() {
		super();
		// In Area
		inDataComponent = new InDataComponent();
		add(inDataComponent);

		// Out Area
		outDataComponent = new OutDataComponent();
		add(outDataComponent);

		// Log Area
		logComponent = new LogComponent();
		add(logComponent);

		rawInDataComponent = new RawInDataComponent();
		add(rawInDataComponent);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	public InDataComponent getInDataComponent() {
		return inDataComponent;
	}

	public OutDataComponent getOutDataComponent() {
		return outDataComponent;
	}

	public RawInDataComponent getRawInDataComponent() {
		return rawInDataComponent;
	}
}
