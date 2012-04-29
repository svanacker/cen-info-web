package org.cen.cup.cup2011.gameboard;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cen.cup.cup2011.gameboard.elements.Circle;
import org.cen.cup.cup2011.gameboard.elements.Paint;
import org.cen.cup.cup2011.gameboard.elements.Pawn;
import org.cen.cup.cup2011.gameboard.elements.Rectangle;
import org.cen.cup.cup2011.gameboard.elements.StartArea2011;
import org.cen.robot.RobotUtils;
import org.cen.robot.match.Opponent;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardClickEvent;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.elements.Board;
import org.cen.ui.gameboard.elements.Border;

/**
 * Gameboard for the cup 2011.
 * 
 * @author Emmanuel ZURMELY / OLQ
 */
public class GameBoard2011 extends AbstractGameBoard {

	public static final int BOARD_HEIGHT = 3000;

	public static final int BOARD_WIDTH = 2100;

	public static final int BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2;

	public static final int BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2;

	public static final int BORDER_WIDTH = 22;

	public static final int BAND_WIDTH = 50;

	public static final int BOX_SIZE = 350;

	public static final int RADIUS_SIZE = 50;

	public static final int ZONE_WIDTH = 120;

	public static final int ZONE_HEIGHT = 130;

	public static final int GREEN_ZONE_HEIGHT = 500;

	public static final int DELIVERY_WIDTH = BOARD_WIDTH
			- (StartArea2011.START_AREA_WIDTH + BORDER_WIDTH);

	private int position1;

	private int position2;

	private int random;

	private final List<IGameBoardElement> elements;

	private final Rectangle2D gameplayBounds;

	private final Rectangle2D visibleBounds;

	public GameBoard2011() {
		super();
		gameplayBounds = new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		visibleBounds = new Rectangle2D.Double(-100, -100, BOARD_WIDTH + 200,
				BOARD_HEIGHT + 200);
		elements = new ArrayList<IGameBoardElement>();
		addElements();
	}

