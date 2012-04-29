package org.cen.cup.cup2011.simulGameboard;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.cen.cup.cup2011.gameboard.GameBoard2011;
import org.cen.cup.cup2011.robot.match.MatchData2011;
import org.cen.cup.cup2011.simulGameboard.elements.SimulPawnElement;
import org.cen.cup.cup2012.gameboard.elements.StartArea2012;
import org.cen.robot.IRobotServiceInitializable;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.simulRobot.match.event.AMovingHandlerEvent;
import org.cen.simulRobot.match.simulMoving.ISimulMovingHandler;
import org.cen.simulRobot.match.simulMoving.SimulMovingHandler;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.ISimulGameBoard;
import org.cen.ui.gameboard.elements.SimulOpponentElement;
import org.cen.ui.gameboard.elements.SimulRobotElement;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * SimulGameboard for the cup 2011.
 * 
 * @author Benouamer Omar
 */
public class SimulGameBoard2011 extends GameBoard2011 implements ISimulGameBoard, IRobotServiceInitializable, ResourceLoaderAware {

	private static final String PREFIX_MATCH = "match";

	private static final String SWITCHES = "switches";

	private String configuration;

	private Properties properties;

	private ResourceLoader resourceLoader;

	protected ISimulMovingHandler simulMovingHandler;

	public SimulGameBoard2011() {
		super();
	}

	@Override
	public void addElement(IGameBoardElement typeElement) {
		getElements().add(typeElement);
	}

	private void addElements() {
		MatchData data = getMatchData();
		// Opponent
		SimulOpponentElement opponentElemement = new SimulOpponentElement(properties, data);
		addElement(opponentElemement);
		// robot
		SimulRobotElement robotElement = new SimulRobotElement(properties, data);
		addElement(robotElement);
		// Pions
		addPawns();
	}

	@Override
	public void addElements(List<IGameBoardElement> typeElements) {
		getElements().addAll(typeElements);
	}

	private void addPawns() {
		// pions central
		getElements().add(
				new SimulPawnElement(
						new Point2D.Double(BOX_SIZE * 3, StartArea2012.START_AREA_WIDTH + BAND_WIDTH + BOX_SIZE * 3), 0));

		int random;
		int position1;
		int position2;
		// Au hasard
		for (int i = 1; i < 6; i++) {
			if (i == 1) {
				position1 = (1) + 1;
				position2 = (2) + 1;
				getElements().add(
						new SimulPawnElement(new Point2D.Double(BOX_SIZE * position1, StartArea2012.START_AREA_WIDTH + BAND_WIDTH
								+ BOX_SIZE * i), 0));
				getElements().add(
						new SimulPawnElement(new Point2D.Double(BOX_SIZE * position2, StartArea2012.START_AREA_WIDTH + BAND_WIDTH
								+ BOX_SIZE * i), 0));
			}
			if (i != 3 && i != 1) {
				position1 = (int) (Math.random() * 4) + 1;
				position2 = (int) (Math.random() * 4) + 1;
				if (position1 == position2) {
					random = (int) (Math.random()) * 2;
					position2 = position2 + random - 1;
					if (position2 == 0) {
						position2 = 5;
					}
				}
				getElements().add(
						new SimulPawnElement(new Point2D.Double(BOX_SIZE * position1, StartArea2012.START_AREA_WIDTH + BAND_WIDTH
								+ BOX_SIZE * i), 0));
				getElements().add(
						new SimulPawnElement(new Point2D.Double(BOX_SIZE * position2, StartArea2012.START_AREA_WIDTH + BAND_WIDTH
								+ BOX_SIZE * i), 0));
			}
		}
	}

	@Override
	public void afterRegister() {
		System.out.println("appel du afterRegister() dans SimulGameBoard2011");
		readConfiguration();
		addElements();
		this.simulMovingHandler = new SimulMovingHandler(servicesProvider);
	}

	public String getConfiguration() {
		return configuration;
	}

	@Override
	public <E extends IGameBoardElement> List<E> getElements(Class<E> typeElement) {
		List<E> elements = new ArrayList<E>();
		Iterator<IGameBoardElement> it = getElements().iterator();
		for (IGameBoardElement element : getElements()) {
			if (element.getClass().equals(typeElement)) {
				elements.add(typeElement.cast(element));
			}
		}
		return elements;
	}

	private MatchData getMatchData() {
		MatchData data = new MatchData2011();
		data.setFromProperties(properties, PREFIX_MATCH + ".");
		String aside = properties.getProperty(SWITCHES + ".8");
		data.setSide(MatchSide.VIOLET);
		if (aside.equals("0")) {
			data.setSide(MatchSide.RED);
		}
		return data;
	}

	@Override
	public ISimulMovingHandler getMovingHandler() {
		return simulMovingHandler;
	}

	@Override
	public void notifyEvent(IGameBoardEvent event) {
		if (event instanceof AMovingHandlerEvent) {
			simulMovingHandler.handleEvent((AMovingHandlerEvent) event);
		}
	}

	protected void readConfiguration() {

		if (resourceLoader == null) {
			return;
		}
		Resource r = resourceLoader.getResource(getConfiguration());
		if (r == null) {
			return;
		}
		InputStream is = null;
		try {
			is = r.getInputStream();
			properties = new Properties();
			properties.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void removeElement(IGameBoardElement typeElement) {
		getElements().remove(typeElement);
	}

	@Override
	public <E extends IGameBoardElement> void removeElements(List<E> typeElement) {
		getElements().removeAll(typeElement);
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	@Override
	public void setResourceLoader(ResourceLoader pResourceLoader) {
		resourceLoader = pResourceLoader;
	}

	public void shutdown() {
		if (simulMovingHandler != null) {
			simulMovingHandler.shutdown();
			simulMovingHandler = null;
		}
	}
}
