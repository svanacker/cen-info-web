package org.cen.cup.cup2009.gameboard;

import static org.cen.cup.cup2009.gameboard.elements.BeaconSupport.BEACON_HEIGHT;

import java.awt.Color;
import org.cen.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cen.cup.cup2009.gameboard.elements.BeaconSupport;
import org.cen.cup.cup2009.gameboard.elements.Board;
import org.cen.cup.cup2009.gameboard.elements.Border;
import org.cen.cup.cup2009.gameboard.elements.CentralConstructionArea;
import org.cen.cup.cup2009.gameboard.elements.ColumnElement;
import org.cen.cup.cup2009.gameboard.elements.ColumnElementPosition;
import org.cen.cup.cup2009.gameboard.elements.ConstructionArea;
import org.cen.cup.cup2009.gameboard.elements.Helper;
import org.cen.cup.cup2009.gameboard.elements.StartArea;
import org.cen.cup.cup2009.gameboard.elements.VerticalDispenser;
import org.cen.cup.cup2009.gameboard.elements.ColumnElement.ColumnElementColor;
import org.cen.cup.cup2009.gameboard.elements.StartArea.StartAreaColor;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardElementsComparator;
import org.cen.ui.gameboard.IGameBoardElement;

public class GameBoard2009 extends AbstractGameBoard {
	public static final int BOARD_HEIGHT = 3000;

	public static final int BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2;

	public static final int BOARD_WIDTH = 2100;

	public static final int BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2;

	public static final int COLUMN_OFFSET_X = 575;

	public static final int COLUMN_OFFSET_Y = 600;

	public static final int COLUMN_SPACING_X = 200;

	public static final int COLUMN_SPACING_Y = 250;

	public static final int COLUMNS_X_COUNT = 4;

	public static final int COLUMNS_Y_COUNT = 3;

	public static final int COLUMN_OFFSET_LAST_X = COLUMN_OFFSET_X + COLUMN_SPACING_X * (COLUMNS_X_COUNT - 1);

	public static final int COLUMN_OFFSET_LAST_Y = COLUMN_OFFSET_Y + COLUMN_SPACING_Y * (COLUMNS_Y_COUNT - 1);;

	public static final int BORDER_WIDTH = 22;

	private ArrayList<IGameBoardElement> elements;

	private Rectangle2D gameplayBounds;

	private Rectangle2D visibleBounds;

	public GameBoard2009() {
		super();
		gameplayBounds = new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		visibleBounds = new Rectangle2D.Double(-80, -272, 2260, 3544);
		elements = new ArrayList<IGameBoardElement>();
		addElements();
	}

