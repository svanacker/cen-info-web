package org.cen.cup.cup2010.gameboard;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.cen.cup.cup2010.gameboard.elements.Board;
import org.cen.cup.cup2010.gameboard.elements.Border;
import org.cen.cup.cup2010.gameboard.elements.Corn;
import org.cen.cup.cup2010.gameboard.elements.Hill;
import org.cen.cup.cup2010.gameboard.elements.Orange;
import org.cen.cup.cup2010.gameboard.elements.StartArea;
import org.cen.cup.cup2010.gameboard.elements.StartArea.StartAreaColor;
import org.cen.cup.cup2010.gameboard.elements.Tomato;
import org.cen.robot.RobotUtils;
import org.cen.robot.match.Opponent;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardClickEvent;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;

/**
 * Gameboard for the cup 2010.
 * 
 * @author Emmanuel ZURMELY
 */
public class GameBoard2010 extends AbstractGameBoard {
	public static final int BOARD_HEIGHT = 3000;

	public static final int BOARD_WIDTH = 2122;

	public static final int BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2;

	public static final int BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2;

	public static final int BORDER_WIDTH = 22;

	public static final int CORN_SPACING_X = 250;

	public static final int CORN_SPACING_Y = 450;

	public static final int CORN_START_X = 722;

	public static final int CORN_START_Y = 150;

	public static final int CONTAINER_Y_FROM_LAST_CORN = 200;

	private ArrayList<IGameBoardElement> elements;

	private Rectangle2D gameplayBounds;

	private Rectangle2D visibleBounds;

	public GameBoard2010() {
		super();
		gameplayBounds = new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		visibleBounds = new Rectangle2D.Double(-100, -100, BOARD_WIDTH + 200,
				BOARD_HEIGHT + 200);
		elements = new ArrayList<IGameBoardElement>();
		addElements();
	}

	private void addElements() {
		elements.add(new Board(new Point2D.Double(0, 0)));
		elements.add(new StartArea(StartAreaColor.BLUE,
				new Point2D.Double(0, 0)));
		elements.add(new StartArea(StartAreaColor.YELLOW, new Point2D.Double(0,
				BOARD_HEIGHT - StartArea.START_AREA_HEIGHT)));
		elements.add(new Border("border", BOARD_WIDTH + BORDER_WIDTH,
				new Point2D.Double(-BORDER_WIDTH, -BORDER_WIDTH), 0));
		elements.add(new Border("border", BOARD_WIDTH + BORDER_WIDTH,
				new Point2D.Double(-BORDER_WIDTH, BOARD_HEIGHT), 0));
		elements.add(new Border("border", BOARD_HEIGHT, new Point2D.Double(
				-BORDER_WIDTH, BOARD_HEIGHT), -Math.PI / 2));
		elements.add(new Border("border", 2000, new Point2D.Double(BOARD_WIDTH,
				500), Math.PI / 2));
		elements.add(new Hill("hill", new Point2D.Double(0, 670)));
		elements.add(new Border("hill border", 1519, new Point2D.Double(
				500 + BORDER_WIDTH, 740), Math.PI / 2));

		int cornId = 1, tomatoId = 1;
		for (int y = CORN_START_Y; y <= BOARD_MIDDLE_HEIGHT; y += CORN_SPACING_Y) {
			boolean corn = (y != BOARD_MIDDLE_HEIGHT);
			for (int x = CORN_START_X; x < BOARD_WIDTH; x += CORN_SPACING_X) {
				if ((x > (CORN_START_X + CORN_SPACING_X * 4) && y == CORN_START_Y)
						|| (x == CORN_START_X && y > CORN_START_Y)
						|| (x == (CORN_START_X + CORN_SPACING_X) && y > (CORN_START_Y + CORN_SPACING_Y))) {
					// first row, no elements for x > 1722
					// second row and following, no elements for x = 722
					// third row and following, no elements for x = 972
					continue;
				}
				if (corn) {
					elements.add(new Corn("corn" + cornId, new Point(x, y),
							false));
					if (y < BOARD_MIDDLE_HEIGHT) {
						elements.add(new Corn("corn" + cornId, new Point(x,
								BOARD_HEIGHT - y), false));
					}
					cornId++;
				} else {
					elements.add(new Tomato("tomato" + tomatoId,
							new Point(x, y)));
					if (y < BOARD_MIDDLE_HEIGHT) {
						elements.add(new Tomato("tomato" + tomatoId, new Point(
								x, BOARD_HEIGHT - y)));
					}
					tomatoId++;
				}
				corn = !corn;
			}
		}

		// Oranges
		// Position des trois branches
		int[] ox = { 70, 145, 195 };
		int[] oy = { 1320, 1375, 1295 };
		int orange = 1;
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < ox.length; i++) {
				int x = ox[i];
				int y = oy[i];
				if (j % 2 == 0) {
					// Décalage sur x
					x += 225;
				}
				if (j > 1) {
					// Symétrique sur y
					y = BOARD_HEIGHT - y;
				}
				elements.add(new Orange("orange" + orange, new Point(x, y)));
				orange++;
			}
		}
	}

	private void addOpponentLocation(Point2D coordinates) {
		Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class,
				servicesProvider);
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
			addOpponentLocation(e.getCoordinates());
			// stoppe la propagation de l'événement
			// return true;
		}
		return false;
	}
}
