package org.cen.cup.cup2008.gameboard;

import org.cen.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cen.cup.cup2008.gameboard.elements.Ball;
import org.cen.cup.cup2008.gameboard.elements.BallPosition;
import org.cen.cup.cup2008.gameboard.elements.Beacon;
import org.cen.cup.cup2008.gameboard.elements.Board;
import org.cen.cup.cup2008.gameboard.elements.Border;
import org.cen.cup.cup2008.gameboard.elements.FreezingContainer;
import org.cen.cup.cup2008.gameboard.elements.HorizontalDispenser;
import org.cen.cup.cup2008.gameboard.elements.StandardContainer;
import org.cen.cup.cup2008.gameboard.elements.StartArea;
import org.cen.cup.cup2008.gameboard.elements.VerticalDispenser;
import org.cen.cup.cup2008.gameboard.elements.Ball.BallColor;
import org.cen.cup.cup2008.gameboard.elements.StartArea.StartAreaColor;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardElementsComparator;
import org.cen.ui.gameboard.IGameBoardElement;

public class GameBoard extends AbstractGameBoard {
	private ArrayList<IGameBoardElement> elements;

	private Rectangle2D gameplayBounds;

	private Rectangle2D visibleBounds;

	public GameBoard() {
		super();
		gameplayBounds = new Rectangle2D.Double(0, 0, 2100, 3000);
		visibleBounds = new Rectangle2D.Double(-80, -272, 2260, 3544);
		elements = new ArrayList<IGameBoardElement>();
		addElements();
	}

	private void addElements() {
		elements.add(new Board(new Point2D.Double(0, 0)));
		elements.add(new StartArea(StartAreaColor.RED, new Point2D.Double(0, 0)));
		elements.add(new StartArea(StartAreaColor.BLUE, new Point2D.Double(0, 2500)));
		elements.add(new Ball("red", BallColor.RED, new Point2D.Double(36, 700)));
		elements.add(new Ball("white", BallColor.WHITE, new Point2D.Double(750, 36)));
		elements.add(new Ball("blue", BallColor.BLUE, new Point2D.Double(36, 2300)));
		elements.add(new Ball("white", BallColor.WHITE, new Point2D.Double(750, 2964)));
		elements.add(new Border("border", 2144, new Point2D.Double(-22, -22), 0));
		elements.add(new Border("border", 2144, new Point2D.Double(-22, 3000), 0));
		elements.add(new Border("border", 3000, new Point2D.Double(-22, 3000), -Math.PI / 2));
		elements.add(new Border("border", 3000, new Point2D.Double(2100, 3000), -Math.PI / 2));
		elements.add(new Beacon("red beacon 1", new Point2D.Double(1050, -22)));
		elements.add(new Beacon("blue beacon 1", new Point2D.Double(1050, 3022)));
		elements.add(new Beacon("red beacon 2", new Point2D.Double(-62, 3062)));
		elements.add(new Beacon("blue beacon 2", new Point2D.Double(-62, -62)));
		elements.add(new Beacon("red beacon 3", new Point2D.Double(2162, 3062)));
		elements.add(new Beacon("blue beacon 3", new Point2D.Double(2162, -62)));
		elements.add(new Beacon("red beacon 4", new Point2D.Double(1582, 3062)));
		elements.add(new Beacon("blue beacon 4", new Point2D.Double(1582, -62)));
		elements.add(new FreezingContainer("red container", new Point2D.Double(1872, 3022), 0));
		elements.add(new FreezingContainer("blue container", new Point2D.Double(1872, -22), Math.PI));
		elements.add(new StandardContainer("container", new Point2D.Double(2122, -22)));
		elements.add(new HorizontalDispenser("horizontal dispenser", new Point2D.Double(-102, 1045)));
		elements.add(new VerticalDispenser("vertical red dispenser", new Point2D.Double(0, 700), 0));
		elements.add(new VerticalDispenser("vertical blue dispenser", new Point2D.Double(0, 2300), 0));
		elements.add(new VerticalDispenser("vertical white dispenser red side", new Point2D.Double(750, 0), Math.PI / 2));
		elements.add(new VerticalDispenser("vertical white dispenser blue side", new Point2D.Double(750, 3000), -Math.PI / 2));
		elements.add(new BallPosition("blue position 1", BallColor.BLUE, new Point2D.Double(200, 2200)));
		elements.add(new BallPosition("blue position 2", BallColor.BLUE, new Point2D.Double(200, 1700)));
		elements.add(new BallPosition("blue position 3", BallColor.BLUE, new Point2D.Double(400, 2200)));
		elements.add(new BallPosition("blue position 4", BallColor.BLUE, new Point2D.Double(400, 1700)));
		elements.add(new BallPosition("red position 1", BallColor.RED, new Point2D.Double(200, 800)));
		elements.add(new BallPosition("red position 2", BallColor.RED, new Point2D.Double(200, 1300)));
		elements.add(new BallPosition("red position 3", BallColor.RED, new Point2D.Double(400, 800)));
		elements.add(new BallPosition("red position 4", BallColor.RED, new Point2D.Double(400, 1300)));
		elements.add(new BallPosition("white position 1", BallColor.WHITE, new Point2D.Double(600, 800)));
		elements.add(new BallPosition("white position 2", BallColor.WHITE, new Point2D.Double(600, 1300)));
		elements.add(new BallPosition("white position 3", BallColor.WHITE, new Point2D.Double(600, 1700)));
		elements.add(new BallPosition("white position 4", BallColor.WHITE, new Point2D.Double(600, 2200)));
		elements.add(new BallPosition("white position 5", BallColor.WHITE, new Point2D.Double(1120, 450)));
		elements.add(new BallPosition("white position 6", BallColor.WHITE, new Point2D.Double(1070, 750)));
		elements.add(new BallPosition("white position 7", BallColor.WHITE, new Point2D.Double(1020, 1050)));
		elements.add(new BallPosition("white position 8", BallColor.WHITE, new Point2D.Double(1120, 2550)));
		elements.add(new BallPosition("white position 9", BallColor.WHITE, new Point2D.Double(1070, 2250)));
		elements.add(new BallPosition("white position 10", BallColor.WHITE, new Point2D.Double(1020, 1950)));
		elements.add(new Ball("white ball red side", BallColor.WHITE, new Point2D.Double(800, 520)));
		elements.add(new Ball("white ball blue side", BallColor.WHITE, new Point2D.Double(800, 2480)));
		elements.add(new Ball("white ball middle", BallColor.WHITE, new Point2D.Double(1000, 1500)));

		Collections.sort(elements, new GameBoardElementsComparator());
	}

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
