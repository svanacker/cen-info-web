package org.cen.vision;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import java.io.IOException;

import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;
import javax.media.jai.IHSColorSpace;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.PlanarImage;
import javax.media.jai.registry.RIFRegistry;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cen.vision.dataobjects.CalibrationData;
import org.cen.vision.dataobjects.CalibrationDescriptor;
import org.cen.vision.filters.CalibrationFilterDescriptor;
import org.cen.vision.filters.CalibrationStat;
import org.cen.vision.filters.ColorFilterDescriptor;
import org.cen.vision.filters.TargetFilterDescriptor;
import org.cen.vision.filters.TargetStat;
import org.cen.vision.filters.TargetStat.TargetLocation;
import org.cen.vision.util.CalibrationDebugListener;
import org.cen.vision.util.CalibrationHandler;
import org.cen.vision.util.GoalTargetHandler;
import org.cen.vision.util.TargetDebugListener;
import org.cen.vision.util.TargetHandler;
import org.cen.vision.util.TargetHandlerThread;

public class Main {
	private static final int IMAGE_WIDTH = 640;

	private static final int IMAGE_HEIGHT = 480;

	public static void main(String[] args) {
		new Main();
	}

	private JSlider weightThresholdSlider;

	private JSlider bottomBoundSlider;

	private JSlider topBoundSlider;

