package org.cen.cup.cup2010.ui.web;

import org.cen.geom.Point2D;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.ajax4jsf.event.PushEventListener;
import org.apache.commons.lang3.StringUtils;
import org.cen.cup.cup2010.gameboard.GameBoard2010;
import org.cen.cup.cup2010.gameboard.elements.Corn;
import org.cen.cup.cup2010.robot.match.MatchData2010;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.robot.match.Opponent;
import org.cen.robot.match.OpponentPosition;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.IGameBoardEventListener;
import org.cen.ui.gameboard.IGameBoardService;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Backing bean for the elements positions view.
 * 
 * @author Emmanuel ZURMELY
 */
public class ElementsPositionsView implements ResourceLoaderAware, IGameBoardEventListener {
	private static final String CORN_ELEMENT_POSITION = "corn";

	private static final String KEY_CARD1 = "uiCard1";

	private static final String KEY_CARD2 = "uiCard2";

	private static final String RANDOM_CORN_ELEMENT = "random corn element";

	private String configuration;

	private int elementsConfigurationCard1;

	private int elementsConfigurationCard2;

	private PushEventListener listener;

	private final Map<String, Point2D> positions = new HashMap<String, Point2D>();

	private Properties properties;

	private ResourceLoader resourceLoader;

	private IRobotServiceProvider servicesProvider;

	private String trajectoryData;

	public String getTrajectoryData() {
		return trajectoryData;
	}

	public void addOpponentPositionsListener(EventListener listener) {
		synchronized (listener) {
			if (this.listener != listener) {
				IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
				gameBoard.removeListener(this);
				gameBoard.addListener(this);
				this.listener = (PushEventListener) listener;
			}
		}
	}

	private void buildPositionsMap() {
		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		List<IGameBoardElement> elements = gameBoard.getElements();
		for (IGameBoardElement e : elements) {
			String name = e.getName();
			if (e instanceof Corn) {
				if (name.startsWith(CORN_ELEMENT_POSITION)) {
					if (e.getPosition().getY() > 1500) {
						// On ne garde qu'un seul côté, l'autre étant symétrique
						continue;
					}
					String id = name.substring(CORN_ELEMENT_POSITION.length());
					positions.put(id, e.getPosition());
				}
			}
		}
	}

	public String getConfiguration() {
		return configuration;
	}

	public int getElementsConfigurationCard1() {
		return elementsConfigurationCard1;
	}

	public int getElementsConfigurationCard2() {
		return elementsConfigurationCard2;
	}

	private MatchData2010 getMatchData() {
		return (MatchData2010) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
	}

	public List<OpponentPositionView> getOpponentPositions() {
		List<OpponentPositionView> list = new ArrayList<OpponentPositionView>();
		Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class, servicesProvider);
		List<OpponentPosition> movements = opponent.getMovementsHistory();
		for (OpponentPosition m : movements) {
			list.add(new OpponentPositionView(m, this));
		}
		return list;
	}

	private List<Point2D> getPositions(int cardId, int configurationId) {
		List<Point2D> result = new ArrayList<Point2D>();
		Properties p = getProperties();
		String s = p.getProperty("card" + cardId + "." + configurationId);
		if (s != null) {
			String[] positionIds = s.split("\\W*,\\W*");
			for (String id : positionIds) {
				// Si une position n'existe pas, on lève une exception, pour
				// être sûr de ne pas passer à côté
				if (!positions.containsKey(id)) {
					throw new RuntimeException("Point with id does not exist : " + id);
				} else {
					Point2D point = positions.get(id);
					if (point != null) {
						result.add(point);
					}
				}
			}
		}
		return result;
	}

	private Properties getProperties() {
		if (properties == null) {
			Resource resource = resourceLoader.getResource(configuration);
			try {
				InputStream is = resource.getInputStream();
				properties = new Properties();
				properties.load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	private List<Point2D> getTrajectory(int configurationId, int index) {
		String prefix;
		MatchSide side = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider).getSide();
		switch (side) {
		case RED:
			prefix = "green-";
			break;
		default:
			prefix = "red-";
			break;
		}

		List<Point2D> results = new ArrayList<Point2D>();
		ITrajectoryService trajectory = servicesProvider.getService(ITrajectoryService.class);
		INavigationMap map = trajectory.getNavigationMap();
		Map<String, Location> locations = new HashMap<String, Location>();
		for (Location l : map.getLocations()) {
			locations.put(l.getName(), l);
		}
		Properties p = getProperties();
		String s = p.getProperty("trajectory1.0");
		if (s != null) {
			String[] positionIds = StringUtils.split(s, ",");
			for (String id : positionIds) {
				if (id.startsWith("^")) {
					continue;
				}
				// Si une position n'existe pas, on l�ve une exception, pour
				// �tre s�r de ne pas passer � c�t�
				if (!locations.containsKey(prefix + id)) {
					throw new RuntimeException("Point with id does not exist : " + id);
				} else {

					Location l = locations.get(prefix + id);
					if (l != null) {
						results.add(l.getPosition());
					}
				}
			}
		}
		return results;
	}

	public int getTrajectoryCardId() {
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		Integer id = (Integer) data.get("analyzedCard");
		if (id == null) {
			return 0;
		} else {
			return id;
		}
	}

	@Override
	public void onGameBoardEvent(IGameBoardEvent event) {
		if (listener != null) {
			listener.onEvent(new EventObject(this));
		}
	}

	public void remove(OpponentPositionView opponentPosition) {
		Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class, servicesProvider);
		opponent.removeMovement(opponentPosition.getMovement());
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public void setElementsConfigurationCard1(int cardId) {
		if (elementsConfigurationCard1 == cardId) {
			return;
		}
		elementsConfigurationCard1 = cardId;
		setMatchData(KEY_CARD1, cardId);
	}

	public void setElementsConfigurationCard2(int cardId) {
		if (elementsConfigurationCard2 == cardId) {
			return;
		}
		elementsConfigurationCard2 = cardId;
		setMatchData(KEY_CARD2, cardId);
	}

	private void setMatchData(String key, int value) {
		MatchData2010 data = getMatchData();
		if (data != null) {
			data.setProperty(key, Integer.toString(value));
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		this.servicesProvider = provider;
	}

	public void updateView() {
		// rebuild positions
		buildPositionsMap();
		// clear existing configurations
		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		List<IGameBoardElement> elements = gameBoard.getElements();
		Iterator<IGameBoardElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			IGameBoardElement element = iterator.next();
			String name = element.getName();
			if (name.equals(RANDOM_CORN_ELEMENT)) {
				iterator.remove();
			}
		}
		// obtain new configuration positions
		List<Point2D> positions = new ArrayList<Point2D>();
		positions.addAll(getPositions(1, elementsConfigurationCard1));
		positions.addAll(getPositions(2, elementsConfigurationCard2));
		// put elements on the game board
		for (Point2D p : positions) {
			elements.add(new Corn(RANDOM_CORN_ELEMENT, p, true));
			Point2D pg = new Point2D.Double(p.getX(), GameBoard2010.BOARD_HEIGHT - p.getY());
			elements.add(new Corn(RANDOM_CORN_ELEMENT, pg, true));
		}
	}
}
