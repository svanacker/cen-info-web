package org.cen.cup.cup2009.robot.match;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import org.cen.geom.Point2D;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.cen.com.IComService;
import org.cen.cup.cup2009.gameboard.configuration.GameboardConfigurationHandler2009;
import org.cen.logging.LoggingUtils;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.robot.device.lcd.com.LcdPrintOutData;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.match.MatchData;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;
import org.cen.util.StringConstants;
import org.cen.vision.IVisionService;
import org.cen.vision.dataobjects.WebCamProperties;

public class ConfigurationAnalyzer {
	private static final String PROPERTY_LOW_ANALYSIS_SLEEP_TIME = "lowAnalysisSleepTime";

	private static final String PROPERTY_FAST_ANALYSIS_SLEEP_TIME = "fastAnalysisSleepTime";

	private static final int DEFAULT_LOW_ANALYSIS_SLEEP_TIME = 6000;

	private static final int DEFAULT_FAST_ANALYSIS_SLEEP_TIME = 2000;

	public static final double COLOR_ANGLE_GREEN = 1.4;

	public static final double COLOR_ANGLE_RED = 0.2;

	private GameboardConfigurationHandler2009 handler;

	private Logger LOGGER = LoggingUtils.getClassLogger();

	private IRobotServiceProvider servicesProvider;

	private boolean terminated = false;

	private IVisionService vision;

	public ConfigurationAnalyzer(IRobotServiceProvider servicesProvider) {
		super();
		this.servicesProvider = servicesProvider;

		initialize();
	}

	public void done() {
		terminated = true;
		handler.interrupt();
	}

	private Image getImage(String name) {
		URL url = getClass().getClassLoader().getResource("org/cen/cup/cup2009/gameboard/configuration/" + name);
		Image srcImage = Toolkit.getDefaultToolkit().createImage(url);
		new ImageIcon(srcImage);
		BufferedImage inputImage = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = inputImage.getGraphics();
		g.drawImage(srcImage, 0, 0, null);
		return inputImage;
	}

	private Image getInput() {
		if (vision.isAvailable()) {
			return vision.getImage();
		} else {
			MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
			if (data == null) {
				System.out.println("MatchData is nul");
				return null;
			}
			Integer trajectoryInteger = (Integer) data.get("uiTrajectory");

			if (trajectoryInteger == null) {
				System.out.println("trajectoryInteger is nul");
				return null;
			}

			int trajectory = trajectoryInteger;
			// int trajectory = 1;
			String suffix = "";
			switch (data.getSide()) {
			case RED: {
				suffix = "G" + trajectory;
				break;
			}
			case VIOLET: {
				suffix = "R" + trajectory;
				break;
			}
			}
			if (suffix.isEmpty()) {
				return null;
			} else {
				return getImage("CoordinatesTest" + suffix + ".jpg");
			}
		}
	}

	private void initialize() {
		handler = new GameboardConfigurationHandler2009();

		InputStream is = getClass().getClassLoader().getResourceAsStream("org/cen/cup/cup2009/gameboard/elements.properties");
		Properties elements = new Properties();
		try {
			elements.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.setElements(elements);

		vision = servicesProvider.getService(IVisionService.class);
		if (!vision.isAvailable()) {
			LOGGER.warning("vision not available, using static images instead");
		}
	}

	public void start() {
		WebCamProperties webCamProperties = RobotUtils.getRobotAttribute(WebCamProperties.class, servicesProvider);
		handler.setWebCamProperties(webCamProperties);

		final MatchData2009 data = (MatchData2009) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		Point2D point = position.getCentralPoint();
		double x = point.getX();
		double y = point.getY();
		switch (data.getSide()) {
		case RED:
			handler.setMirror(false);
			handler.setPosition(x, y, position.getAlpha());
			handler.setColorAngle(COLOR_ANGLE_GREEN);
			break;
		case VIOLET:
			handler.setMirror(true);
			handler.setPosition(x, y, position.getAlpha());
			handler.setColorAngle(COLOR_ANGLE_RED);
			break;
		}

		final IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		final IComService comService = servicesProvider.getService(IComService.class);

		Thread analyzerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Properties properties = RobotUtils.getRobot(servicesProvider).getConfiguration().getProperties();
				int fastAnalysisSleepTime = getIntValue(properties, PROPERTY_FAST_ANALYSIS_SLEEP_TIME, DEFAULT_FAST_ANALYSIS_SLEEP_TIME);
				LOGGER.finest(PROPERTY_FAST_ANALYSIS_SLEEP_TIME + " = " + fastAnalysisSleepTime);
				int lowAnalysisSleepTime = getIntValue(properties, PROPERTY_LOW_ANALYSIS_SLEEP_TIME, DEFAULT_LOW_ANALYSIS_SLEEP_TIME);
				LOGGER.finest(PROPERTY_LOW_ANALYSIS_SLEEP_TIME + " = " + lowAnalysisSleepTime);

				LOGGER.fine("analyzing gameboard");
				int sleepTime = fastAnalysisSleepTime;
				int card = Strategy2009.TRAJECTORY_HOMOLOGATION;
				while (!terminated) {
					Image image = getInput();
					if (image != null) {
						handler.setInput(image);
						handler.execute();

						// test d'interruption de l'analyse
						if (terminated) {
							break;
						}

						int newCard = handler.getColumnElementsCard();
						int error = handler.getError();
						if (card != newCard && error < 4) {
							card = newCard;
							String s = "Card: " + card;
							LOGGER.finest(s);
							comService.writeOutData(new LcdPrintOutData(StringConstants.STR_CLS + s));
							sleepTime = lowAnalysisSleepTime;

							data.setGameBoardConfiguration(card);
							strategy.notifyEvent(new GameboardConfigurationReadEvent(card));
						}
					}
					try {
						if (!terminated) {
							String s = StringConstants.STR_CR + "Analyze in " + (sleepTime / 1000) + " s";
							comService.writeOutData(new LcdPrintOutData(s));
							Thread.sleep(sleepTime);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			private int getIntValue(Properties properties, String key, int defaultValue) {
				String s = properties.getProperty(key);
				try {
					Integer i = Integer.valueOf(s);
					return i;
				} catch (Exception e) {
					LOGGER.warning("invalid property value: " + key + "=" + s);
					return defaultValue;
				}
			}
		});

		if (data.getHomologation()) {
			int card = data.getGameBoardConfiguration();

			String s = StringConstants.STR_CLS + "Homologation: card " + card;
			comService.writeOutData(new LcdPrintOutData(s));

			strategy.notifyEvent(new GameboardConfigurationReadEvent(card));
		} else {
			analyzerThread.start();
		}
	}
}
