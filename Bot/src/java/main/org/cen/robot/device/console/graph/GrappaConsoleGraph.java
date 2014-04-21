package org.cen.robot.device.console.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;

import att.grappa.Graph;
import att.grappa.GrappaPanel;
import att.grappa.Parser;

public class GrappaConsoleGraph implements IConsoleGraph {
	private String data;

	private Graph graph;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private GrappaPanel panel;

	public GrappaConsoleGraph() {
		super();
	}

	public GrappaConsoleGraph(URL resource) {
		this();
		if (resource != null) {
			loadData(resource);
		}
	}

	private void loadData(URL resource) {
		try {
			InputStream is = resource.openStream();
			try {
				StringBuilder sb = new StringBuilder();
				while (true) {
					int b = is.read();
					if (b < 0)
						break;
					sb.append((char) b);
				}
				setData(sb.toString());
			} finally {
				is.close();
			}
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
		}
	}

	public String getData() {
		return data;
	}

	protected Graph getGraph() {
		return graph;
	}

	@Override
	public void render(Graphics2D graphics) {
		Graph graph = getGraph();
		if (graph != null) {
			Rectangle2D box = graph.getBoundingBox();
			BufferedImage bi = new BufferedImage((int) box.getWidth(), (int) box.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bi.createGraphics();
			g.setBackground(Color.WHITE);
			g.setColor(Color.BLACK);
			g.clearRect(0, 0, (int) box.getWidth(), (int) box.getHeight());
			g.setClip(0, 0, (int) box.getWidth(), (int) box.getHeight());
			panel = new GrappaPanel(graph);
			panel.setSize(box.getBounds().getSize());
			panel.clearOutline();
			panel.zoomToOutline();
			panel.paintComponent(g);

			Image img = bi.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
			graphics.drawImage(img, 0, 0, null);
		}
	}

	public void setData(String data) {
		this.data = data;
		Reader reader = new StringReader(data);
		try {
			try {
				Parser parser = new Parser(reader);
				parser.parse();
				graph = parser.getGraph();
			} finally {
				reader.close();
			}
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}
	}
}
