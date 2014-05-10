package org.cen.cup.cup2009.ui.web;

import org.cen.geom.Point2D;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.cen.cup.cup2009.gameboard.GameBoard2009;
import org.cen.cup.cup2009.gameboard.elements.ColumnElement;
import org.cen.cup.cup2009.gameboard.elements.ColumnElement.ColumnElementColor;
import org.cen.cup.cup2009.gameboard.elements.ColumnElementPosition;
import org.cen.cup.cup2009.gameboard.elements.Trajectory;
import org.cen.cup.cup2009.gameboard.elements.VerticalDispenser;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardService;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ColumnElementPositionsView implements ResourceLoaderAware {
	private static final String COLUMN_ELEMENT_POSITION = "column element position ";

	private static final String DISPENSER_POSITION = "dispenser position ";

	private static final String RANDOM_COLUMN_ELEMENT = "random column element";

	private static final String TRAJECTORY_ELEMENT = "trajectory";

	private int columnElementsConfigurationId;

	private String configuration;

	private int dispensersConfigurationId;

	private final Map<String, Point2D> positions = new HashMap<String, Point2D>();

	private Properties properties;

	private ResourceLoader resourceLoader;

	private IRobotServiceProvider servicesProvider;

	private double time;

	private final boolean[] trajectory = new boolean[] { true, false, false };

	private void buildPositionsMap() {
		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		List<IGameBoardElement> elements = gameBoard.getElements();
		for (IGameBoardElement e : elements) {
			String name = e.getName();
			if (e instanceof ColumnElementPosition) {
				if (name.startsWith(COLUMN_ELEMENT_POSITION)) {
					if (e.getPosition().getY() > 1500) {
						// On ne garde qu'un seul c�t�, l'autre �tant sym�trique
						continue;
					}
					String id = name.substring(COLUMN_ELEMENT_POSITION.length());
					positions.put(id, e.getPosition());
				}
			} else if (e instanceof VerticalDispenser && name.startsWith(DISPENSER_POSITION)) {
				if (e.getPosition().getY() > 1500) {
					// On ne garde qu'un seul c�t�, l'autre �tant sym�trique
					continue;
				}
				String id = name.substring(DISPENSER_POSITION.length());
				positions.put(id, e.getPosition());
			}
		}
	}

	public int getColumnElementsConfigurationId() {
		return columnElementsConfigurationId;
	}

	public String getConfiguration() {
		return configuration;
	}

	public int getDispensersConfigurationId() {
		return dispensersConfigurationId;
	}

	public String getEstimatedTime() {
		return DecimalFormat.getNumberInstance().format(time) + " s";
	}

	private double getEstimatedTime(List<Point2D> positions, int i) {
		double time = 0;
		Point2D prec = null, prec2 = null;
		for (Point2D p : positions) {
			// distance
			if (prec != null) {
				// 10 cm/s
				time += prec.distance(p) / 100;
			}
			if (prec2 != null) {
				// rotation
				double a1 = Math.atan2(p.getY() - prec.getY(), p.getX() - prec.getX());
				double a2 = Math.atan2(prec.getY() - prec2.getY(), prec.getX() - prec2.getX());
				double a = 2 * Math.PI - a1 - a2;
				a %= Math.PI;
				// 45�/s
				time += a / (Math.PI / 4);
			}
			prec2 = prec;
			prec = p;
		}
		// collecting
		switch (i) {
		case 0:
			time += 4 * 3;
			break;
		case 1:
			time += 2 * 3;
			break;
		}
		return time;
	}

	private List<Point2D> getPositions(int configurationId) {
		List<Point2D> result = new ArrayList<Point2D>();
		Properties p = getProperties();
		String s = p.getProperty("card" + configurationId);
		if (s != null) {
			String[] positionIds = s.split("\\W*,\\W*");
			for (String id : positionIds) {
				// Si une position n'existe pas, on l�ve une exception, pour
				// �tre s�r de ne pas passer � c�t�
				if (!positions.containsKey(id)) {
					throw new RuntimeException("Point with id does not exist : " + id);
				} else {
					Point2D point = positions.get(id);
					if (point != null) {
						result.add(point);
					}
				}
			}
		}
		return result;
	}

	private Properties getProperties() {
		if (properties == null) {
			Resource resource = resourceLoader.getResource(configuration);
			try {
				InputStream is = resource.getInputStream();
				properties = new Properties();
				properties.load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	public boolean[] getTrajectory() {
		return trajectory;
	}

	private List<Point2D> getTrajectory(int configurationId, int index) {
		String prefix;
		MatchSide side = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider).getSide();
		switch (side) {
		case RED:
			prefix = "green-";
			break;
		default:
			prefix = "red-";
			break;
		}

		List<Point2D> results = new ArrayList<Point2D>();
		ITrajectoryService trajectory = servicesProvider.getService(ITrajectoryService.class);
		INavigationMap map = trajectory.getNavigationMap();
		Map<String, Location> locations = new HashMap<String, Location>();
		for (Location l : map.getLocations()) {
			locations.put(l.getName(), l);
		}
		Properties p = getProperties();
		String s = p.getProperty("trajectory" + configurationId + "." + index);
		if (s != null) {
			String[] positionIds = org.apache.commons.lang3.StringUtils.split(s, ",");
			for (String id : positionIds) {
				if (id.startsWith("^")) {
					continue;
				}
				// Si une position n'existe pas, on l�ve une exception, pour
				// �tre s�r de ne pas passer � c�t�
				if (!locations.containsKey(prefix + id)) {
					throw new RuntimeException("Point with id does not exist : " + id);
				} else {

					Location l = locations.get(prefix + id);
					if (l != null) {
						results.add(l.getPosition());
					}
				}
			}
		}
		return results;
	}

	public int getTrajectoryCardId() {
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		Integer id = (Integer) data.get("analyzedCard");
		if (id == null) {
			return 0;
		} else {
			return id;
		}
	}

	public String getTrajectoryData() {
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < trajectory.length; i++) {
			if (trajectory[i]) {
				String t = data.getProperty("trajectoryData" + i);
				if (t != null) {
					b.append(t);
				}
			}
		}
		String trajectory = b.toString();
		if (data.getProperty("analyzed") == null) {
			Integer card = (Integer) data.get("card");
			if (card != null) {
				columnElementsConfigurationId = card.intValue();
				updateView();
				data.setProperty("analyzed", "true");
			}
		}
		return trajectory;
	}

	public void setColumnElementsConfigurationId(int configurationId) {
		if (this.columnElementsConfigurationId == configurationId) {
			return;
		}
		this.columnElementsConfigurationId = configurationId;
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		data.put("uiTrajectory", configurationId);
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public void setDispensersConfigurationId(int dispensersConfigurationId) {
		if (this.dispensersConfigurationId == dispensersConfigurationId) {
			return;
		}
		this.dispensersConfigurationId = dispensersConfigurationId;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		this.servicesProvider = provider;
	}

	public void updateView() {
		// rebuild positions
		buildPositionsMap();
		// clear existing configurations
		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		List<IGameBoardElement> elements = gameBoard.getElements();
		Iterator<IGameBoardElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			IGameBoardElement element = iterator.next();
			String name = element.getName();
			if (name.equals(RANDOM_COLUMN_ELEMENT) || name.startsWith(TRAJECTORY_ELEMENT)) {
				iterator.remove();
			}
		}
		// obtain new configuration positions
		List<Point2D> positions = new ArrayList<Point2D>();
		positions.addAll(getPositions(columnElementsConfigurationId));
		positions.addAll(getPositions(dispensersConfigurationId));
		// put elements on the game board
		for (Point2D p : positions) {
			elements.add(new ColumnElement(RANDOM_COLUMN_ELEMENT, ColumnElementColor.GREEN, p));
			Point2D pg = new Point2D.Double(p.getX(), GameBoard2009.BOARD_HEIGHT - p.getY());
			elements.add(new ColumnElement(RANDOM_COLUMN_ELEMENT, ColumnElementColor.RED, pg));
		}
		// obtain trajectory
		time = 0;
		for (int i = 0; i < trajectory.length; i++) {
			if (!trajectory[i]) {
				continue;
			}
			positions = getTrajectory(columnElementsConfigurationId, i);
			time += getEstimatedTime(positions, i);
			elements.add(new Trajectory(TRAJECTORY_ELEMENT + i, positions, i));
		}
	}
}
