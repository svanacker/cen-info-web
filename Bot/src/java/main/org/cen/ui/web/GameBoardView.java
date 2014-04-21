package org.cen.ui.web;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.cen.com.IComService;
import org.cen.com.out.OutData;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.ITrajectoryRecorder;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.TrajectoryRecord;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.device.console.IRobotDeviceConsole;
import org.cen.robot.device.console.IRobotDeviceConsoleAction;
import org.cen.robot.device.console.IRobotDeviceConsoleService;
import org.cen.robot.device.console.graph.IConsoleGraph;
import org.cen.robot.device.navigation.com.BezierMoveOutData;
import org.cen.ui.gameboard.GameBoardClickEvent;
import org.cen.ui.gameboard.GameBoardFlags;
import org.cen.ui.gameboard.GameBoardPainter;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.IGameBoardEventListener;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.elements.BezierCurve;
import org.cen.ui.gameboard.elements.ITrajectoryPath;
import org.cen.ui.gameboard.elements.TrajectoryGauge;

/**
 * Objet de présentation encapsulant la table de jeu
 */
public class GameBoardView implements ActionListener {

	private IGameBoardService gameBoard;

	private GameBoardPainter painter;

	private IRobotServiceProvider servicesProvider;

	private int x;

	private int y;

	private double finalAngle;

	private double cp1Distance;

	private double cp2Distance;

	private boolean trajectoryAutoUpdate = true;

	private OutData trajectoryOutData;

	public GameBoardView() {
		super();
	}

	private void buildBezierCurve(Point2D start, Point2D end, double angle) {
		BezierCurve curve = new BezierCurve("TrajectoryPreview", start, end, cp1Distance, cp2Distance, angle, finalAngle);
		TrajectoryGauge gauge = new TrajectoryGauge("TrajectoryPreview", curve);
		gameBoard.removeElements("TrajectoryPreview");
		List<IGameBoardElement> elements = gameBoard.getElements();
		elements.add(curve);
		elements.add(gauge);

		buildOutData();
	}

	private void buildOutData() {
		ITrajectoryPath path = getCurrentTrajectoryPath();
		if (path == null) {
			return;
		}

		Point2D finalPosition = path.getTrajectoryEnd();
		double finalAngle = path.getRobotFinalAngle();
		double x = finalPosition.getX();
		double y = finalPosition.getY();

		trajectoryOutData = new BezierMoveOutData(x, y, finalAngle, cp1Distance / 10.0, cp2Distance / 10.0);
	}

	public void doBezier() {
		if (trajectoryOutData == null) {
			return;
		}

		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(trajectoryOutData);
	}

	public void drawGraph(Graphics2D g2d, Object obj) {
		String consoleName = (String) obj;
		int i = consoleName.indexOf(':');
		consoleName = consoleName.substring(0, i);
		IRobotDeviceConsole console = getConsole(consoleName);
		if (console != null) {
			IConsoleGraph graph = console.getGraph();
			if (graph != null) {
				graph.render(g2d);
			}
		}
	}

	private IRobotDeviceConsole getConsole(String consoleName) {
		IRobotDeviceConsoleService service = servicesProvider.getService(IRobotDeviceConsoleService.class);
		if (service == null) {
			return null;
		}
		List<IRobotDeviceConsole> consoles = service.getConsoles();
		for (IRobotDeviceConsole console : consoles) {
			if (console.getName().equals(consoleName)) {
				return console;
			}
		}
		return null;
	}

	public List<ConsoleView> getConsoles() {
		IRobotDeviceConsoleService service = servicesProvider.getService(IRobotDeviceConsoleService.class);
		if (service == null) {
			return null;
		}
		List<ConsoleView> consoleViews = new ArrayList<ConsoleView>();
		List<IRobotDeviceConsole> consoles = service.getConsoles();
		for (IRobotDeviceConsole console : consoles) {
			ConsoleView view = new ConsoleView(console.getName());
			Collection<IRobotDeviceConsoleAction> actions = console.getActions();
			for (IRobotDeviceConsoleAction action : actions) {
				view.addAction(action.getName(), action.getLabel());
			}
			view.setProperties(console.getProperties());
			consoleViews.add(view);
		}
		return consoleViews;
	}

	public double getCp1Distance() {
		return cp1Distance;
	}

