package org.cen.cup.cup2012.navigation;

import java.awt.Point;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.cen.cup.cup2012.gameboard.GameBoard2012;
import org.cen.cup.cup2012.gameboard.MapIsland;
import org.cen.cup.cup2012.gameboard.ShipHold;
import org.cen.cup.cup2012.gameboard.elements.StartArea2012;
import org.cen.cup.cup2012.gameboard.elements.Totem;
import org.cen.cup.cup2012.gameboard.lines.FollowLine2012;
import org.cen.cup.cup2012.robot.Robot2012;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.AbstractNavigationMap;
import org.cen.navigation.Location;
import org.cen.robot.device.navigation.BezierMoveRequest;
import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.ITargetActionItemList;
import org.cen.robot.match.strategy.ITargetActionList;
import org.cen.robot.match.strategy.TargetList;
import org.cen.robot.match.strategy.impl.DefaultTargetAction;
import org.cen.robot.match.strategy.impl.SimpleTarget;
import org.cen.robot.match.strategy.impl.SimpleTargetActionItem;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Navigation map for the cup 2012.
 */
public class NavigationMap2012 extends AbstractNavigationMap implements ResourceLoaderAware {

	private static final String SUFFIX_VIOLET = "V";

	private static final String SUFFIX_RED = "R";

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	// Distance to take bullion

	private static final double X_TO_TAKE_BULLION_LEFT = GameBoard2012.BOARD_MIDDLE_WIDTH - Robot2012.DISTANCE_ROBOT_CENTER_TO_ARM - Totem.TOTEM_INTERNAL_WIDTH;

	private static final double X_TO_TAKE_BULLION_RIGHT = GameBoard2012.BOARD_MIDDLE_WIDTH + Robot2012.DISTANCE_ROBOT_CENTER_TO_ARM + Totem.TOTEM_INTERNAL_WIDTH;

	// Global value

	private static final double FOLLOW_LINE_MIDDLE_HEIGHT = StartArea2012.START_AREA_HEIGHT + FollowLine2012.LINE_VERTICAL_HEIGHT - (FollowLine2012.LINE_WIDTH / 2);

	private static final double SECURITY_DISTANCE = 40;

	private static final double BIG_SECURITY_DISTANCE = 80;

	// X

	private static final double A_X = 250d;

	private static final double B_X = X_TO_TAKE_BULLION_LEFT;

	private static final double C_X = X_TO_TAKE_BULLION_RIGHT;

	private static final double D_X = GameBoard2012.BOARD_WIDTH - Robot2012.ROBOT_WIDTH / 2 - SECURITY_DISTANCE;

	// Y

	private static final double Y_1 = FOLLOW_LINE_MIDDLE_HEIGHT;

	private static final double Y_3 = GameBoard2012.BOARD_MIDDLE_HEIGHT;

	private static final double Y_2 = 640d + 477d;

	private static final double Y_5 = GameBoard2012.BOARD_HEIGHT - FOLLOW_LINE_MIDDLE_HEIGHT;

	private static final double Y_4 = GameBoard2012.BOARD_HEIGHT - Y_2;

	// Captain Boat Key Point

	private static final double CBKP_X1 = GameBoard2012.BOARD_MIDDLE_WIDTH + 150d;

	private static final double CBKP_Y1 = 550d;

	// Captain Boat unload Point
	private static final double CBU_X1 = GameBoard2012.BOARD_WIDTH - ShipHold.SHIP_PROTECTION_WIDTH - Robot2012.ROBOT_WIDTH / 2 - BIG_SECURITY_DISTANCE;

	private static final double CBU_Y1 = 200d;

	// Message Bottle Point
	private static final double MB_X2 = GameBoard2012.BOARD_WIDTH - 200d;

	private static final double MB_Y2 = GameBoard2012.BOARD_HEIGHT - 1000d;

	private static final double MBU_X2 = MB_X2 + 110d;

	private static final double MBU_Y2 = MB_Y2 - 100d;

	private static final double MBU_X1 = GameBoard2012.BOARD_WIDTH - 150d;

	// Map Island -> Point to go to be aligned when unload
	private static final double MI_X = 200d;

