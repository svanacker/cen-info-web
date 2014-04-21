package org.cen.ui.gameboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.cen.navigation.INavigationMap;
import org.cen.navigation.IPathVector;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.navigation.PathVectorLine;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.strategy.IGameStrategy;
import org.cen.robot.match.strategy.IGameStrategyItem;
import org.cen.robot.match.strategy.IGameStrategyItemList;
import org.cen.robot.match.strategy.ITarget;
import org.cen.ui.gameboard.robot.OpponentRobotPainter;
import org.cen.ui.gameboard.robot.RobotPainter;
import org.cen.ui.gameboard.trajectory.TrajectoryPainter;

/**
 * Game board painter. This object draws the game board at the specified
 * dimensions into a given graphics object.
 * 
 * @author Emmanuel ZURMELY
 */
public class GameBoardPainter {

	private final Set<GameBoardFlags> drawFlags;

	/**
	 * The drawing scale.
	 */
	protected double scale;

	/**
	 * The list of shapes to draw.
	 */
	protected List<ShapeData> shapes = new ArrayList<ShapeData>();

	protected Dimension size;

	private List<Location> trajectory;

	private AffineTransform transform;

	private AffineTransform transformInv;

	private final IRobotServiceProvider servicesProvider;

	private final BasicStroke[] pathStrokes = { new BasicStroke(3), new BasicStroke(5), new BasicStroke(7) };

	/**
	 * Constructor.
	 * 
	 * @param servicesProvider
	 *            the services provider
	 */
	public GameBoardPainter(IRobotServiceProvider servicesProvider) {
		super();
		this.servicesProvider = servicesProvider;
		scale = 1;
		drawFlags = EnumSet.of(GameBoardFlags.OBJECTS, GameBoardFlags.TRAJECTORY, GameBoardFlags.OPPONENT, GameBoardFlags.TARGETS);
	}

	/**
	 * Adds a shape to draw with the given attributes.
	 * 
	 * @param shape
	 *            the shape to draw
	 * @param stroke
	 *            the stroke to use to draw the shape
	 * @param paint
	 *            the paint to use to draw the shape
	 */
	public void addShape(Shape shape, Stroke stroke, Paint paint) {
		shapes.add(new ShapeData(shape, stroke, paint));
	}

	/**
	 * Clears the list of the shapes to draw.
	 */
	public void clearShapes() {
		shapes.clear();
	}

	private void drawCircle(Graphics2D g2d, int x, int y, int radius) {
		g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
	}

	/**
	 * Returns the draw flags.
	 * 
	 * @return the draw flags
	 */
	public Set<GameBoardFlags> getDrawFlags() {
		return drawFlags;
	}

	/**
	 * Transforms coordinates from scaled coordinates to real coordinates.
	 * 
	 * @param screenCoordinates
	 *            the scaled coordinates in pixel
	 * @return the real coordinates
	 */
	public Point2D getRealCoordinates(Point screenCoordinates) {
		Point2D realCoordinates = new Point2D.Double();
		transformInv.transform(screenCoordinates, realCoordinates);
		return realCoordinates;
	}

	public Dimension getSize() {
		return size;
	}

	/**
	 * Renders the game board into the specified graphic object.
	 * 
	 * @param g
	 *            the target graphic object
	 */
	public void paint(Graphics g) {
		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
		INavigationMap navigationMap = trajectoryService.getNavigationMap();
		// scale transform used for rendering obstacles
		AffineTransform obstacles = AffineTransform.getScaleInstance(1.5, 1.5);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform t = g2d.getTransform();
		Paint paint = g2d.getPaint();
		Stroke stroke = g2d.getStroke();
		try {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for (GameBoardFlags stage : drawFlags) {
				if (stage.equals(GameBoardFlags.OBSTACLES)) {
					g.setColor(new Color(1f, 0f, 0f, .25f));
				}
				for (IGameBoardElement e : gameBoard.getElements()) {
					g2d.setTransform(transform);
					Point2D p = e.getPosition();
					double x = p.getX();
					double y = p.getY();
					g2d.translate(x, y);
					double theta = e.getOrientation();
					g2d.rotate(theta);
					switch (stage) {
					case OBJECTS:
						paintElement(e, g2d, t);
						g2d.setPaint(paint);
						g2d.setStroke(stroke);
						break;
					case OBSTACLES:
						Shape s = e.getBounds();
						if (s != null) {
							Area a = new Area(s);
							a.transform(obstacles);
							g2d.fill(a);
						}
						break;
					}
				}
			}
			g2d.setTransform(transform);
			if (drawFlags.contains(GameBoardFlags.PATHS)) {
				paintPaths(g2d, navigationMap, t);
			}
			if (drawFlags.contains(GameBoardFlags.TRAJECTORY) && trajectory != null) {
				TrajectoryPainter trajectoryPainter = new TrajectoryPainter(trajectory);
				trajectoryPainter.paint(g2d);
			}

			paintShapes(g2d);
			paintRobot(g2d);
			if (drawFlags.contains(GameBoardFlags.OPPONENT)) {
				OpponentRobotPainter opponentRobotPainter = new OpponentRobotPainter(servicesProvider);
				opponentRobotPainter.paint(g2d);
			}
			if (drawFlags.contains(GameBoardFlags.TARGETS)) {
				paintTargets(g2d, t);
			}
		} finally {
			g2d.setTransform(t);
		}
	}

	private void paintElement(IGameBoardElement e, Graphics2D g2d, AffineTransform t) {
		e.paint(g2d);
		g2d.setTransform(t);
		Point2D point = e.getPosition();
		Point2D p = transform.transform(point, null);
		g2d.translate(p.getX(), p.getY());
		e.paintUnscaled(g2d);
	}

