package org.cen.cup.cup2011.navigation;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.cen.cup.cup2011.gameboard.GameBoard2011;
import org.cen.cup.cup2011.gameboard.elements.StartArea2011;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.AbstractNavigationMap;
import org.cen.navigation.Location;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Navigation map for the cup 2011.
 */
public class NavigationMap2011 extends AbstractNavigationMap implements
		ResourceLoaderAware {

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private static final int GRID_SIZE_Y = 11;

	private static final int GRID_SIZE_X = 10;

	private ResourceLoader resourcesLoader;

	private String resourcesPath;

	private Properties robotProperties;

	private static final int DISTANCE = GameBoard2011.BOX_SIZE / 2;

	private static final int OFFSET_X_A = 185;

	private static final int OFFSET_X = GameBoard2011.BOX_SIZE / 2;

	private static final int OFFSET_Y = GameBoard2011.BOX_SIZE / 2
			+ StartArea2011.START_AREA_HEIGHT + GameBoard2011.BAND_WIDTH;

	private static final int Y_INTERMEDIATE_POINT = 250;

	private static final int Y_GREEN_ZONE = 150;

	private void addPath(int x1, int y1, int x2, int y2) {
		String start = getLocationName(x1, y1);
		String end = getLocationName(x2, y2);
		addPath(start, end);
	}

	private void buildGrid() {
		String[] exclusions = { "J2", "J6", "J10", "J14", "K6", "K10" };
		Set<String> excluded = new HashSet<String>();
		for (String s : exclusions) {
			excluded.add(s);
		}
		String[] protectionZone = { "K3", "K4", "K5", "K11", "K12", "K13" };
		Set<String> protectedZone = new HashSet<String>();
		for (String s : protectionZone) {
			protectedZone.add(s);
		}

		addLocation(new Location("red start", 178, 180));
		addLocation(new Location("blue start", 178, 2820));

		for (int y = 0; y < GRID_SIZE_Y; y++) {
			for (int x = 0; x < GRID_SIZE_X + 1; x++) {
				String name = getLocationName(x, y);
				if (!excluded.contains(name)) {
					if (protectedZone.contains(name)) {
						addLocation(new Location(name, x * DISTANCE
								+ ((OFFSET_X * 70) / 100), y * DISTANCE
								+ OFFSET_Y));
					} else if (name.substring(0, 1).equals("K")) {
						addLocation(new Location(name, x * DISTANCE + OFFSET_X,
								y * DISTANCE + OFFSET_Y));
					} else if (name.substring(0, 1).equals("A")) {
						addLocation(new Location(name, OFFSET_X_A, y * DISTANCE
								+ OFFSET_Y));
					} else {
						addLocation(new Location(name, x * DISTANCE + OFFSET_X,
								y * DISTANCE + OFFSET_Y));
					}
				}
			}
		}

		for (int x = 0; x < 5; x++) {
			addLocation(new Location("GZB" + (x + 1), 695 + x * 280,
					GameBoard2011.BOARD_HEIGHT - Y_GREEN_ZONE));
			addLocation(new Location("GZR" + (x + 1), 695 + x * 280,
					Y_GREEN_ZONE));
		}
		addLocation(new Location("GZB6", 1675, GameBoard2011.BOARD_HEIGHT
				- Y_INTERMEDIATE_POINT));
		addLocation(new Location("GZR6", 1675, Y_INTERMEDIATE_POINT));

		addPath("GZB1", "D13");
		addPath("GZB1", "E13");
		addPath("E13", "GZB2", "F13");
		addPath("F13", "GZB3", "G13");
		addPath("GZB3", "H13", "GZB4");
		addPath("I13", "GZB4");
		addPath("I13", "GZB6", "GZB5");

		addPath("GZR1", "D3");
		addPath("GZR1", "E3");
		addPath("E3", "GZR2", "F3");
		addPath("F3", "GZR3", "G3");
		addPath("GZR3", "H3", "GZR4");
		addPath("I3", "GZR4");
		addPath("I3", "GZR6", "GZR5");

		// for (int y = -2; y < 1; y++) {
		// for (int x = 3; x < GRID_SIZE_X; x++) {
		// String name = getLocationName(x, y);
		// if (!excluded.contains(name)) {
		// addLocation(new Location(name, x * DISTANCE + OFFSET_X, y * DISTANCE
		// + OFFSET_Y));
		// }
		// }
		// }
		//
		// for (int y = GRID_SIZE_Y - 1; y < GRID_SIZE_Y + 2; y++) {
		// for (int x = 3; x < GRID_SIZE_X; x++) {
		// String name = getLocationName(x, y);
		// if (!excluded.contains(name)) {
		// addLocation(new Location(name, x * DISTANCE + OFFSET_X, y * DISTANCE
		// + OFFSET_Y));
		// }
		// }
		// }

		for (int y = -2; y < GRID_SIZE_Y + 2; y++) {
			for (int x = 0; x < GRID_SIZE_X + 1; x++) {
				addPath(x, y, x + 1, y);
				addPath(x, y, x + 1, y + 1);
				addPath(x, y, x, y + 1);
				addPath(x + 1, y, x, y + 1);
			}
		}

		addPath("red start", "A3");
		addPath("blue start", "A13");
	}

	private String getLocationName(int x, int y) {
		return (char) (x + 'A') + String.valueOf(y + 3);
	}

	private void initConfiguration() {
		robotProperties = new Properties();
		Resource resource = resourcesLoader.getResource(resourcesPath);
		try {
			InputStream is = resource.getInputStream();
			try {
				robotProperties.load(is);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			LOGGER.warning("unable to load properties: " + e.getMessage());
		}
	}

	@Override
	protected void logInvalidLocation(String s) {
		// super.logInvalidLocation(s);
	}

	@Override
	public void reset() {
		super.reset();
		initConfiguration();
		buildGrid();
	}

	@Override
	public void setResourceLoader(ResourceLoader resourcesLoader) {
		this.resourcesLoader = resourcesLoader;
	}

	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}
}
