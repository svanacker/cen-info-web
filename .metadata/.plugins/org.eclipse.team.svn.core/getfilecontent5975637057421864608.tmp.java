package org.cen.cup.cup2012.navigation;

import java.io.InputStream;
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
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Navigation map for the cup 2012.
 */
public class NavigationMap2012 extends AbstractNavigationMap implements ResourceLoaderAware {

	public static final String SUFFIX_VIOLET = "V";

	public static final String SUFFIX_RED = "R";

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

	public static String getOppositeName(String name) {
		if (name.endsWith(SUFFIX_RED)) {
			name = name.substring(0, name.length() - SUFFIX_RED.length());
			name += SUFFIX_VIOLET;
		} else if (name.endsWith(SUFFIX_VIOLET)) {
			name = name.substring(0, name.length() - SUFFIX_VIOLET.length());
			name += SUFFIX_RED;
		}
		return name;
	}

	public static String getRedName(String name) {
		return name + SUFFIX_RED;
	}

	public static double getSymmetricAngle(double angle) {
		return -angle;
	}

	public static String getVioletName(String name) {
		return name + SUFFIX_VIOLET;
	}

	public static double toAngle(int i) {
		if (i > 0x7FFF) {
			i -= 0x10000;
		}
		double angle = Math.toRadians(0.1 * i);
		return angle;
	}

	private ResourceLoader resourcesLoader;

	private String resourcesPath;

	private Properties robotProperties;

	private void addPath(int x1, int y1, int x2, int y2) {
		String start = getLocationName(x1, y1);
		String end = getLocationName(x2, y2);
		addPath(start, end);
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

	public void addSymmetricSplinePath(String location1, String location2, int cpDistance1, int cpDistance2, int angle1, int angle2) {
		double a1 = toAngle(angle1);
		double a2 = toAngle(angle2);

		addSplinePath(location1 + SUFFIX_RED, location2 + SUFFIX_RED, cpDistance1, cpDistance2, getSymmetricAngle(a1), getSymmetricAngle(a2));
		addSplinePath(location1 + SUFFIX_VIOLET, location2 + SUFFIX_VIOLET, cpDistance1, cpDistance2, a1, a2);
	}

	private void buildGrid() {
		addSymmetricLocation("Start", 0, 0);
		addSymmetricLocation("Bullion1", 0x0370, 0x0156);
		addSymmetricLocation("Bottle1", 0x0720, 0x0280);
		addSymmetricLocation("Bottle2", 0x07AF, 0x076C);
		addSymmetricLocation("B2Front", 0x05DC, 0x0800);
		addSymmetricLocation("BullionRight", 0x0560, 0x0580);
		addSymmetricLocation("Drop1", 0x0402, 0x0115);
		addSymmetricLocation("OutDrop1", 0x0315, 0x01A2);
		addSymmetricLocation("AfterBullionLeft1", 0x025C, 0x055C);
		addSymmetricLocation("Home", 0x00F5, 0x016F);
		addSymmetricLocation("HomeFront1", 0x0208, 0x02C2);
		addSymmetricLocation("AfterBullionLeft2", 0x0282, 0x083C);
		addSymmetricLocation("CDFixedRed", 0x06A8, 0x05D5);

		addSymmetricSplinePath("Start", "Bullion1", 0x40, 0x40, 675, 0xFC7C);
		addSymmetricSplinePath("Bullion1", "Bottle1", 0xF0, 0xC0, 0xFC7C, 0x0708);
		addSymmetricSplinePath("Bottle1", "B2Front", 0x57, 0x0A, 0x0708, 0x0384);
		addSymmetricSplinePath("B2Front", "Bottle2", 0xD8, 0xF8, 0x0384, 0xF8F8);
		addSymmetricSplinePath("Bottle2", "BullionRight", 0x1E, 0x1E, 0xF8F8, 0xFC7C);
		addSymmetricSplinePath("BullionRight", "Drop1", 0x64, 0x32, 0xFC7C, 0xFC7C);
		addSymmetricSplinePath("Drop1", "OutDrop1", 0xD9, 0xEC, 0xFC7C, 0x04BA);
		addSymmetricSplinePath("HomeFront1", "AfterBullionLeft2", 0x1E, 0x78, 0x0384, 0x0384);
	}

	@Override
	public void addSplinePath(String location1, String location2, int cpDistance1, int cpDistance2, double angle1, double angle2) {
		cpDistance1 = toDistance(cpDistance1);
		cpDistance2 = toDistance(cpDistance2);
		super.addSplinePath(location1, location2, cpDistance1, cpDistance2, angle1, angle2);
	}

	public static int toDistance(int i) {
		if (i > 0x7F) {
			i -= 0x100;
		}
		return i;
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