	public double getCp2Distance() {
		return cp2Distance;
	}

	private ITrajectoryPath getCurrentTrajectoryPath() {
		List<IGameBoardElement> elements = gameBoard.findElements("TrajectoryPreview");
		for (IGameBoardElement e : elements) {
			if (e instanceof ITrajectoryPath) {
				return (ITrajectoryPath) e;
			}
		}
		return null;
	}

	public boolean getDisplayLabels() {
		return painter.getDrawFlags().contains(GameBoardFlags.LABELS);
	}

	public boolean getDisplayWeights() {
		return painter.getDrawFlags().contains(GameBoardFlags.PATHS_WEIGHTS);
	}

	public int getFinalAngle() {
		return (int) Math.toDegrees(finalAngle);
	}

	public boolean getHighlightObstacles() {
		return painter.getDrawFlags().contains(GameBoardFlags.OBSTACLES);
	}

	public boolean getHighlightPaths() {
		return painter.getDrawFlags().contains(GameBoardFlags.PATHS);
	}

	public boolean getHighlightTargets() {
		return painter.getDrawFlags().contains(GameBoardFlags.TARGETS);
	}

	public BufferedImage getImage() {
		Dimension d = painter.getSize();
		BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		paint((Graphics2D) image.getGraphics(), null);
		return image;
	}

	public long getN() {
		return System.currentTimeMillis();
	}

	public boolean getTrajectoryAutoUpdate() {
		return trajectoryAutoUpdate;
	}

	public String getTrajectoryCommand() {
		if (trajectoryOutData == null) {
			return "<none defined>";
		} else {
			return trajectoryOutData.getMessage();
		}
	}