	private static final double MI_Y = GameBoard2012.BOARD_MIDDLE_HEIGHT - MapIsland.MAP_HEIGHT / 2;

	// Map Island unload Point
	private static final double MIU_X = 100d;

	private static final double MIU_Y = MI_Y;

	private ResourceLoader resourcesLoader;

	private String resourcesPath;

	private Properties robotProperties;

	private TargetList targets;

	private void addPath(int x1, int y1, int x2, int y2) {
		String start = getLocationName(x1, y1);
		String end = getLocationName(x2, y2);
		addPath(start, end);
	}

	private void addSplineRequest(ITargetActionItemList items, String location, double d1, double d2, double angle) {
		Map<String, Location> map = getLocationsMap();
		Location l = map.get(location);
		if (l == null) {
			logInvalidLocation(location);
		}

		Point p = l.getPosition();
		BezierMoveRequest request = new BezierMoveRequest(p, d1, d2, angle);
		SimpleTargetActionItem item = new SimpleTargetActionItem(request);
		items.addTargetActionItem(item);
	}

	/**
	 * Adds a symmetric location by appending its name with the "R" suffix for
	 * the red side and the "V" suffix for the violet side.
	 * 
	 * @param locationName
	 *            location name
	 * @param x
	 *            in VIOLET coordinates
	 * @param y
	 *            in VIOLET coordinates
	 */
	private void addSymmetricLocation(String locationName, int x, int y) {
		addLocation(locationName + SUFFIX_VIOLET, x, y);
		addLocation(locationName + SUFFIX_RED, x, GameBoard2012.BOARD_HEIGHT - y);
	}

	public void addSymmetricSplinePath(String location1, String location2, int cpDistance1, int cpDistance2, double angle1, double angle2) {
		addSplinePath(location1 + SUFFIX_RED, location2 + SUFFIX_RED, cpDistance1, cpDistance2, -angle1, -angle2);
		addSplinePath(location1 + SUFFIX_VIOLET, location2 + SUFFIX_VIOLET, cpDistance1, cpDistance2, angle1, angle2);
	}

	private ITargetAction addTargetAction(ITargetActionList list, ITarget target, String start, String end, int time) {
		Map<String, Location> map = getLocationsMap();
		Location l1 = map.get(start);
		Location l2 = map.get(end);
		if (l1 == null) {
			logInvalidLocation(start);
		}
		if (l2 == null) {
			logInvalidLocation(end);
		}
		DefaultTargetAction action = new DefaultTargetAction(target, l1, l2, time);
		list.addTargetAction(action);
		return action;
	}

	private void buildGrid() {
		addSymmetricLocation("Start", 0, 0);
		addSymmetricLocation("Bullion1", 0x0370, 0x0156);
		addSymmetricLocation("Bottle1", 0x0720, 0x0280);
		addSymmetricLocation("Bottle2", 0x07AF, 0x076C);

		addSymmetricSplinePath("Start", "Bullion1", 0x40, 0x40, Math.toRadians(67.5), toAngle(0xFC7C));
	}

