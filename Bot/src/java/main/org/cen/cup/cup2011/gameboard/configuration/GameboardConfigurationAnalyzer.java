package org.cen.cup.cup2011.gameboard.configuration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.ImageIcon;

import org.cen.cup.cup2011.gameboard.GameBoard2011;
import org.cen.logging.ILoggingService;
import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.ui.gameboard.GameBoardFlags;
import org.cen.ui.gameboard.GameBoardPainter;
import org.cen.vision.IVisionService;
import org.cen.vision.coordinates.CoordinatesFactory;
import org.cen.vision.coordinates.CoordinatesTransform;
import org.cen.vision.dataobjects.WebCamProperties;
import org.cen.vision.filters.FilterInterruptor;
import org.cen.vision.filters.GrowingRegionOpImage.Region;

public class GameboardConfigurationAnalyzer implements Runnable {
	// the ratio of the image height that is filtered out
	private static final double RATIO_FILTERED_ZONE = .25d;

	// the ratio of the image height that is filtered out for initial positions
	private static final double RATIO_FILTERED_ZONE_INITIAL = .23d;

	// the minimum value a region must have to be identified as a pawn
	private static final int MIN_REGION_LEVEL = 128;

	// the minimum size a region must have to be identified as a pawn
	private static final int MIN_REGION_SIZE = 30;

	private final IRobotServiceProvider servicesProvider;

	private IVisionService vision;

	private boolean running = false;

	private final double colorAngle = 1.03d;

	private long colorFilterTime = 0;

	private final double regionThreshold = 2500d;

	private final int regionSeedInterval = 6;

	private long regionsFilterTime = 0;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private FilterInterruptor interruptor;

	private RobotPosition snapshotPosition;

	private IGameboardAnalysisListener listener;

	private boolean initial = false;

	private final List<Point2D> initialPawnPositions;

	private int debugSequence = 0;

	private static final int SEARCH_RANGE_X = 2;

	private static final int SEARCH_RANGE_Y = 2;

	private boolean debug = false;

	private String debugPath = null;

	private static final boolean USE_PHOTO = false;

	// Distance maximale d'un pion pour être pris en compte
	private static final double MAX_PAWN_DISTANCE = 800;

	public GameboardConfigurationAnalyzer(IRobotServiceProvider provider, List<Point2D> initialPawnPositions,
			String debugPath) {
		super();
		this.servicesProvider = provider;
		this.initialPawnPositions = initialPawnPositions;
		debug = debugPath != null && !debugPath.isEmpty();
		if (debug) {
			DateFormat format = new SimpleDateFormat("yyMMdd-HHmmss");
			debugPath += format.format(new Date());
			File directory = new File(debugPath);
			directory.mkdir();
			debugPath += "\\";
		}
		this.debugPath = debugPath;
		initialize();
	}

	private void analyze(Image image) throws InterruptedException {
		interruptor.setInterrupted(false);
		debugImage((RenderedImage) image, "src");
		image = applyZoneFilter(image);
		debugImage((RenderedImage) image, "filter");
		image = buildColorFilter(image);
		debugImage((RenderedImage) image, "color");
		RenderedOp op = buildRegionFilter(image);
		List<Region> regions = analyzeRegions(op);
		List<Point2D> points;
		if (initial) {
			points = getInitialPawnCoordinates(regions, op.getAsBufferedImage());
		} else {
			points = getPawnCoordinates(regions);
		}
		if (debug) {
			printResults(points);
			debugGameBoard();
		}
		listener.finished(points);
	}

	public void analyzeGameboard(IGameboardAnalysisListener listener, boolean initial) {
		if (running) {
			return;
		}
		running = true;
		this.initial = initial;
		this.listener = listener;
		Thread thread = new Thread(this, getClass().getSimpleName());
		thread.start();
	}

