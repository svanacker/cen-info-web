package org.cen.cup.cup2009.navigation;

import static org.cen.cup.cup2009.gameboard.GameBoard2009.BOARD_HEIGHT;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.BOARD_MIDDLE_HEIGHT;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.BOARD_MIDDLE_WIDTH;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.BOARD_WIDTH;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_OFFSET_LAST_X;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_OFFSET_LAST_Y;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_OFFSET_X;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_OFFSET_Y;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_SPACING_X;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_SPACING_Y;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cen.cup.cup2009.gameboard.GameBoard2009;
import org.cen.cup.cup2009.robot.match.Strategy2009;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.AbstractNavigationMap;
import org.cen.navigation.IPathVector;
import org.cen.navigation.Location;
import org.cen.navigation.PathVectorLine;
import org.cen.robot.RobotPosition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class NavigationMap2009 extends AbstractNavigationMap implements ResourceLoaderAware {

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private ResourceLoader resourcesLoader;

	private String resourcesPath;

	private Properties robotProperties;

	@Override
	public void setResourceLoader(ResourceLoader resourcesLoader) {
		this.resourcesLoader = resourcesLoader;
	}

	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}

	private void addPath(HashMap<String, Location> map, String origin, String... destinations) {
		for (String destination : destinations) {
			Location o = map.get(origin);
			Location d = map.get(destination);
			if (o != null && d != null) {
				PathVectorLine v = addVector(o, d);
				double angle = Math.atan2(d.getY() - o.getY(), d.getX() - o.getX());
				v.setType((int) (angle * 100));
			}
		}
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

	private void buildGrid() {
		RobotPosition robotPosition = new RobotPosition(0, 0, 0);
		robotPosition.setFromProperties(robotProperties, Strategy2009.PROPERTY_INITIAL_POSITION + ".BLUE.");

		// Positionne le point de d�part du robot en fonction du robot
		addLocation(new Location("green-s", (int) robotPosition.getCentralPoint().getX(), (int) robotPosition.getCentralPoint().getY()));
		addLocation(new Location("green-c", BOARD_WIDTH / 2, BOARD_HEIGHT / 2));

		// Points avec les coordonn�es ajust�es
		int i = 1;
		for (int x = COLUMN_OFFSET_X; x <= COLUMN_OFFSET_LAST_X; x += (COLUMN_SPACING_X - 5)) {
			for (int y = COLUMN_OFFSET_Y; y <= COLUMN_OFFSET_LAST_Y; y += COLUMN_SPACING_Y) {
				addLocation(new Location("green-" + i, x, y));
				i++;
			}
		}
		// "f" signifie "front"
		addLocation(new Location("green-f0", 275, 300));
		i = 1;
		for (int x = COLUMN_OFFSET_X; x <= COLUMN_OFFSET_LAST_X + COLUMN_SPACING_X; x += COLUMN_SPACING_X - 5) {
			addLocation(new Location("green-f" + i, x, 400));
			i++;
		}

		// Points avec les coordonn�es exactes
		i = 1;
		for (int x = COLUMN_OFFSET_X; x <= COLUMN_OFFSET_LAST_X; x += COLUMN_SPACING_X) {
			for (int y = COLUMN_OFFSET_Y; y <= COLUMN_OFFSET_LAST_Y; y += COLUMN_SPACING_Y) {
				addLocation(new Location("green-a" + i, x, y));
				i++;
			}
		}
		// "f" signifie "front"
		addLocation(new Location("green-f0", 275, 300));
		i = 1;
		for (int x = COLUMN_OFFSET_X; x <= COLUMN_OFFSET_LAST_X + COLUMN_SPACING_X; x += COLUMN_SPACING_X) {
			addLocation(new Location("green-fa" + i, x, 400));
			i++;
		}
		addLocation(new Location("green-f5", 1375, 300));

		i = 1;
		// On a besoin d'une ligne suppl�mentaire pour certains points
		for (int y = COLUMN_OFFSET_Y; y <= COLUMN_OFFSET_LAST_Y + COLUMN_SPACING_Y; y += COLUMN_SPACING_Y) {
			// Points � gauche
			addLocation(new Location("green-l" + i, 1300, y));
			// Point � droite
			addLocation(new Location("green-r" + i, 450, y));
			// Points encore plus � gauche
			addLocation(new Location("green-ll" + i, 1375, y));
			i++;
		}
		// Point pour larguer le 1er linteau, en venant de la position 6
		addLocation(new Location("green-base6", BOARD_MIDDLE_WIDTH - 187, BOARD_MIDDLE_HEIGHT - 267));

		// Points particuliers

		// ANCIENS POINTS

		// Position en retrait du linteau le plus proche
		addLocation(new Location("green-lin1", 300, 900));
		// Position ou on prend le linteau le plus proche => Le robot est � 190
		// mm du bord lorsqu'il prend un linteau
		addLocation(new Location("green-lin2", 190, 900));

		// Position en retrait du linteau le plus proche
		addLocation(new Location("green-lin21", 300, 1300));
		// Position ou on prend le linteau le plus proche
		addLocation(new Location("green-lin22", 190, 1300));

		// Centre (Proche du centre), depuis la premi�re ligne
		addLocation(new Location("green-base", COLUMN_OFFSET_X, BOARD_MIDDLE_HEIGHT));
		// Centre (Proche du centre), pour l'homologation
		addLocation(new Location("green-base-h", COLUMN_OFFSET_X, BOARD_MIDDLE_HEIGHT + 150));
		// Point pour larguer le 2�me linteau
		addLocation(new Location("green-base2", 750, BOARD_MIDDLE_HEIGHT));

		HashMap<String, Location> map = new HashMap<String, Location>();
		Collection<Location> locations = getLocations();
		for (Location location : locations) {
			map.put(location.getName(), location);
		}

		// Points de palets
		for (i = 1; i < 12; i++) {
			if (i % 3 != 0) {
				addPath(map, "green-" + i, "green-" + (i + 1));
			}
			if (i < 10) {
				addPath(map, "green-" + i, "green-" + (i + 3));
			}
		}
		// Dessine les chemins sur l'interface graphique
		addPath(map, "green-f0", "green-f1", "green-r1");
		addPath(map, "green-f1", "green-f2", "green-1");
		addPath(map, "green-f2", "green-f3", "green-4");
		addPath(map, "green-f3", "green-f4", "green-7");
		addPath(map, "green-f4", "green-f5", "green-10");
		addPath(map, "green-f5", "green-l1");
		addPath(map, "green-l1", "green-l2", "green-10");
		addPath(map, "green-l2", "green-l3", "green-11");
		addPath(map, "green-l3", "green-12");
		addPath(map, "green-r1", "green-r2", "green-1");
		addPath(map, "green-r2", "green-r3", "green-2");
		addPath(map, "green-r3", "green-3");

		symetrize(map);
	}

	/**
	 * G�n�rer les trajectoires rouges par sym�trie.
	 * 
	 * @param map
	 */
	private void symetrize(HashMap<String, Location> map) {
		List<Location> locations = new ArrayList<Location>();
		for (Location l : getLocations()) {
			String s = l.getName();
			if (s.startsWith("green")) {
				s = "red" + s.substring("green".length());
				locations.add(new Location(s, l.getX(), GameBoard2009.BOARD_HEIGHT - l.getY()));
			}
		}

		List<PathVectorLine> paths = new ArrayList<PathVectorLine>();
		for (IPathVector v : getPathVectors()) {
			Location s = v.getStart();
			Location e = v.getEnd();
			String sn = s.getName();
			String en = e.getName();
			if (sn.startsWith("green") && en.startsWith("green")) {
				sn = "red" + sn.substring("green".length());
				en = "red" + en.substring("green".length());
				s = new Location(sn, s.getX(), GameBoard2009.BOARD_HEIGHT - s.getY());
				e = new Location(en, e.getX(), GameBoard2009.BOARD_HEIGHT - e.getY());
				paths.add(new PathVectorLine(s, e));
			}
		}

		for (Location l : locations) {
			addLocation(l);
		}
		for (PathVectorLine v : paths) {
			addVector(v);
		}
	}

	@PostConstruct
	public void initialize() {
		initConfiguration();
		buildGrid();
	}

}
