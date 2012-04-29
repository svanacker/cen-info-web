package org.cen.cup.cup2012.gameboard;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cen.cup.cup2012.gameboard.elements.BlackCoin;
import org.cen.cup.cup2012.gameboard.elements.Bottle;
import org.cen.cup.cup2012.gameboard.elements.Bullion;
import org.cen.cup.cup2012.gameboard.elements.StartArea2012;
import org.cen.cup.cup2012.gameboard.elements.Totem;
import org.cen.cup.cup2012.gameboard.elements.WhiteCoin;
import org.cen.cup.cup2012.gameboard.lines.FollowLine2012;
import org.cen.robot.RobotUtils;
import org.cen.robot.match.Opponent;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardClickEvent;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.elements.Board;
import org.cen.ui.gameboard.elements.Border;

/**
 * Gameboard for the cup 2012.
 */
public class GameBoard2012 extends AbstractGameBoard {

	public static final double BOARD_HEIGHT = 3000d;

	public static final double BOARD_WIDTH = 2000d;

	public static final double BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2.0d;

	public static final double BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2.0d;

	public static final double BORDER_WIDTH = 22d;

	private final List<IGameBoardElement> elements;

	private final Rectangle2D gameplayBounds;

	private final Rectangle2D visibleBounds;

	/** Color of board. */
	private final Color COLOR_BOARD_2012 = Color2012.RAL_5012;

	public GameBoard2012() {
		super();
		gameplayBounds = new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		visibleBounds = new Rectangle2D.Double(-100, -100, BOARD_WIDTH + 200, BOARD_HEIGHT + 200);
		elements = new ArrayList<IGameBoardElement>();
		addElements();
	}

	private void addElements() {
		elements.add(new Board(COLOR_BOARD_2012, GameBoard2012.BOARD_WIDTH, GameBoard2012.BOARD_HEIGHT));

		elements.add(new StartArea2012(StartArea2012.PURPLE, 0d, 0d));

		elements.add(new StartArea2012(StartArea2012.RED, 0, BOARD_HEIGHT - StartArea2012.START_AREA_HEIGHT));

		// Bottom border
		elements.add(new Border("border", BOARD_WIDTH + BORDER_WIDTH * 2, -BORDER_WIDTH, -BORDER_WIDTH, 0));
		// Left border
		elements.add(new Border("border", BOARD_HEIGHT, 0d, 0d, Math.PI / 2));
		// Up border
		elements.add(new Border("border", BOARD_WIDTH + BORDER_WIDTH * 2, -BORDER_WIDTH, BOARD_HEIGHT, 0));
		// Right border
		elements.add(new Border("border", BOARD_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT, -Math.PI / 2));

		elements.add(new FollowLine2012());

		// MapIsland
		elements.add(new MapIsland(0d, BOARD_MIDDLE_HEIGHT));

		// PeanutIsland
		elements.add(new PeanutIsland(BOARD_MIDDLE_WIDTH, BOARD_MIDDLE_HEIGHT));

		// Palm Tree
		elements.add(new PalmTree(BOARD_MIDDLE_WIDTH, BOARD_MIDDLE_HEIGHT));

		// Captain Boat area (both
		elements.add(new CaptainBoatArea(0, 0));

		// Captain Ship area
		elements.add(new ShipHold(BOARD_WIDTH - ShipHold.SHIP_HOLD_WIDTH, 0));
		elements.add(new ShipHold(BOARD_WIDTH - ShipHold.SHIP_HOLD_WIDTH, BOARD_HEIGHT - ShipHold.SHIP_HOLD_HEIGHT));

		double bottleDistance1 = 640d;
		double bottleDistance2 = 640d + 477d;

		// Bottle Red
		elements.add(new Bottle("bottle1red", BOARD_WIDTH, bottleDistance2, Color2012.RAL_3001));
		// Bottle Red
		elements.add(new Bottle("bottle2red", BOARD_WIDTH, BOARD_HEIGHT - bottleDistance1, Color2012.RAL_3001));

		// Bottle Red
		elements.add(new Bottle("bottle1purple", BOARD_WIDTH, BOARD_HEIGHT - bottleDistance2, Color2012.RAL_4008));
		// Bottle Red
		elements.add(new Bottle("bottle2purple", BOARD_WIDTH, bottleDistance1, Color2012.RAL_4008));

		// totems
		elements.add(new Totem(BOARD_MIDDLE_WIDTH, 1100));
		elements.add(new Totem(BOARD_MIDDLE_WIDTH, BOARD_HEIGHT - 1100));

		// CD : fixed Coin
		// to the left
		elements.add(new WhiteCoin(500d, 1000d));
		elements.add(new WhiteCoin(500d, BOARD_HEIGHT - 1000d));
		// to the right
		elements.add(new WhiteCoin(BOARD_WIDTH - 300, 450d));
		elements.add(new WhiteCoin(BOARD_WIDTH - 300, BOARD_HEIGHT - 450d));

		// on the center, at the right
		elements.add(new BlackCoin(BOARD_WIDTH - 300, BOARD_MIDDLE_HEIGHT + 100d));
		elements.add(new BlackCoin(BOARD_WIDTH - 300, BOARD_MIDDLE_HEIGHT - 100d));

		elements.add(new WhiteCoin(BOARD_WIDTH - 200, BOARD_MIDDLE_HEIGHT));
		elements.add(new WhiteCoin(BOARD_WIDTH - 400, BOARD_MIDDLE_HEIGHT));

		// Bullion on the middle
		elements.add(new Bullion(BOARD_WIDTH - 647d, BOARD_MIDDLE_HEIGHT, Math.PI / 2));

		// Bullion aligned to the captain boat area
		double bullionShift = 420d;
		double bullionOrientation = Math.PI / 50;
		elements.add(new Bullion(StartArea2012.START_AREA_WIDTH + 285d, bullionShift, -bullionOrientation));
		elements.add(new Bullion(StartArea2012.START_AREA_WIDTH + 285d, GameBoard2012.BOARD_HEIGHT - bullionShift,
				+bullionOrientation));
	}

	private void addOpponentLocation(Point2D coordinates) {
		Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class, servicesProvider);
		opponent.addLocation(coordinates);
	}

	@Override
	public List<IGameBoardElement> getElements() {
		return elements;
	}

	@Override
	public Rectangle2D getGameplayBounds() {
		return gameplayBounds;
	}

	@Override
	public Rectangle2D getVisibleBounds() {
		return visibleBounds;
	}

	@Override
	protected boolean handleEvent(IGameBoardEvent event) {
		if (event instanceof GameBoardClickEvent) {
			GameBoardClickEvent e = (GameBoardClickEvent) event;
			// addOpponentLocation(e.getCoordinates());
			// stoppe la propagation de l'événement
			// return true;
		}
		return false;
	}
}