	/**
	 * Draws the navigation points and their labels into the specified graphic
	 * object.
	 * 
	 * @param g
	 *            the target graphic object
	 */
	private void paintNavigationPoint(AffineTransform transform, Graphics2D g) {
		// Draws the point
		ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
		INavigationMap navigationMap = trajectoryService.getNavigationMap();
		Collection<Location> locations = navigationMap.getLocations();
		for (Location location : locations) {
			g.setColor(Color.DARK_GRAY);
			drawCircle(g, location.getX(), location.getY(), 10);
		}

		if (getDrawFlags().contains(GameBoardFlags.LABELS)) {
			// Draws the name of the point
			AffineTransform t = g.getTransform();
			g.setTransform(transform);
			Point2D point = new Point2D.Float();
			for (Location location : locations) {
				Font font = g.getFont().deriveFont(10f);
				g.setFont(font);
				Point2D lpoint = location.getPosition();
				point = t.transform(lpoint, point);
				// Affichage du nom
				String name = location.getName();
				g.drawString(name, (int) point.getX(), (int) point.getY());
				// Affichage des coordonnées
				name = "(" + (int) lpoint.getX() + ", " + (int) lpoint.getY() + ")";
				// g.drawString(name, (int) point.getX(), (int) point.getY() +
				// 10);
			}
			g.setTransform(t);
		}
	}

	private void paintPaths(Graphics2D g2d, INavigationMap navigationMap, AffineTransform t) {
		g2d.setColor(Color.BLACK);
		Collection<IPathVector> paths = navigationMap.getPathVectors();
		// Show the Grid of Paths
		for (IPathVector p : paths) {
			if (p instanceof PathVectorLine) {
				PathVectorLine path = (PathVectorLine) p;
				Location start = path.getStart();
				Location end = path.getEnd();
				int w = Math.max(Math.min(path.getWeight() / 10, 250), 0);
				Color color = new Color(w, 0, w);
				g2d.setColor(color);
				g2d.setStroke(pathStrokes[w / 85]);
				g2d.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
				if (drawFlags.contains(GameBoardFlags.PATHS_WEIGHTS)) {
					g2d.setTransform(t);
					Font font = g2d.getFont().deriveFont(10f);
					g2d.setFont(font);
					Point point = new Point((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
					transform.transform(point, point);
					String s = Integer.toString(start.getDistance(end) + path.getWeight());
					g2d.drawString(s, point.x, point.y);
					g2d.setTransform(transform);
				}
			}
		}
		// Paint the navigation points and their name
		paintNavigationPoint(t, g2d);
	}

	/**
	 * Renders the robot into the specified graphic object.
	 * 
	 * @param g
	 *            the target graphic object
	 */
	private void paintRobot(Graphics2D g2d) {
		RobotPainter robotPainter = new RobotPainter(servicesProvider);
		robotPainter.paint(g2d);
	}

	/**
	 * Renders the shapes into the specified graphic object.
	 * 
	 * @param g
	 *            the target graphic object
	 */
	public void paintShapes(Graphics2D g) {
		for (ShapeData d : shapes) {
			Stroke stroke = d.getStroke();
			if (stroke != null) {
				g.setStroke(stroke);
			}
			Paint paint = d.getPaint();
			if (paint != null) {
				g.setPaint(paint);
			}
			g.draw(d.getShape());
		}
	}

	private void paintTargets(Graphics2D g2d, AffineTransform t) {
		MatchData matchData = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		IGameStrategy strategy = matchData.getStrategy();
		if (strategy == null) {
			return;
		}
		IGameStrategyItemList items = strategy.getItems();

		Shape s = new Ellipse2D.Double(-30, -30, 60, 60);
		Color availableColor = new Color(0.2f, 1.0f, 0.2f, 0.75f);
		Color unavailableColor = new Color(1.0f, 0.2f, 0.2f, 0.75f);
		for (IGameStrategyItem item : items) {
			ITarget target = item.getTarget();
			Point2D p = target.getPosition();
			if (p != null) {
				if (target.isAvailable()) {
					g2d.setColor(availableColor);
				} else {
					g2d.setColor(unavailableColor);
				}
				double x = p.getX();
				double y = p.getY();
				g2d.translate(x, y);
				g2d.fill(s);
				g2d.translate(-x, -y);
			}
		}
	}

	/**
	 * Sets the size of the rendering area in pixels.
	 * 
	 * @param size
	 *            the size of the rendering area in pixels
	 */
	public void setSize(Dimension size) {
		this.size = size;
		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		Rectangle2D bounds = gameBoard.getVisibleBounds();
		double sx = size.getWidth() / bounds.getWidth();
		double sy = size.getHeight() / bounds.getHeight();
		scale = Math.min(sx, sy);
		transform = new AffineTransform();
		transform.setToIdentity();
		// (0, 0) is at bottom right
		// positive x goes right
		// positive y goes up
		transform.scale(scale, -scale);
		// set the origin of the game area (0, 0)
		transform.translate(-bounds.getX(), -bounds.getY());
		// make all the board visible
		// transform.translate(-bounds.getWidth(), -bounds.getHeight());
		transform.translate(0, -bounds.getHeight());
		// create the inverse transform for transforming screen coordinates
		// into real coordinates
		try {
			transformInv = transform.createInverse();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the list of trajectories to render.
	 * 
	 * @param trajectory
	 *            the list of trajectories to render
	 */
	public void setTrajectory(List<Location> trajectory) {
		this.trajectory = trajectory;
	}
}