	private void buildGrid2() {
		/*
		 * for (int y = 0; y < GRID_SIZE_Y; y++) { for (int x = 0; x <
		 * GRID_SIZE_X + 1; x++) { String name = getLocationName(x, y);
		 * addLocation(new Location(name, (int) (x * DISTANCE), (int) (y *
		 * DISTANCE))); } }
		 * 
		 * for (int y = -2; y < GRID_SIZE_Y + 2; y++) { for (int x = 0; x <
		 * GRID_SIZE_X + 1; x++) { addPath(x, y, x + 1, y); addPath(x, y, x + 1,
		 * y + 1); addPath(x, y, x, y + 1); addPath(x + 1, y, x, y + 1); } }
		 */

		// Line 1
		addLocation("A1", A_X, Y_1);
		addLocation("B1", B_X, Y_1);
		addLocation("C1", C_X, Y_1);
		addLocation("D1", D_X, Y_1);

		// Line 2
		addLocation("A2", A_X, Y_2);
		addLocation("B2", B_X, Y_2);
		addLocation("C2", C_X, Y_2);
		addLocation("D2", D_X, Y_2);

		// Line 3
		addLocation("A3", A_X, Y_3);
		addLocation("B3", B_X, Y_3);
		addLocation("C3", C_X, Y_3);
		addLocation("D3", D_X, Y_3);

		// Line 4
		addLocation("A4", A_X, Y_4);
		addLocation("B4", B_X, Y_4);
		addLocation("C4", C_X, Y_4);
		addLocation("D4", D_X, Y_4);

		// Line 5
		addLocation("A5", A_X, Y_5);
		addLocation("B5", B_X, Y_5);
		addLocation("C5", C_X, Y_5);
		addLocation("D5", D_X, Y_5);

		// Special Points : Captain Boat
		addLocation("CB1", CBKP_X1, CBKP_Y1);
		addLocation("CBU1", CBU_X1, CBU_Y1);
		addLocation("M1", GameBoard2012.BOARD_MIDDLE_WIDTH, Y_1);

		// Special Points : Message Bottle 2
		addLocation("MBU1", MBU_X1, Y_1);

		// Special Points : Message Bottle 2
		addLocation("MB2", MB_X2, MB_Y2);
		addLocation("MBU2", MBU_X2, MBU_Y2);

		// Special Points : Map Island
		addLocation("MI", MI_X, MI_Y);
		addLocation("MIU", MIU_X, MIU_Y);

		// Paths

		// Vertical lines
		addPath("A1", "A2", "A3", "A4", "A5");
		addPath("B1", "B2", "B3", "B4", "B5");
		addPath("C1", "C2", "C3", "C4", "C5");
		addPath("D1", "D2", "D3", "D4", "D5");

		// Big horizontal lines
		addPath("A1", "B1", "C1", "D1");
		addPath("A5", "B5", "C5", "D5");

		// Left horizontal lines
		addPath("A2", "B2");
		addPath("A3", "B3");
		addPath("A4", "B4");

		// Right horizontal lines
		addPath("C2", "D2");
		addPath("C3", "D3");
		addPath("C4", "D4");

		addPath("B1", "A2");

		// Message Bottle 2 trajectory
		addPath("A1", "CB1", "MB2", "MBU2", "C5");
		addPath("C2", "CBU1");

		// Captain Boat
		addPath("CBU1", "B1");
		addPath("CBU1", "M1");

		// Message Bottle 1 trajectory
		addPath("D1", "MBU1");

		// Go to MapIsland
		addPath("B1", "MI", "MIU");
		addPath("A2", "MI");
		addPath("A3", "MI");
		addPath("A4", "MI");
		addPath("B2", "MI");
		addPath("B3", "MI");
		addPath("B4", "MI");
	}

	private void buildTargets() {
		targets = new TargetList();
		SimpleTarget target = new SimpleTarget("Bullion1V", 3);
		targets.registerTarget(target);
		ITargetActionList list = target.getTargetActionList();
		ITargetAction action = addTargetAction(list, target, "StartV", "Bullion1V", 5);
		ITargetActionItemList items = action.getItems();
		addSplineRequest(items, "Bullion1V", 0x40, 0x40, toAngle(0xFC7C));

		target = new SimpleTarget("Bottle1V", 5);
		targets.registerTarget(target);
		list = target.getTargetActionList();
		action = addTargetAction(list, target, "Bullion1V", "Bottle1V", 5);
		items = action.getItems();
		addSplineRequest(items, "Bottle1V", 0xF0, 0xC0, toAngle(0x0708));
	}

	private String getLocationName(int x, int y) {
		return (char) (x + 'A') + String.valueOf(y + 3);
	}

	public TargetList getTargets() {
		return targets;
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
	public void reset() {
		super.reset();
		initConfiguration();
		buildGrid();
		buildTargets();
	}

	@Override
	public void setResourceLoader(ResourceLoader resourcesLoader) {
		this.resourcesLoader = resourcesLoader;
	}

	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}

	private double toAngle(int i) {
		if (i > 0x7FFF) {
			i = i - 0x10000;
		}
		double angle = Math.toRadians(0.1 * i);
		return angle;
	}
}
