package org.cen.cup.cup2011.gameboard.configuration;

import java.awt.Color;

import org.cen.geom.Point2D;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.cen.cup.cup2011.gameboard.elements.Pawn;
import org.cen.navigation.ITrajectoryService;
import org.cen.robot.match.MatchSide;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardService;

public class GameboardConfigurationHandler2011 {
	// Valeur ajoutée à la probabilité de présence lors de la détection d'un
	// pion
	private static final double VALUE_PAWN_PRESENCE = .5d;

	public static final int COLUMN_OFFSET_X = 0;

	public static final int COLUMN_OFFSET_Y = 450;

	public static final int COLUMN_SPACING_X = 350;

	public static final int COLUMN_SPACING_Y = 350;

	public static final int COLUMNS_X_COUNT = 7;

	public static final int COLUMNS_Y_COUNT = 7;

	public static final int COLUMN_OFFSET_LAST_X = COLUMN_OFFSET_X + COLUMN_SPACING_X * (COLUMNS_X_COUNT - 1);

	public static final int COLUMN_OFFSET_LAST_Y = COLUMN_OFFSET_Y + COLUMN_SPACING_Y * (COLUMNS_Y_COUNT - 1);

	// Distance maximale permettant de considérer 2 positions comme 1 seul pion
	private static final double DISTANCE_SAME_PAWN = 200;

	private static final double DISTANCE_WELL_POSITIONNED_PAWN = 150;

	public static final int POSITIONED_PAWN_MAX_WEIGHT = 10000000;

	public static final int POSITIONED_PAWN_WEIGHT = POSITIONED_PAWN_MAX_WEIGHT;

	public static final int WELL_POSITIONED_PAWN_WEIGHT = 200000000;

	public static final int DISTANCE_PAWN_DEAD_ZONE = 200;

	public static final double PAWN_RADIUS = 100d;

	private List<PawnPosition> pawnPositions = new ArrayList<PawnPosition>();

	private List<PawnPosition> dropPositions = new ArrayList<PawnPosition>();

	private IRobotServiceProvider servicesProvider;

	// drapeau indiquant si la position des pions détectés est ajustée sur les
	// positions avant match (true) où si elles sont conservées telles quelles
	// en cours de match (false)
	private boolean adjustPawnPositions = true;

	private List<Point2D> initialPositions = new ArrayList<Point2D>();

	private List<PawnPosition> lastAnalysisPawns = new ArrayList<PawnPosition>();

	boolean initialAnalysis = false;

	public GameboardConfigurationHandler2011(IRobotServiceProvider servicesProvider) {
		super();

		this.servicesProvider = servicesProvider;

		for (int x = 0; x <= COLUMNS_X_COUNT - 2; x++) {
			for (int y = 0; y <= COLUMNS_Y_COUNT - 2; y++) {
				double cx = (x + 1) * COLUMN_SPACING_X + COLUMN_OFFSET_X;
				double cy = (y + 1) * COLUMN_SPACING_Y + COLUMN_OFFSET_Y;
				Point2D p = new Point2D.Double(cx, cy);
				int i = x + y * (COLUMNS_X_COUNT - 2);
				if (i == 10 || i == 11 || i == 13 || i == 14) {
					// la ligne du milieu ne comporte que la position 12
					continue;
				}
				initialPositions.add(p);
			}
		}

		clearPawns();

		updateGameBoardView();
	}

	public void clearPawns() {
		lastAnalysisPawns.clear();
		lastAnalysisPawns.addAll(pawnPositions);

		initialAnalysis = true;
		pawnPositions.clear();

		// La position du milieu comporte toujours un pion
		double cx = (COLUMNS_X_COUNT - 1) * COLUMN_SPACING_X / 2 + COLUMN_OFFSET_X;
		double cy = (COLUMNS_Y_COUNT - 1) * COLUMN_SPACING_Y / 2 + COLUMN_OFFSET_Y;
		Point2D centralPawn = new Point2D.Double(cx, cy);
		pawnPositions.add(new PawnPosition(centralPawn));
	}

	public List<PawnPosition> getDropPositions() {
		return dropPositions;
	}

	public List<Point2D> getInitialPawnPositions() {
		return initialPositions;
	}

	public List<PawnPosition> getNearestDropPositions(Point2D position, double distance) {
		return getNearestPositions(dropPositions, position, distance);
	}