	public String getTrajectoryDestination() {
		ITrajectoryPath path = getCurrentTrajectoryPath();
		if (path == null) {
			return "<none>";
		} else {
			Point2D p = path.getTrajectoryEnd();
			double x = p.getX();
			double y = p.getY();
			return String.format("(%.0f, %.0f)", x, y);
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void moveToFinalPosition() {
		ITrajectoryPath path = getCurrentTrajectoryPath();
		if (path == null) {
			return;
		}

		Point2D finalPosition = path.getTrajectoryEnd();
		double finalAngle = path.getRobotFinalAngle();
		double x = finalPosition.getX();
		double y = finalPosition.getY();

		RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		position.set(x, y, finalAngle);
		return;
	}

	public void paint(Graphics2D g2d, Object obj) {
		rebuildTrajectories(painter);
		painter.paint(g2d);
	}

	@Override
	public void processAction(ActionEvent e) {
		Point screenCoordinates = new Point(x, y);
		Point2D realCoordinates = painter.getRealCoordinates(screenCoordinates);
		GameBoardClickEvent event = new GameBoardClickEvent(realCoordinates);
		System.out.println(event);
		gameBoard.postEvent(event);
	}

	public void processConsoleAction(ActionEvent e) {
		System.out.println(e);
		UIComponent c = (UIComponent) e.getSource();
		UIComponent parent = c.getParent();
		String consoleName = parent.getId();
		String actionName = c.getId();

		IRobotDeviceConsole console = getConsole(consoleName);
		if (console != null) {
			Collection<IRobotDeviceConsoleAction> actions = console.getActions();
			for (IRobotDeviceConsoleAction action : actions) {
				if (action.getName().equals(actionName)) {
					action.execute(null);
				}
			}
		}
	}

	private void rebuildTrajectories(GameBoardPainter painter) {
		ITrajectoryRecorder recorder = servicesProvider.getService(ITrajectoryRecorder.class);
		if (recorder == null) {
			return;
		}
		List<TrajectoryRecord> records = recorder.getRecords();
		for (TrajectoryRecord record : records) {
			if (!record.isActive()) {
				continue;
			}
			Path2D path = new Path2D.Double();
			for (Point2D p : record.getTrajectory()) {
				path.lineTo(p.getX(), p.getY());
				System.out.println(p);
			}
			painter.addShape(path, null, Color.MAGENTA);
		}
	}

	public void setCp1Distance(double cp1Distance) {
		this.cp1Distance = cp1Distance;
	}

	public void setCp2Distance(double cp2Distance) {
		this.cp2Distance = cp2Distance;
	}

	public void setDisplayLabels(boolean displayLabels) {
		Set<GameBoardFlags> flags = painter.getDrawFlags();
		if (displayLabels) {
			flags.add(GameBoardFlags.LABELS);
		} else {
			flags.remove(GameBoardFlags.LABELS);
		}
	}

	public void setDisplayWeights(boolean value) {
		Set<GameBoardFlags> flags = painter.getDrawFlags();
		if (value) {
			flags.add(GameBoardFlags.PATHS_WEIGHTS);
		} else {
			flags.remove(GameBoardFlags.PATHS_WEIGHTS);
		}
	}

	public void setFinalAngle(int finalAngle) {
		this.finalAngle = Math.toRadians(finalAngle);
	}

	public void setHighlightObstacles(boolean highlightObstacles) {
		Set<GameBoardFlags> flags = painter.getDrawFlags();
		if (highlightObstacles) {
			flags.add(GameBoardFlags.OBSTACLES);
		} else {
			flags.remove(GameBoardFlags.OBSTACLES);
		}
	}

	public void setHighlightPaths(boolean highlightPaths) {
		Set<GameBoardFlags> flags = painter.getDrawFlags();
		if (highlightPaths) {
			flags.add(GameBoardFlags.PATHS);
		} else {
			flags.remove(GameBoardFlags.PATHS);
		}
	}

	public void setHighlightTargets(boolean highlightTargets) {
		Set<GameBoardFlags> flags = painter.getDrawFlags();
		if (highlightTargets) {
			flags.add(GameBoardFlags.TARGETS);
		} else {
			flags.remove(GameBoardFlags.TARGETS);
		}
	}

	public void setServicesProvider(final IRobotServiceProvider provider) {
		servicesProvider = provider;

		gameBoard = provider.getService(IGameBoardService.class);
		final ITrajectoryService trajectory = provider.getService(ITrajectoryService.class);
		INavigationMap map = trajectory.getNavigationMap();

		// gameBoard.addListener(new IGameBoardEventListener() {
		// @Override
		// public void onGameBoardEvent(IGameBoardEvent event) {
		// if (event instanceof GameBoardClickEvent) {
		// RobotPosition position =
		// RobotUtils.getRobotAttribute(RobotPosition.class, provider);
		// GameBoardClickEvent e = (GameBoardClickEvent) event;
		// List<Location> path = trajectory.getPath(position.getCentralPoint(),
		// e.getCoordinates());
		// painter.setTrajectory(path);
		//
		// List<RobotDeviceRequest> requests = trajectory.getRequests(path);
		// IRobotDevicesHandler handler =
		// servicesProvider.getService(IRobotDevicesHandler.class);
		// DeviceRequestDispatcher dispatcher = handler.getRequestDispatcher();
		// for (RobotDeviceRequest request : requests) {
		// dispatcher.sendRequest(request);
		// }
		//
		// // resets the final angle for the next move
		// finalAngle = 0d;
		// }
		// }
		// });

		gameBoard.addListener(new IGameBoardEventListener() {
			@Override
			public void onGameBoardEvent(IGameBoardEvent event) {
				if (event instanceof GameBoardClickEvent) {
					RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, provider);
					GameBoardClickEvent e = (GameBoardClickEvent) event;

					// P0
					Point2D start = (Point2D) position.getCentralPoint().clone();

					// P3
					Point2D end = e.getCoordinates();

					// Angle
					double angle = position.getAlpha();

					buildBezierCurve(start, end, angle);
				}
			}
		});

		painter = new GameBoardPainter(servicesProvider);
		painter.setSize(new Dimension(400, 500));
	}

	public void setTrajectoryAutoUpdate(boolean trajectoryAutoUpdate) {
		this.trajectoryAutoUpdate = trajectoryAutoUpdate;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void updateTrajectory() {
		List<IGameBoardElement> elements = gameBoard.findElements("TrajectoryPreview");
		for (IGameBoardElement e : elements) {
			if (e instanceof BezierCurve) {
				BezierCurve b = (BezierCurve) e;
				Point2D start = b.getTrajectoryStart();
				Point2D end = b.getTrajectoryEnd();
				double angle = b.getRobotInitialAngle();
				buildBezierCurve(start, end, angle);
				break;
			}
		}
	}
}
