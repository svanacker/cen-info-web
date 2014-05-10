package org.cen.cup.cup2011.ui.web;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.cen.cup.cup2011.gameboard.elements.Pawn;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.match.Opponent;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;
import org.cen.ui.gameboard.IGameBoardService;

// TODO changer l'encapsulation
public class ElementsPositionsView2011 implements ElementListener {
	private Element[][] elements;

	private int rowsCount = 11;

	private int columnsCount = 10;

	private IRobotServiceProvider provider;

	private ElementType elementType;

	public ElementsPositionsView2011() {
		super();
	}

	@Override
	public void changed(Element element) {
		switch (elementType) {
		case OPPONENT:
			opponentChanged(element);
			break;
		case PAWNS:
			pawnsChanged(element);
			break;
		}
	}

	public int getColumnsCount() {
		return columnsCount;
	}

	public Element[][] getElements() {
		return elements;
	}

	private Element getNewElement(String name, ElementsPositionsView2011 elementsPositionsView2011) {
		switch (elementType) {
		case OPPONENT:
			return new OpponentElement(name, this);
		case PAWNS:
			return new Element(name, this);
		}
		return null;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	@PostConstruct
	public void initialize() {
		elements = new Element[rowsCount][];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = new Element[columnsCount];
			for (int j = 0; j < elements[i].length; j++) {
				String name = (char) (j + 'A') + String.valueOf(i + 3);
				elements[i][j] = getNewElement(name, this);
			}
		}
	}

	private void opponentChanged(Element element) {
		ITrajectoryService trajectory = provider.getService(ITrajectoryService.class);
		INavigationMap map = trajectory.getNavigationMap();
		Map<String, Location> locations = map.getLocationsMap();
		Location l = locations.get(element.getName());
		if (l != null) {
			Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class, provider);
			opponent.setLastLocation(l.getPosition());
		}
	}

	private void pawnsChanged(Element element) {
		IGameBoardService gameboard = provider.getService(IGameBoardService.class);
		ITrajectoryService trajectory = provider.getService(ITrajectoryService.class);
		INavigationMap map = trajectory.getNavigationMap();
		if (element.getValue()) {
			String name = element.getName();
			Location l = map.getLocationsMap().get(name);
			if (l != null) {
				Pawn pawn = new Pawn(l.getPosition(), 1d);
				gameboard.getElements().add(pawn);
			}
		} else {
			gameboard.removeElements(element.getName());
		}
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		this.provider = provider;
	}
}