	private List<Region> analyzeRegions(RenderedOp op) throws InterruptedException {
		long t = System.currentTimeMillis();
		BufferedImage image = op.getAsBufferedImage();
		regionsFilterTime = System.currentTimeMillis() - t;
		if (interruptor.isInterrupted()) {
			throw new InterruptedException();
		}
		Graphics g = image.getGraphics();

		List<Region> regions = new ArrayList<Region>();

		int n = (Integer) op.getProperty("regionCount");
		for (int i = 1; i <= n; i++) {
			Region r = (Region) op.getProperty("region" + i);
			boolean b = r.getMeanValue()[0] > MIN_REGION_LEVEL && r.getCount() > MIN_REGION_SIZE;
			if (b) {
				regions.add(r);
			}
			if (debug) {
				Point2D p = r.getLocation();
				if (b) {
					drawPosition(g, p, Color.RED);
				} else {
					drawPosition(g, p, Color.WHITE);
				}
				Rectangle bounds = r.getBounds();
				g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		}
		if (debug) {
			WebCamProperties wcp = vision.getWebCamProperties();
			CoordinatesTransform c = CoordinatesFactory.getCoordinates(wcp);
			drawSquares(image, snapshotPosition, c);
		}
		debugImage(image, "regions");
		return regions;
	}

	private Image applyZoneFilter(Image image) {
		if (!(image instanceof BufferedImage)) {
			return image;
		}
		BufferedImage im = (BufferedImage) image;
		Graphics g = im.getGraphics();
		double ratio;
		if (initial) {
			ratio = RATIO_FILTERED_ZONE_INITIAL;
		} else {
			ratio = RATIO_FILTERED_ZONE;
		}
		g.fillRect(0, 0, image.getWidth(null), (int) (image.getHeight(null) * ratio));

		return im;
	}

	private Image buildColorFilter(Image image) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(5);
		pb.add(5);
		pb.add(2);
		pb.add(2);
		RenderedOp op = JAI.create("BoxFilter", pb);

		pb = new ParameterBlock();
		pb.addSource(op.getAsBufferedImage());
		pb.add(colorAngle);
		op = JAI.create("ColorFilter", pb);

		long t = System.currentTimeMillis();
		BufferedImage result = op.getAsBufferedImage();
		colorFilterTime = System.currentTimeMillis() - t;
		return result;
	}

	private RenderedOp buildRegionFilter(Image image) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(regionThreshold);
		pb.add(regionSeedInterval);
		pb.add(regionSeedInterval);
		RenderedOp op = JAI.create("GrowingRegionFilter", pb);