	public PawnPosition getNearestPawnPosition(Point2D position) {
		PawnPosition nearest = null;
		double distance = Double.MAX_VALUE;
		for (PawnPosition pawnPosition : pawnPositions) {
			double d = pawnPosition.getPosition().distance(position);
			if (pawnPosition.isPresent() && d < distance) {
				distance = d;
				nearest = pawnPosition;
			}
		}
		return nearest;
	}

	public List<PawnPosition> getNearestPawnPositions(Point2D position, double distance) {
		return getNearestPositions(pawnPositions, position, distance);
	}

	private List<PawnPosition> getNearestPositions(List<PawnPosition> list, Point2D position, double distance) {
		ArrayList<PawnPosition> positions = new ArrayList<PawnPosition>();
		for (PawnPosition pawnPosition : list) {
			double d = pawnPosition.getPosition().distance(position);
			if (d < distance) {
				positions.add(pawnPosition);
			}
		}
		return positions;
	}

	public PawnPosition getPawnPosition(int index) {
		return getPawnPositions().get(index);
	}

	public List<PawnPosition> getPawnPositions() {
		if (initialAnalysis) {
			return lastAnalysisPawns;
		} else {
			return pawnPositions;
		}
	}

	public MatchSide getPositionSide(Point2D position) {
		double x = position.getX();
		double y = position.getY();
		if (x < COLUMN_OFFSET_X || x > COLUMN_OFFSET_LAST_X || y < COLUMN_OFFSET_Y || y > COLUMN_OFFSET_LAST_Y) {
			return null;
		}

		/*
		 * ******************** if exactly between 4 o 2 squares return none of
		 * two Sides******************
		 */
		if (((x - COLUMN_OFFSET_X) % COLUMN_SPACING_X) == 0 || ((y - COLUMN_OFFSET_Y) % COLUMN_SPACING_Y) == 0) {
			return null;
		}
		/****************************** fin *********************************** */

		x -= COLUMN_OFFSET_X;
		x /= COLUMN_SPACING_X;

		y -= COLUMN_OFFSET_Y;
		y /= COLUMN_SPACING_Y;

		int i = (int) x + (int) y;
		switch (i % 2) {
		case 0:
			return MatchSide.RED;
		case 1:
			return MatchSide.VIOLET;
		default:
			return null;
		}
	}

	public void handlePawnDroped(PawnPosition pDropPosition) {
		pDropPosition.getConfiguration().add(PawnConfiguration.SINGLE);
		pDropPosition.adjustPresenceProbability(1.0d, 1.0d);
		updateWeights(pDropPosition.getPosition(), WELL_POSITIONED_PAWN_WEIGHT, WELL_POSITIONED_PAWN_WEIGHT);
	}

	public void handlePawnTaken(PawnPosition pPawn) {
		pawnPositions.remove(pPawn);
		updateWeights(pPawn.getPosition(), 0, 0);
	}

	public boolean isAdjustPawnPositions() {
		return adjustPawnPositions;
	}

	public void setAdjustPawnPositions(boolean adjustPawnPositions) {
		this.adjustPawnPositions = adjustPawnPositions;
	}

	public void setMatchSide(MatchSide matchSide) {
		dropPositions.clear();
		// Construit la liste des zones de dépose permettant de marquer des
		// points en fonction de la couleur
		for (int x = 0; x <= COLUMNS_X_COUNT - 2; x++) {
			for (int y = 0; y <= COLUMNS_Y_COUNT - 2; y++) {
				double cx = (0.5d + x) * COLUMN_SPACING_X + COLUMN_OFFSET_X;
				double cy = (0.5d + y) * COLUMN_SPACING_Y + COLUMN_OFFSET_Y;
				Point2D p = new Point2D.Double(cx, cy);
				if (getPositionSide(p) == matchSide) {
					int i = x + y * (COLUMNS_X_COUNT - 1);
					Set<PawnConfiguration> configuration;
					if (i == 7 || i == 9 || i == 17 || i == 23 || i == 25 || i == 27) {
						configuration = EnumSet.of(PawnConfiguration.BONUS);
					} else if (i == 5 || i == 11 || i == 29 || i == 35) {
						configuration = EnumSet.of(PawnConfiguration.PROTECTED);
					} else {
						configuration = EnumSet.noneOf(PawnConfiguration.class);
					}
					PawnPosition position = new PawnPosition(p, configuration);
					dropPositions.add(position);
				}
			}
		}
		// updateGreenZoneWeight();
	}