	private void addElements() {
		elements.add(new Board(new Point2D.Double(0, 0)));
		elements.add(new StartArea(StartAreaColor.GREEN, new Point2D.Double(0, 0)));
		elements.add(new StartArea(StartAreaColor.RED, new Point2D.Double(0, BOARD_HEIGHT - StartArea.START_AREA_HEIGHT)));
		elements.add(new Border("border", 2132, new Point2D.Double(-BORDER_WIDTH, -BORDER_WIDTH), 0));
		elements.add(new Border("border", 2132, new Point2D.Double(-BORDER_WIDTH, BOARD_HEIGHT), 0));
		elements.add(new Border("border", BOARD_HEIGHT, new Point2D.Double(-BORDER_WIDTH, BOARD_HEIGHT), -Math.PI / 2));
		elements.add(new Border("plexiglass border", BOARD_HEIGHT, 10, Color.CYAN, new Point2D.Double(BOARD_WIDTH, BOARD_HEIGHT), -Math.PI / 2));
		elements.add(new CentralConstructionArea("central construction area", new Point2D.Double(BOARD_WIDTH / 2, BOARD_HEIGHT / 2)));
		elements.add(new BeaconSupport("green beacon 1", new Point2D.Double(BOARD_WIDTH / 2, -BEACON_HEIGHT / 2 - BORDER_WIDTH)));
		elements.add(new BeaconSupport("red beacon 1", new Point2D.Double(BOARD_WIDTH / 2, BOARD_HEIGHT + BEACON_HEIGHT / 2 + BORDER_WIDTH)));
		elements.add(new BeaconSupport("green beacon 2", new Point2D.Double(-BeaconSupport.BEACON_HEIGHT / 2 - BORDER_WIDTH, BOARD_HEIGHT + BEACON_HEIGHT / 2 + BORDER_WIDTH)));
		elements.add(new BeaconSupport("red beacon 2", new Point2D.Double(-BEACON_HEIGHT / 2 - BORDER_WIDTH, -BEACON_HEIGHT / 2 - BORDER_WIDTH)));
		elements.add(new BeaconSupport("green beacon 3", new Point2D.Double(2150, BOARD_HEIGHT + BEACON_HEIGHT / 2 + BORDER_WIDTH)));
		elements.add(new BeaconSupport("red beacon 3", new Point2D.Double(2150, -BEACON_HEIGHT / 2 - BORDER_WIDTH)));
		int i = 1;
		// position des �l�ments de colonnes
		for (double x = COLUMN_OFFSET_X; x <= COLUMN_OFFSET_LAST_X; x += COLUMN_SPACING_X) {
			for (double y = COLUMN_OFFSET_Y; y <= COLUMN_OFFSET_LAST_Y; y += COLUMN_SPACING_Y) {
				elements.add(new ColumnElementPosition(i, ColumnElementColor.NEUTRAL, new Point2D.Double(x, y)));
				elements.add(new ColumnElementPosition(i++, ColumnElementColor.NEUTRAL, new Point2D.Double(x, BOARD_HEIGHT - y)));
			}
		}
		// rep�res
		for (double o = 705d; o <= 1905d; o += 600d) {
			for (double y = 0d; y <= 390d; y += 130d) {
				elements.add(new Helper(new Point2D.Double(1875d, y + o)));
			}
		}
		// Autres rep�res
		for (double y = 900d; y <= GameBoard2009.BOARD_WIDTH; y += 400d) {
			elements.add(new Helper(new Point2D.Double(125d, y)));
		}

		// Zone de construction
		elements.add(new ConstructionArea("zone 1 green", new Point2D.Double(2050, 900)));
		elements.add(new ConstructionArea("zone 1 red", new Point2D.Double(2050, GameBoard2009.BOARD_WIDTH)));
		elements.add(new ConstructionArea("zone 2", new Point2D.Double(2050, BOARD_HEIGHT / 2)));

		// 
		elements.add(new Border("red construction area border", 100, BORDER_WIDTH, Color.WHITE, new Point2D.Double(GameBoard2009.BOARD_WIDTH, 2422), Math.PI));
		elements.add(new Border("green construction area border", 100, BORDER_WIDTH, Color.WHITE, new Point2D.Double(2000, 578), 0));
		elements.add(new VerticalDispenser("dispenser position 12/10", new Point2D.Double(800, 40)));
		elements.add(new VerticalDispenser("dispenser position 12/10", new Point2D.Double(800, 2960)));
		elements.add(new VerticalDispenser("dispenser position 11/10", new Point2D.Double(1300, 40)));
		elements.add(new VerticalDispenser("dispenser position 11/10", new Point2D.Double(1300, 2960)));
		elements.add(new VerticalDispenser("dispenser", new Point2D.Double(2060, 289)));
		elements.add(new VerticalDispenser("dispenser", new Point2D.Double(2060, 2711)));
		elements.add(new ColumnElement("green dispenser", ColumnElementColor.RED, new Point2D.Double(2060, 289)));
		elements.add(new ColumnElement("red dispenser", ColumnElementColor.GREEN, new Point2D.Double(2060, 2711)));

		Collections.sort(elements, new GameBoardElementsComparator());
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
}