		return op;
	}

	private void debugGameBoard() {
		Dimension d = new Dimension(420, 600);
		GameBoardPainter p = new GameBoardPainter(servicesProvider);
		p.setSize(d);
		BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		p.getDrawFlags().add(GameBoardFlags.OBJECTS);
		p.getDrawFlags().add(GameBoardFlags.PATHS);
		p.paint(image.getGraphics());
		debugImage(image, "gameboard", "png");
	}

	private void debugImage(RenderedImage image, String name) {
		debugImage(image, name, "jpg");
	}

	private void debugImage(RenderedImage image, String name, String format) {
		if (debug) {
			try {
				ImageIO.write(image, format, new File(getDebugPath() + name + debugSequence + "." + format));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void drawPosition(Graphics g, Point2D p, Color color) {
		g.setColor(color);
		g.drawLine((int) p.getX() - 3, (int) p.getY(), (int) p.getX() + 3, (int) p.getY());
		g.drawLine((int) p.getX(), (int) p.getY() - 3, (int) p.getX(), (int) p.getY() + 3);
	}

	private void drawSquares(BufferedImage image, RobotPosition robotPosition, CoordinatesTransform c) {
		Graphics g = image.getGraphics();
		for (int x = GameboardConfigurationHandler2011.COLUMN_OFFSET_X; x < GameboardConfigurationHandler2011.COLUMN_OFFSET_LAST_X; x += GameboardConfigurationHandler2011.COLUMN_SPACING_X) {
			for (int y = GameboardConfigurationHandler2011.COLUMN_OFFSET_Y; y < GameboardConfigurationHandler2011.COLUMN_OFFSET_LAST_Y; y += GameboardConfigurationHandler2011.COLUMN_SPACING_Y) {
				Point2D p1 = new Point2D.Double(x, y);
				p1 = c.absoluteToScreen(robotPosition, p1);

				Point2D p2 = new Point2D.Double(x + GameboardConfigurationHandler2011.COLUMN_SPACING_X, y);
				p2 = c.absoluteToScreen(robotPosition, p2);
				g.setColor(Color.GREEN);
				g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());

				p2 = new Point2D.Double(x, y + GameboardConfigurationHandler2011.COLUMN_SPACING_Y);
				p2 = c.absoluteToScreen(robotPosition, p2);
				g.setColor(Color.BLUE);
				g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
			}
		}
	}

	public long getColorFilterTime() {
		return colorFilterTime;
	}

	private String getDebugPath() {
		return debugPath;
	}

	private List<Point2D> getInitialPawnCoordinates(List<Region> regions, BufferedImage image) {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		WebCamProperties wcp = vision.getWebCamProperties();
		CoordinatesTransform c = CoordinatesFactory.getCoordinates(wcp);

		Graphics g = image.getGraphics();

		for (Point2D pawnPosition : initialPawnPositions) {
			Point2D p = c.absoluteToScreen(snapshotPosition, pawnPosition);

			for (Region r : regions) {
				if (r.getBounds().contains(p) && isPawnPresent(image, p)) {
					points.add(pawnPosition);
					if (debug) {
						drawPosition(g, p, Color.RED);
					}
				}
			}
		}

		if (debug) {
			drawSquares(image, snapshotPosition, c);
			debugImage(image, "pawns");
		}
		return points;
	}

	private List<Point2D> getPawnCoordinates(List<Region> regions) {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		WebCamProperties wcp = vision.getWebCamProperties();
		CoordinatesTransform c = CoordinatesFactory.getCoordinates(wcp);
		for (Region r : regions) {
			Point2D p = r.getLocation();
			int x = (int) p.getX();
			int y = (int) p.getY();
			p = c.screenToAbsolute(snapshotPosition, x, y);
			if (isValidPosition(p)) {
				points.add(p);
			}
		}
		return points;
	}

	public long getRegionsFilterTime() {
		return regionsFilterTime;
	}

	private Image getResourceImage(String name) {
		ClassLoader l = getClass().getClassLoader();
		URL u = l.getResource("org/cen/cup/cup2011/gameboard/configuration/" + name);
		Image image = Toolkit.getDefaultToolkit().getImage(u);
		// forces synchronous loading
		new ImageIcon(image);
		BufferedImage im = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		g.drawImage(image, 0, 0, null);
		return im;
	}

	private Image getTestImage() {
		return getResourceImage("Picture 1.jpg");
	}

	@PostConstruct
	protected void initialize() {
		LOGGER.fine("Initializing gameboard configuration analyzer");
		interruptor = new FilterInterruptor();
		LOGGER.fine("Gameboard configuration analyzer initialized");
	}

	public void initializeVision() {
		vision = servicesProvider.getService(IVisionService.class);
		if (vision != null) {
			vision.getImage();
		}
	}

	public void interrupt() {
		if (running) {
			interruptor.setInterrupted(true);
		}
	}

	private boolean isPawnPresent(BufferedImage image, Point2D p) {
		int x = (int) p.getX();
		int y = (int) p.getY();
		for (int dx = x - SEARCH_RANGE_X; dx < x + SEARCH_RANGE_X; dx++) {
			for (int dy = y - SEARCH_RANGE_Y; dy < y + SEARCH_RANGE_Y; dy++) {
				if (dx < 0 || dx >= image.getWidth() || dy < 0 || dy >= image.getHeight()) {
					continue;
				}
				int rgb = image.getRGB(dx, dy);
				if ((rgb & 0xFFFFFF) != 0) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isValidPosition(Point2D p) {
		if (p.distance(snapshotPosition.getCentralPoint()) > MAX_PAWN_DISTANCE) {
			return false;
		}
		double x = p.getX();
		double y = p.getY();
		return (x > GameboardConfigurationHandler2011.COLUMN_OFFSET_X + GameboardConfigurationHandler2011.PAWN_RADIUS
				&& x < GameboardConfigurationHandler2011.COLUMN_OFFSET_LAST_X
						- GameboardConfigurationHandler2011.PAWN_RADIUS
				&& y > GameboardConfigurationHandler2011.PAWN_RADIUS && y < GameBoard2011.BOARD_HEIGHT
				- GameboardConfigurationHandler2011.PAWN_RADIUS);
	}

	private void printResults(List<Point2D> points) {
		StringBuilder b = new StringBuilder();
		b.append("Color filter duration (ms): " + getColorFilterTime());
		b.append('\n');
		b.append("Regions filter duration (ms): " + getRegionsFilterTime());
		b.append('\n');
		b.append("Snapshot position: " + snapshotPosition);
		b.append('\n');
		b.append("Pawns detected: " + points.size());
		b.append('\n');
		for (Point2D p : points) {
			b.append("Pawn: " + p);
			b.append('\n');
		}
		String s = b.toString();

		try {
			Writer w = new FileWriter(new File(getDebugPath() + "results" + debugSequence + ".txt"));
			try {
				w.write(s);
			} finally {
				w.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			if (vision == null || (!debug && !vision.isAvailable())) {
				return;
			}
			LOGGER.finest("Taking gameboard snapshot");
			Image image = null;
			if (USE_PHOTO) {
				image = getTestImage();
			} else {
				image = vision.getImage();
			}
			if (image != null) {
				if (debug) {
					debugSequence++;
				}
				RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
				Point2D cp = position.getCentralPoint();
				snapshotPosition = new RobotPosition(cp.getX(), cp.getY(), position.getAlpha());
				LOGGER.finest("Analyzing gameboard");
				try {
					analyze(image);
				} catch (InterruptedException e) {
					LOGGER.finest("analyze was interrupted");
					listener.finished(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			running = false;
		}
	}

	public void setDebugPath(String debugPath) {
		this.debugPath = debugPath;
	}

	@PreDestroy
	public void shutdown() {
		LOGGER.fine("Shutting down gameboard configuration analyzer");
		interruptor.setInterrupted(true);
		interruptor = null;
		vision = null;
		if (debug) {
			writeLog();
		}
		LOGGER.fine("Gameboard configuration analyzer shut down");
	}

	private void writeLog() {
		ILoggingService logging = servicesProvider.getService(ILoggingService.class);
		try {
			Writer w = new FileWriter(new File(getDebugPath() + "log.txt"));
			try {
				logging.save(w);
			} finally {
				w.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