	public void updateGameBoardView() {
		if (servicesProvider == null) {
			return;
		}

		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		if (gameBoard == null) {
			return;
		}

		gameBoard.removeElements(Pawn.NAME);
		List<IGameBoardElement> elements = gameBoard.getElements();
		for (PawnPosition p : getPawnPositions()) {
			Pawn pawn = new Pawn(p.getPosition(), p.getPresenceProbability());
			elements.add(pawn);
		}
		for (PawnPosition p : dropPositions) {
			if (p.isPresent()) {
				Pawn pawn = new Pawn(p.getPosition(), p.getPresenceProbability(), Color.GREEN);
				elements.add(pawn);
			}
		}
	}

	private void updateGreenZoneWeight() {
		for (int x = 2; x < COLUMNS_X_COUNT - 2; x++) {
			double cx = (0.5d + x) * COLUMN_SPACING_X + COLUMN_OFFSET_X;
			double cy = (COLUMNS_Y_COUNT - 1) * COLUMN_SPACING_Y + DISTANCE_PAWN_DEAD_ZONE + COLUMN_OFFSET_Y;
			Point2D p = new Point2D.Double(cx, cy);
			updateWeights(p, WELL_POSITIONED_PAWN_WEIGHT, WELL_POSITIONED_PAWN_WEIGHT);

			cy = -DISTANCE_PAWN_DEAD_ZONE + COLUMN_OFFSET_Y;
			p = new Point2D.Double(cx, cy);
			updateWeights(p, WELL_POSITIONED_PAWN_WEIGHT, WELL_POSITIONED_PAWN_WEIGHT);
		}
	}

	private void updateInitialPawnPositions(Point2D point) {
		// Recherche la position initiale la plus proche de la position
		// spécifiée
		Point2D nearest = point;
		double distance = Double.MAX_VALUE;
		for (Point2D p : initialPositions) {
			double d = p.distance(point);
			if (d < distance) {
				distance = d;
				nearest = p;
			}
		}
		updateMatchPawnPosition(nearest);
	}

	public void updateMatchPawnPosition(Point2D point) {
		int weight = POSITIONED_PAWN_WEIGHT;
		PawnPosition p = getNearestPawnPosition(point);
		// * ****************** test si un pion est sur une drop position et
		// ajuste la proba **************/
		List<PawnPosition> nearestDropPositions = getNearestDropPositions(point, DISTANCE_WELL_POSITIONNED_PAWN);
		PawnPosition dropPosition = null;
		if (!nearestDropPositions.isEmpty()) {
			dropPosition = nearestDropPositions.get(0);
		}
		/* ***************************************************************************** */

		if (p != null && p.getPosition().distance(point) < DISTANCE_SAME_PAWN) {
			// augmente la probabilité de présence
			p.adjustPresenceProbability(1d, VALUE_PAWN_PRESENCE);
			point = p.getPosition();
		}
		/*
		 * ****************** test si un pion est sur une drop position et
		 * ajuste la proba *************
		 */
		else if (dropPosition != null) {
			if (!dropPosition.isPresent()) {
				dropPosition.getConfiguration().add(PawnConfiguration.SINGLE);
			}
			dropPosition.adjustPresenceProbability(1d, VALUE_PAWN_PRESENCE);
		}
		/*
		 * ******************************** fin
		 * ****************************************************
		 */

		else {
			pawnPositions.add(new PawnPosition(point));
		}

		updateWeights(point, weight, weight);
		updateGameBoardView();
	}

	public void updatePawnPosition(Point2D point) {
		if (adjustPawnPositions) {
			updateInitialPawnPositions(point);
		} else {
			updateMatchPawnPosition(point);
		}
	}

	private void updateWeights(Point2D point, int pWeight, int pMaxWeight) {
		// Mise à jour des poids de la carte de navigation
		if (servicesProvider != null) {
			ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
			trajectoryService.getNavigationMap().updateWeights(point, DISTANCE_PAWN_DEAD_ZONE, pWeight, pMaxWeight);
		}
	}

	public void setAnalysisTerminated() {
		initialAnalysis = false;
		lastAnalysisPawns.clear();
	}
}
