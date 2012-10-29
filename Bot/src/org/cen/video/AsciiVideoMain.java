package org.cen.video;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.apache.commons.io.IOUtils;

public class AsciiVideoMain {

	public static final String FRAME_SEPARATOR = "STRATEGY_BOARD";

	private String[] videoPages;

	private int pageIndex = 0;

	private int pageCount;

	private JTextArea textArea;

	private void addNextPageButton(Container container) {
		Action action = new AbstractAction("Next") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				pageIndex++;
			}
		};

		// Create the button; the icon will appear to the left of the label
		JButton button = new JButton(action);
	}

	private void showPage() {
		String text = "";
		if (pageIndex < pageCount) {
			text = videoPages[pageIndex];
		}
		textArea.setText(text);
	}

	private InputStream getInputStream() throws Exception {
		InputStream result = new FileInputStream(new File("d:/uart.txt"));
		return result;
	}

	private void readStream() throws Exception {
		InputStream inputStream = getInputStream();
		String text = IOUtils.toString(inputStream);
		videoPages = text.split(FRAME_SEPARATOR);
		pageCount = videoPages.length;
		pageIndex = 0;
	}

	public void createFrame() throws Exception {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));

		Container container = frame.getContentPane();

		addNextPageButton(container);
		textArea = new JTextArea();
		Font font = new Font("Monospaced", Font.PLAIN, 12);
		textArea.setFont(font);

		container.add(textArea);
		frame.setVisible(true);

		frame.pack();

		readStream();
		showPage();
	}

	public static void main(String[] args) throws Exception {
		AsciiVideoMain main = new AsciiVideoMain();
		main.createFrame();
	}
}
