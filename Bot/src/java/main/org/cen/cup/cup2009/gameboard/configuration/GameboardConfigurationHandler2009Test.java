package org.cen.cup.cup2009.gameboard.configuration;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.cen.cup.cup2009.robot.match.ConfigurationAnalyzer;
import org.cen.cup.cup2009.robot.match.Strategy2009;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.vision.dataobjects.WebCamProperties;
import org.cen.vision.util.JAIUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameboardConfigurationHandler2009Test {
	private GameboardConfigurationHandler2009 handler;

	/** Les propri�t�s du robot. */
	private Properties robotProperties;

	/** Est mis � jour par les propri�t�s. */
	private RobotPosition robotPosition = new RobotPosition(0, 0, 0);

	private void expectR(int i) {
		handler.setMirror(true);
		robotPosition.setFromProperties(robotProperties, Strategy2009.PROPERTY_INITIAL_POSITION + ".RED.");
		handler.setPosition(robotPosition.getCentralPoint().getX(), robotPosition.getCentralPoint().getY(), robotPosition.getAlpha());
		handler.setColorAngle(ConfigurationAnalyzer.COLOR_ANGLE_RED);
		handler.execute();
		Assert.assertEquals(i, handler.getColumnElementsCard());
	}

	private void expectG(int i) {
		handler.setMirror(false);
		robotPosition.setFromProperties(robotProperties, Strategy2009.PROPERTY_INITIAL_POSITION + ".BLUE.");
		handler.setPosition(robotPosition.getCentralPoint().getX(), robotPosition.getCentralPoint().getY(), robotPosition.getAlpha());
		handler.setColorAngle(ConfigurationAnalyzer.COLOR_ANGLE_GREEN);
		handler.execute();
		Assert.assertEquals(i, handler.getColumnElementsCard());
	}

	static {
		ConsoleHandler h = new ConsoleHandler();
		h.setLevel(Level.ALL);
		Logger l = Logger.getLogger("");
		l.setLevel(Level.ALL);
		l.addHandler(h);
	}

	@BeforeClass
	public static void registerOperators() {
		JAIUtils.registerOperators();
	}

	public void setImage(String name) {
		URL url = getClass().getClassLoader().getResource("org/cen/cup/cup2009/gameboard/configuration/" + name);
		Image srcImage = Toolkit.getDefaultToolkit().createImage(url);
		new ImageIcon(srcImage);
		BufferedImage inputImage = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = inputImage.getGraphics();
		g.drawImage(srcImage, 0, 0, null);
		handler.setInput(inputImage);
	}

	@Before
	public void setUp() {
		// Chargement des propri�t�s
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("org/cen/cup/cup2009/robot/robot2009.properties");
		robotProperties = new Properties();
		try {
			robotProperties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		WebCamProperties webCamProperties = new WebCamProperties();
		webCamProperties.set(robotProperties);

		handler = new GameboardConfigurationHandler2009();
		handler.setWebCamProperties(webCamProperties);
		InputStream is = getClass().getClassLoader().getResourceAsStream("org/cen/cup/cup2009/gameboard/elements.properties");
		Properties elements = new Properties();
		try {
			elements.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.setElements(elements);
	}

	@Test
	public void TestR1() {
		setImage("CoordinatesTestR1.jpg");
		expectR(1);
	}

	@Test
	public void TestR2() {
		setImage("CoordinatesTestR2.jpg");
		expectR(2);
	}

	@Test
	public void TestR3() {
		setImage("CoordinatesTestR3.jpg");
		expectR(3);
	}

	@Test
	public void TestR4() {
		setImage("CoordinatesTestR4.jpg");
		expectR(4);
	}

	@Test
	public void TestR5() {
		setImage("CoordinatesTestR5.jpg");
		expectR(5);
	}

	@Test
	public void TestR6() {
		setImage("CoordinatesTestR6.jpg");
		expectR(6);
	}

	@Test
	public void TestR7() {
		setImage("CoordinatesTestR7.jpg");
		expectR(7);
	}

	@Test
	public void TestR8() {
		setImage("CoordinatesTestR8.jpg");
		expectR(8);
	}

	@Test
	public void TestR9() {
		setImage("CoordinatesTestR9.jpg");
		expectR(9);
	}

	@Test
	public void TestR10() {
		setImage("CoordinatesTestR10.jpg");
		expectR(10);
	}

	@Test
	public void TestG1() {
		setImage("CoordinatesTestG1.jpg");
		expectG(1);
	}

	@Test
	public void TestG2() {
		setImage("CoordinatesTestG2.jpg");
		expectG(2);
	}

	@Test
	public void TestG3() {
		setImage("CoordinatesTestG3.jpg");
		expectG(3);
	}

	@Test
	public void TestG4() {
		setImage("CoordinatesTestG4.jpg");
		expectG(4);
	}

	@Test
	public void TestG5() {
		setImage("CoordinatesTestG5.jpg");
		expectG(5);
	}

	@Test
	public void TestG6() {
		setImage("CoordinatesTestG6.jpg");
		expectG(6);
	}

	@Test
	public void TestG7() {
		setImage("CoordinatesTestG7.jpg");
		expectG(7);
	}

	@Test
	public void TestG8() {
		setImage("CoordinatesTestG8.jpg");
		expectG(8);
	}

	@Test
	public void TestG9() {
		setImage("CoordinatesTestG9.jpg");
		expectG(9);
	}

	@Test
	public void TestG10() {
		setImage("CoordinatesTestG10.jpg");
		expectG(10);
	}
}
