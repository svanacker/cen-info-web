package org.cen.cup.cup2009.gameboard.configuration;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.event.EventListenerList;

import org.cen.cup.cup2009.gameboard.GameBoard2009;
import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceProvider;
import org.cen.vision.coordinates.CoordinatesFactory;
import org.cen.vision.coordinates.CoordinatesTransform;
import org.cen.vision.dataobjects.WebCamProperties;
import org.cen.vision.filters.FilterInterruptor;
import org.cen.vision.filters.GrowingRegionOpImage;
import org.cen.vision.filters.GrowingRegionOpImage.Region;

public class GameboardConfigurationHandler2009 implements IGameboardConfigurationHandler {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private static final int OFFSET_COLUMNS_X = GameBoard2009.COLUMN_OFFSET_X - (GameBoard2009.COLUMN_SPACING_X / 2);

	private static final int OFFSET_COLUMNS_Y = GameBoard2009.COLUMN_OFFSET_Y - (GameBoard2009.COLUMN_SPACING_Y / 2);

	public static void main(String[] args) {
		GameboardConfigurationHandler2009 h = new GameboardConfigurationHandler2009();
		InputStream is = ClassLoader.getSystemResourceAsStream("org/cen/cup/cup2009/gameboard/elements.properties");
		Properties elements = new Properties();
		try {
			elements.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		h.setElements(elements);
		h.setDetectedPositions(new Integer[] { 1, 4, 6, 7, 8, 10 });
		System.out.println("card: " + h.getColumnElementsCard() + ", errors: " + h.getError());
	}

	private double colorAngle;

	private int columnsCard;

	private int columnsError;

	private CoordinatesTransform coordinates;

	private DebugEvent debugEvent = new DebugEvent(this);

	private Map<Integer, Integer[]> elements = new HashMap<Integer, Integer[]>();

	private BufferedImage filteredInput;

	private Image input;

	private FilterInterruptor interruptor = new FilterInterruptor();

	private EventListenerList listeners = new EventListenerList();

	private double minRegionIntensity = 0.5d;

	private int minRegionSize = 20;

	private boolean mirror = false;

	private BufferedImage regionOutput;

	private List<Region> regions = new ArrayList<Region>();

	private double regionThreshold = 1500;

	private IRobotServiceProvider servicesProvider;

	private AffineTransform transform = new AffineTransform();

	private WebCamProperties webCamProperties;

	public GameboardConfigurationHandler2009() {
		super();
	}

	public void addListener(DebugEventListener listener) {
		listeners.add(DebugEventListener.class, listener);
	}

	private int analyzeRegion(Region r) {
		Point2D p = r.getLocation();
		p = coordinates.screenToGameBoard((int) p.getX(), (int) p.getY());
		transform.transform(p, p);
		int x = (int) (p.getX() - OFFSET_COLUMNS_X) / GameBoard2009.COLUMN_SPACING_X;
		int y = (int) (p.getY() - OFFSET_COLUMNS_Y) / GameBoard2009.COLUMN_SPACING_Y;
		if (x >= 0 && x <= (GameBoard2009.COLUMNS_X_COUNT - 1) && y >= 0 && y <= GameBoard2009.COLUMNS_Y_COUNT - 1) {
			int position = x * (GameBoard2009.COLUMNS_X_COUNT - 1) + y + 1;
			LOGGER.fine("Region " + r.getId() + ", x=" + x + ", y=" + y + " --> position " + position);
			return position;
		}
		return 0;
	}

	private void analyzeRegions() {
		if (regions.isEmpty()) {
			return;
		}

		List<Integer> l = new ArrayList<Integer>();
		for (Region r : regions) {
			int id = analyzeRegion(r);
			if (id > 0 && id < 13) {
				l.add(id);
			}
		}
		setDetectedPositions(l.toArray(new Integer[l.size()]));
	}

	private void buildColorFilter(double colorAngle) throws InterruptedException {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(input);
		pb.add(colorAngle);
		pb.add(null);
		pb.add(interruptor);
		RenderedOp op = JAI.create("ColorFilter", pb);

		filteredInput = op.getAsBufferedImage();
		if (interruptor.isInterrupted()) {
			throw new InterruptedException();
		}
	}

	private void buildRegionFilter(double regionThreshold) throws InterruptedException {
		interruptor.setInterrupted(false);
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(filteredInput);
		pb.add(regionThreshold);
		pb.add(interruptor);
		RenderedOp op = JAI.create("GrowingRegionFilter", pb);
		regionOutput = op.getAsBufferedImage();
		if (interruptor.isInterrupted()) {
			throw new InterruptedException();
		}

		regions.clear();
		int n = 0;
		Object regionCount = op.getProperty(GrowingRegionOpImage.KEY_REGIONCOUNT);
		if (regionCount instanceof Integer) {
			n = (Integer) regionCount;
		}
		for (int i = 1; i <= n; i++) {
			Region r = (Region) op.getProperty("region" + i);
			if (r.getMeanValue()[0] / 255d > minRegionIntensity && r.getCount() > 10) {
				regions.add(r);
			}
		}
	}

	public void execute() {
		try {
			buildColorFilter(colorAngle);
			buildRegionFilter(regionThreshold);
			analyzeRegions();
		} catch (InterruptedException e) {
			LOGGER.finest("analyze was interrupted");
		}
	}

	private void fireDebugEvent() {
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == DebugEventListener.class) {
				((DebugEventListener) l[i + 1]).debug(debugEvent);
			}
		}
	}

	public double getColorAngle() {
		return colorAngle;
	}

	public int getColumnElementsCard() {
		return columnsCard;
	}

	public int getError() {
		return columnsError;
	}

	public BufferedImage getFilteredInput() {
		return filteredInput;
	}

	public Image getInput() {
		return input;
	}

	public double getMinRegionIntensity() {
		return minRegionIntensity;
	}

	public int getMinRegionSize() {
		return minRegionSize;
	}

	public List<Region> getRegions() {
		return regions;
	}

	public double getRegionThreshold() {
		return regionThreshold;
	}

	public AffineTransform getTransform() {
		return transform;
	}

	public WebCamProperties getWebCamProperties() {
		return webCamProperties;
	}

	public void interrupt() {
		interruptor.setInterrupted(true);
	}

	public void setColorAngle(double colorAngle) {
		this.colorAngle = colorAngle;
	}

	public void setDetectedPositions(Integer[] objects) {
		Map<Integer, Integer> errors = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer[]> e : elements.entrySet()) {
			Integer[] p = e.getValue();
			// compteur d'erreurs
			int error = 0;
			// parcours des positions détectées
			int expected = p.length;
			int detected = objects.length;

			detectedPos: for (int i = 0; i < detected; i++) {
				// recherche dans les positions attendues
				for (int j = 0; j < expected; j++) {
					if (objects[i].intValue() == p[j].intValue()) {
						// trouvé, on passe au suivant
						continue detectedPos;
					}
				}
				// pas trouvé, on incrémente le compteur d'erreurs
				error++;
			}

			expectedPos: for (int i = 0; i < expected; i++) {
				// recherche dans les positions attendues
				for (int j = 0; j < detected; j++) {
					if (objects[j].intValue() == p[i].intValue()) {
						// trouv�, on passe au suivant
						continue expectedPos;
					}
				}
				// pas trouvé, on incrémente le compteur d'erreurs
				error++;
			}

			errors.put(e.getKey(), error);
		}
		int min = Integer.MAX_VALUE;
		for (Entry<Integer, Integer> e : errors.entrySet()) {
			int v = e.getValue();
			if (v < min) {
				min = v;
				columnsCard = e.getKey();
				columnsError = v;
			}
		}
	}

	public void setElements(Properties elements) {
		// Lecture des éléments (format : card1=1,2,3,4,5,6)
		List<Integer> list = new ArrayList<Integer>();
		for (Entry<Object, Object> e : elements.entrySet()) {
			list.clear();
			Object key = e.getKey();
			if (key instanceof String) {
				String s = (String) key;
				if (s.startsWith("card")) {
					s = s.substring("card".length());
				} else {
					continue;
				}
				Object value = e.getValue();
				Integer i = Integer.valueOf(s);
				String[] positionIds = ((String) value).split("\\W*,\\W*");
				try {
					for (String id : positionIds) {
						list.add(Integer.valueOf(id));
					}
					Integer[] positions = new Integer[list.size()];
					list.toArray(positions);
					this.elements.put(i, positions);
				} catch (Exception ex) {
					// Ignore la carte invalide
					continue;
				}
			}
		}
	}

	public void setInput(Image input) {
		this.input = input;
	}

	public void setMinRegionIntensity(double minRegionIntensity) {
		this.minRegionIntensity = minRegionIntensity;
	}

	public void setMinRegionSize(int minRegionSize) {
		this.minRegionSize = minRegionSize;
	}

	public void setMirror(boolean mirror) {
		this.mirror = mirror;
	}

	public void setPosition(double x, double y, double orientation) {
		// symétrie par rapport à la moitié de la longueur du terrain
		if (mirror) {
			y = GameBoard2009.BOARD_HEIGHT - y;
		}
		transform.setToIdentity();
		transform.translate(x, y);
		if (mirror) {
			// symétrie par rapport à la moitié de la largeur
			transform.scale(1, -1);
		}
		// rotation
		transform.rotate(orientation);
	}

	public void setRegionThreshold(double regionThreshold) {
		this.regionThreshold = regionThreshold;
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		servicesProvider.registerService(IGameboardConfigurationHandler.class, this);
	}

	public void setWebCamProperties(WebCamProperties webCamProperties) {
		this.webCamProperties = webCamProperties;
		coordinates = CoordinatesFactory.getCoordinates(webCamProperties);
	}
}