	public Main() {
		super();

		registerOperators();

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));

		Container c = frame.getContentPane();
		c.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		JPanel imagesPanel = new JPanel();

		final JLabel inImage = new JLabel();
		imagesPanel.add(inImage);

		// Données de calibration
		final CalibrationDescriptor descriptor = new CalibrationDescriptor(new CalibrationData[] { new CalibrationData("red", 0, .3), new CalibrationData("yellow", 1.03, .3), new CalibrationData("green", 2.53, .3), new CalibrationData("blue", 4.11, .3), new CalibrationData("test", 3.20, .3) });

		final JLabel outImage = new JLabel();
		imagesPanel.add(outImage);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		c.add(imagesPanel, gbc);

		JPanel slidersPanel = new JPanel();
		slidersPanel.setLayout(new GridLayout(6, 2));

		JLabel label = new JLabel("Angle de couleur");
		slidersPanel.add(label);
		final JSlider angleSlider = new JSlider(0, 628, 314);
		slidersPanel.add(angleSlider);

		label = new JLabel("Saturation");
		slidersPanel.add(label);
		final JSlider saturationSlider = new JSlider(0, 100, 10);
		slidersPanel.add(saturationSlider);

		label = new JLabel("Intensité");
		slidersPanel.add(label);
		final JSlider intensitySlider = new JSlider(0, 100, 50);
		slidersPanel.add(intensitySlider);

		label = new JLabel("Borne inférieure");
		slidersPanel.add(label);
		bottomBoundSlider = new JSlider(0, 100, 100);
		slidersPanel.add(bottomBoundSlider);

		label = new JLabel("Borne supérieure");
		slidersPanel.add(label);
		topBoundSlider = new JSlider(0, 100, 0);
		slidersPanel.add(topBoundSlider);

		label = new JLabel("Seuil de détection");
		slidersPanel.add(label);
		weightThresholdSlider = new JSlider(0, 200, 150);
		slidersPanel.add(weightThresholdSlider);

		gbc.gridwidth = 1;
		c.add(slidersPanel, gbc);

		final JRadioButton b1 = new JRadioButton("Canette (jaune)");
		final JRadioButton b2 = new JRadioButton("Bouteille (vert)");
		final JRadioButton b3 = new JRadioButton("Piles (bleu)");
		final JRadioButton b4 = new JRadioButton("Piles (rouge)");
		final JRadioButton b5 = new JRadioButton("Test");

		b1.setActionCommand("yellow");
		b2.setActionCommand("green");
		b3.setActionCommand("blue");
		b4.setActionCommand("red");
		b5.setActionCommand("test");

		ButtonGroup bg = new ButtonGroup();
		bg.add(b1);
		bg.add(b2);
		bg.add(b3);
		bg.add(b4);
		bg.add(b5);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(4, 1));
		p.add(b1);
		p.add(b2);
		p.add(b3);
		p.add(b4);
		p.add(b5);

		c.add(p);

		final JCheckBox colorFilterCheckBox = new JCheckBox("Color filter");
		c.add(colorFilterCheckBox);
		final JCheckBox historyCheckBox = new JCheckBox("History");
		c.add(historyCheckBox);

		frame.setVisible(true);

		Acquisition a = new Acquisition();
		try {
			a.open();
		} catch (NoPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotRealizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final Acquisition ac = a;
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (ac != null)
					ac.close();
				super.windowClosed(e);
			}
		});
		frame.pack();

		// Attente de l'initialisation
		a.waitForInput();

		// Calibration de la webcam
		System.out.println("Calibrating...");
		final CalibrationHandler ch = new CalibrationHandler(ac);
		ch.setDistanceThreshold(.25);
		// Affichage de la calibration pour débogage
		ch.addDebugListener(new CalibrationDebugListener() {
			public void debug(Image img, CalibrationStat stats) {
				drawCalibration(img, stats);
				inImage.setIcon(new ImageIcon(img));
			}

			private void drawCalibration(Image img, CalibrationStat cstats) {
				Graphics2D g = (Graphics2D) img.getGraphics();
				CalibrationStat.Sample[] s = cstats.getSamples();
				ColorSpace c = IHSColorSpace.getInstance();
				int w = img.getWidth(null);
				int h = img.getHeight(null);
				float[] f = new float[3];
				int n = s.length;
				for (int i = n - 1; i >= 0; i--) {
					// System.out.println(s[i]);
					if (s[i].getDistance() < ch.getDistanceThreshold()) {
						f[0] = 1f;
						f[1] = (float) s[i].getAngle();
						f[2] = 1f;
						f = c.toRGB(f);
						for (int j = 0; j < 3; j++)
							f[j] = Math.min(1, f[j]);
						g.setColor(new Color(f[0], f[1], f[2], .5f));
						g.fillRect(i * w / n, 0, w / n, h);
					}
				}
			}
		});
		// if (ch.execute(descriptor))
		// System.out.println("Calibration done");
		// else
		// System.out.println("Calibration failed");

		// Affichage des résultats de la calibration
		System.out.println(descriptor.toString());

		final float[] f = new float[IMAGE_WIDTH * IMAGE_HEIGHT * 3];

		final GoalTargetHandler h = new GoalTargetHandler(a);

		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = e.getActionCommand();
				// Utilisation des informations de calibrage
				CalibrationData d = descriptor.getData(name);
				if (d != null) {
					angleSlider.setValue((int) (d.getValue() * 100));
					h.setFilteredColor(angleSlider.getValue() / 100f);
				}
			}
		};

		b1.addActionListener(l);
		b2.addActionListener(l);
		b3.addActionListener(l);
		b4.addActionListener(l);
		b5.addActionListener(l);

		ChangeListener cl = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				h.setFilteredColor(angleSlider.getValue() / 100f);
				h.setIntensityThreshold(intensitySlider.getValue() / 100f);
				h.setSaturationThreshold(saturationSlider.getValue() / 100f);
				h.setWeightThreshold(weightThresholdSlider.getValue() / 100f);
				h.setGoadDetectionThreshold(weightThresholdSlider.getValue() / 100f);
				h.setTopBound(topBoundSlider.getValue() / 100f);
				h.setBottomBound(bottomBoundSlider.getValue() / 100f);
			}
		};

		angleSlider.addChangeListener(cl);
		intensitySlider.addChangeListener(cl);
		saturationSlider.addChangeListener(cl);
		weightThresholdSlider.addChangeListener(cl);

		h.addDebugListener(new TargetDebugListener() {
			public void debug(TargetHandler targetHandler, Image img, TargetStat stats, double[][] history) {
				ParameterBlock pb = new ParameterBlock();
				pb.addSource(img);
				pb.add(0.01 * angleSlider.getValue());

				if (colorFilterCheckBox.isSelected()) {
					pb = new ParameterBlock();
					pb.addSource(img);
					pb.add(angleSlider.getValue());
					pb.add(f);
					PlanarImage target = JAI.create("ColorFilter", pb, null);
					Image im = target.getAsBufferedImage();
					if (historyCheckBox.isSelected()) {
						drawHistory(im, history);
					}
					outImage.setIcon(new ImageIcon(im));
				}

				TargetLocation[] l = h.getGoalBounds();
				drawTarget(img, l[0]);
				drawTarget(img, l[1]);

				inImage.setIcon(new ImageIcon(img));
			}
		});
		h.setFilteredColor(descriptor.getData("test").getResult());
		TargetHandlerThread ht = new TargetHandlerThread(h);
		ht.resume();
		try {
			ht.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void drawHistory(Image img, double[][] data) {
		double cx = img.getWidth(null) / TargetHandler.GRID_SIZE;
		double cy = img.getHeight(null) / TargetHandler.GRID_SIZE;
		Graphics2D g = (Graphics2D) img.getGraphics();
		int ly = (int) (data[0].length * topBoundSlider.getValue() / 100d);
		int hy = (int) (data[0].length * bottomBoundSlider.getValue() / 100d);
		g.setColor(Color.RED);
		g.fillRect(0, 0, (int) (cx * data.length), (int) (cy * ly));
		g.setColor(Color.GREEN);
		g.fillRect(0, (int) (cy * hy), (int) (cx * data.length), (int) (cy * data[0].length));
		for (int x = 0; x < data.length; x++)
			for (int y = ly; y < hy; y++) {
				float d = (float) data[x][y] / 2;
				Color c;
				if (d > (weightThresholdSlider.getValue() / 200f)) {
					c = new Color(d, 0, d, 1f);
				} else {
					c = new Color(d, d, d, 1f);
				}
				g.setColor(c);
				g.fillRect((int) (x * cx), (int) (y * cy), 10, 8);
			}
	}

	private void drawTarget(Image img, TargetLocation data) {
		System.out.println(data);
		Graphics2D g = (Graphics2D) img.getGraphics();
		if (data.getWeight() < weightThresholdSlider.getValue() / 100f) {
			g.drawString("No target", 0, 15);
			return;
		}
		double cx = img.getWidth(null) / TargetHandler.GRID_SIZE;
		double cy = img.getHeight(null) / TargetHandler.GRID_SIZE;
		Point p = data.getLocation();
		g.setColor(Color.red);
		g.fillOval((int) (p.x * cx), (int) (p.y * cy), 10, 10);
		g.setColor(Color.white);
		g.drawOval((int) (p.x * cx), (int) (p.y * cy), 10, 10);
	}

	private void registerOperators() {
		String productName = "testjai";
		OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();

		ColorFilterDescriptor colorFilterDescriptor = new ColorFilterDescriptor();
		String operationName = "ColorFilter";
		RenderedImageFactory rif = colorFilterDescriptor;
		or.registerDescriptor(colorFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);

		TargetFilterDescriptor targetFilterDescriptor = new TargetFilterDescriptor();
		operationName = "TargetFilter";
		rif = targetFilterDescriptor;
		or.registerDescriptor(targetFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);

		CalibrationFilterDescriptor calibrationFilterDescriptor = new CalibrationFilterDescriptor();
		operationName = "CalibrationFilter";
		rif = calibrationFilterDescriptor;
		or.registerDescriptor(calibrationFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);
	}
}
