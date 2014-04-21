package org.cen.robot.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class is the super class to writes data which are exchanged between
 * client and server
 * 
 * @author svanacker
 * @version 10/03/2007
 */
public class DataComponent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The label for title of component */
	protected JLabel label;

	/** The zone in which text appears */
	protected JTextArea textArea;

	/** Button to clear the text */
	protected JButton clearButton;

	/**
	 * Constructor
	 */
	public DataComponent(String caption) {
		super();

		label = new JLabel(caption);
		add(label);

		textArea = new JTextArea(10, 15);
		textArea.setEditable(false);
		JScrollPane outPane = new JScrollPane(textArea);
		add(outPane);

		clearButton = new JButton("Clear");
		add(clearButton);
		clearButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}

		});
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
}
