package org.cen.cup.cup2010.navigation;

import static org.cen.cup.cup2010.gameboard.GameBoard2010.BOARD_HEIGHT;
import static org.cen.cup.cup2010.gameboard.GameBoard2010.BOARD_WIDTH;
import static org.cen.cup.cup2010.gameboard.GameBoard2010.BORDER_WIDTH;
import static org.cen.cup.cup2010.gameboard.GameBoard2010.CONTAINER_Y_FROM_LAST_CORN;
import static org.cen.cup.cup2010.gameboard.GameBoard2010.CORN_SPACING_X;
import static org.cen.cup.cup2010.gameboard.GameBoard2010.CORN_SPACING_Y;
import static org.cen.cup.cup2010.gameboard.GameBoard2010.CORN_START_X;
import static org.cen.cup.cup2010.gameboard.GameBoard2010.CORN_START_Y;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cen.logging.LoggingUtils;
import org.cen.navigation.AbstractNavigationMap;
import org.cen.navigation.Location;
import org.cen.navigation.PathVectorLine;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Navigation map for the cup 2010.
 * 
 * @author Emmanuel ZURMELY
 */
public class NavigationMap2010 extends AbstractNavigationMap implements ResourceLoaderAware {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private ResourceLoader resourcesLoader;

	private String resourcesPath;

	private Properties robotProperties;

	public void addPath(HashMap<String, Location> locationsMap, String... locations) {
		Location begin;
		Location end = null;
		for (String s : locations) {
			begin = end;
			end = locationsMap.get(s);
			if (end == null) {
				LOGGER.severe("Unknown location: " + s);
			} else if (begin != null) {
				PathVectorLine v = addVector(begin, end);
				double angle = Math.atan2(end.getY() - begin.getY(), end.getX() - begin.getX());
				v.setType((int) (angle * 100));
			}
		}
	}

	private void buildGrid() {
		// start positions
		addLocation(new Location("blue start", 154, 324));
		addLocation(new Location("blue start 2", 569, 324));
		// addLocation(new Location("blue start", 122 + 160, 160));
		addLocation(new Location("blue container", BOARD_WIDTH - 100, BOARD_HEIGHT - CORN_START_Y - CONTAINER_Y_FROM_LAST_CORN));
		addLocation(new Location("yellow start", 154, BOARD_HEIGHT - 324));
		addLocation(new Location("yellow start 2", 569, BOARD_HEIGHT - 324));
		// addLocation(new Location("yellow start", 122 + 160, BOARD_HEIGHT -
		// 160));
		addLocation(new Location("yellow container", BOARD_WIDTH - 100, CORN_START_Y + CONTAINER_Y_FROM_LAST_CORN));
		addLocation(new Location("blue end", BOARD_WIDTH - BORDER_WIDTH - 128 - (CORN_SPACING_X * CONTAINER_Y_FROM_LAST_CORN) / CORN_SPACING_Y, BOARD_HEIGHT - CORN_START_Y - CONTAINER_Y_FROM_LAST_CORN));
		addLocation(new Location("yellow end", BOARD_WIDTH - BORDER_WIDTH - 128 - (CORN_SPACING_X * CONTAINER_Y_FROM_LAST_CORN) / CORN_SPACING_Y, CORN_START_Y + CONTAINER_Y_FROM_LAST_CORN));

		// grid
		int row = 0;
		for (int y = CORN_START_Y; y <= BOARD_HEIGHT; y += CORN_SPACING_Y / 2) {
			int col = 0;
			for (int x = CORN_START_X; x < BOARD_WIDTH; x += CORN_SPACING_X) {
				addLocation(new Location("R" + row + "C" + col, x, y));
				col++;
			}
			row++;
		}

		// Paths
		HashMap<String, Location> map = new HashMap<String, Location>();
		Collection<Location> locations = getLocations();
		for (Location location : locations) {
			map.put(location.getName(), location);
		}

		addPath(map, "blue start", "blue start 2", "R2C0");
		addPath(map, "yellow start", "yellow start 2", "R10C0");
		addPath(map, "R2C0", "R4C1", "R6C2", "R8C3", "R10C4");
		addPath(map, "R0C1", "R2C2", "R4C3", "R6C4", "R8C5");
		addPath(map, "R6C0", "R8C1", "R10C2", "R12C3");
		addPath(map, "R6C0", "R4C1", "R2C2", "R0C3");
		addPath(map, "R10C0", "R8C1", "R6C2", "R4C3", "R2C4");
		addPath(map, "R12C1", "R10C2", "R8C3", "R6C4", "R4C5");
		addPath(map, "R2C0", "R6C0", "R10C0");
		addPath(map, "R10C4", "blue end", "blue container");
		addPath(map, "R2C4", "yellow end", "yellow container");
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

	@PostConstruct
	public void initialize() {
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