	private void addElements() {
		elements.add(new Board(Color.RED, GameBoard2011.BOARD_WIDTH,
				GameBoard2011.BOARD_HEIGHT));

		elements.add(new StartArea2011(Color.RED, new Point2D.Double(0, 0)));
		elements.add(new StartArea2011(Color.BLUE, new Point2D.Double(0,
				BOARD_HEIGHT - StartArea2011.START_AREA_HEIGHT)));
		// Bord Bas
		elements.add(new Border("border", BOARD_WIDTH + BORDER_WIDTH * 2,
				new Point2D.Double(-BORDER_WIDTH, -BORDER_WIDTH), 0));
		// Bord Gauche
		elements.add(new Border("border", BOARD_HEIGHT,
				new Point2D.Double(0, 0), Math.PI / 2));
		// Bord Haut
		elements.add(new Border("border", BOARD_WIDTH + BORDER_WIDTH * 2,
				new Point2D.Double(-BORDER_WIDTH, BOARD_HEIGHT), 0));
		// Bord Droit
		elements.add(new Border("border", BOARD_HEIGHT, new Point2D.Double(
				BOARD_WIDTH, BOARD_HEIGHT), -Math.PI / 2));

		// Separation Bas
		elements.add(new Border("border", StartArea2011.START_AREA_HEIGHT,
				new Point2D.Double(StartArea2011.START_AREA_WIDTH
						+ BORDER_WIDTH, 0), Math.PI / 2));
		// Separation Haut
		elements.add(new Border(
				"border",
				StartArea2011.START_AREA_HEIGHT,
				new Point2D.Double(StartArea2011.START_AREA_WIDTH, BOARD_HEIGHT),
				-Math.PI / 2));

		// Bande Bas
		elements.add(new Rectangle("bande_bas", BOARD_WIDTH, BAND_WIDTH,
				Color.BLACK, new Point2D.Double(0,
						StartArea2011.START_AREA_HEIGHT), 0));
		// Bande Haut
		elements.add(new Rectangle("bande_haut", BOARD_WIDTH, BAND_WIDTH,
				Color.BLACK, new Point2D.Double(0, BOARD_HEIGHT
						- StartArea2011.START_AREA_HEIGHT - BAND_WIDTH), 0));

		// Distribution Bas
		elements.add(new Rectangle("distribution_bas", DELIVERY_WIDTH,
				StartArea2011.START_AREA_HEIGHT, Paint.RAL_6018,
				new Point2D.Double(StartArea2011.START_AREA_WIDTH
						+ BORDER_WIDTH, 0), 0));
		// Distribution Haut
		elements.add(new Rectangle("distribution_haut", DELIVERY_WIDTH,
				StartArea2011.START_AREA_HEIGHT, Paint.RAL_6018,
				new Point2D.Double(StartArea2011.START_AREA_WIDTH
						+ BORDER_WIDTH, BOARD_HEIGHT
						- StartArea2011.START_AREA_HEIGHT), 0));

		// Damier
		for (int j = 0; j < 6; j++) {
			for (int i = 0; i < 3; i++) {
				elements.add(new Rectangle("Case", BOX_SIZE, BOX_SIZE,
						Color.BLUE, new Point2D.Double(BOX_SIZE * (j % 2)
								+ BOX_SIZE * 2 * i,
								StartArea2011.START_AREA_HEIGHT + BAND_WIDTH
										+ BOX_SIZE * j), 0));
				// Case Bonus Bleu
				if (((j == 1) && (i < 2)) || ((j == 3) && (i == 2))) {
					elements.add(new Circle("Bonus", RADIUS_SIZE, Color.BLACK,
							new Point2D.Double(BOX_SIZE * (j % 2) + BOX_SIZE
									* 2 * i + BOX_SIZE / 2,
									StartArea2011.START_AREA_HEIGHT
											+ BAND_WIDTH + BOX_SIZE * j
											+ BOX_SIZE / 2)));

				}
				// Case Bonus Rouge
				if (((j == 2) && (i == 2)) || ((j == 4) && (i < 2))) {
					elements.add(new Circle("Bonus", RADIUS_SIZE, Color.BLACK,
							new Point2D.Double(BOX_SIZE * (j % 2) + BOX_SIZE
									* 2 * i + BOX_SIZE * 1.5,
									StartArea2011.START_AREA_HEIGHT
											+ BAND_WIDTH + BOX_SIZE * j
											+ BOX_SIZE / 2)));
				}
			}
		}

		// Zone protégée
		// Bas
		elements.add(new Border("ligne_bas", BOX_SIZE * 2, new Point2D.Double(
				BOARD_WIDTH - BOX_SIZE + BORDER_WIDTH,
				StartArea2011.START_AREA_HEIGHT + BAND_WIDTH), Math.PI / 2));
		elements.add(new Rectangle("zone_bas", ZONE_WIDTH, BOX_SIZE * 2,
				Color.BLACK, new Point2D.Double(BOARD_WIDTH - ZONE_WIDTH,
						StartArea2011.START_AREA_HEIGHT + BAND_WIDTH), 0));

		elements.add(new Rectangle("zone_bas_gauche", ZONE_HEIGHT,
				BORDER_WIDTH, Color.BLACK, new Point2D.Double(BOARD_WIDTH
						- ZONE_WIDTH - ZONE_HEIGHT,
						StartArea2011.START_AREA_HEIGHT + BAND_WIDTH), 0));
		elements.add(new Rectangle("zone_bas_droite", ZONE_HEIGHT,
				BORDER_WIDTH, Color.BLACK, new Point2D.Double(BOARD_WIDTH
						- ZONE_WIDTH - ZONE_HEIGHT,
						StartArea2011.START_AREA_HEIGHT + BAND_WIDTH + BOX_SIZE
								* 2 - BORDER_WIDTH), 0));

		// Haut
		elements.add(new Border("ligne_haut", BOX_SIZE * 2, new Point2D.Double(
				BOARD_WIDTH - BOX_SIZE, BOARD_HEIGHT
						- (StartArea2011.START_AREA_HEIGHT + BAND_WIDTH)),
				-Math.PI / 2));
		elements.add(new Rectangle("zone_haut", ZONE_WIDTH, BOX_SIZE * 2,
				Color.BLACK, new Point2D.Double(BOARD_WIDTH, BOARD_HEIGHT
						- (StartArea2011.START_AREA_HEIGHT + BAND_WIDTH)),
				-Math.PI));

		elements.add(new Rectangle(
				"zone_haut_droite",
				ZONE_HEIGHT,
				BORDER_WIDTH,
				Color.BLACK,
				new Point2D.Double(
						BOARD_WIDTH - ZONE_WIDTH - ZONE_HEIGHT,
						BOARD_HEIGHT
								- (StartArea2011.START_AREA_HEIGHT + BAND_WIDTH + BORDER_WIDTH)),
				0));
		elements.add(new Rectangle(
				"zone_haut_gauche",
				ZONE_HEIGHT,
				BORDER_WIDTH,
				Color.BLACK,
				new Point2D.Double(
						BOARD_WIDTH - ZONE_WIDTH - ZONE_HEIGHT,
						BOARD_HEIGHT
								- (StartArea2011.START_AREA_HEIGHT + BAND_WIDTH + BOX_SIZE * 2)),
				0));

		if (false) {
			// Pions
			// pions central
			elements.add(new Pawn(
					new Point2D.Double(BOX_SIZE * 3,
							StartArea2011.START_AREA_WIDTH + BAND_WIDTH
									+ BOX_SIZE * 3), 1d));

			// Au hasard
			for (int i = 1; i < 6; i++) {
				if (i != 3) {
					position1 = (int) (Math.random() * 4) + 1;
					position2 = (int) (Math.random() * 4) + 1;
					if (position1 == position2) {
						random = (int) (Math.random()) * 2;
						position2 = position2 + random - 1;
						if (position2 == 0) {
							position2 = 5;
						}
					}
					elements.add(new Pawn(new Point2D.Double(BOX_SIZE
							* position1, StartArea2011.START_AREA_WIDTH
							+ BAND_WIDTH + BOX_SIZE * i), 1d));
					elements.add(new Pawn(new Point2D.Double(BOX_SIZE
							* position2, StartArea2011.START_AREA_WIDTH
							+ BAND_WIDTH + BOX_SIZE * i), 1d));
				}
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
			// addOpponentLocation(e.getCoordinates());
			// stoppe la propagation de l'événement
			// return true;
		}
		return false;
	}
}
