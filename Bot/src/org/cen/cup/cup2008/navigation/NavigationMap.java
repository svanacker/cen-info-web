package org.cen.cup.cup2008.navigation;

import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.cen.navigation.AbstractNavigationMap;
import org.cen.navigation.Location;
import org.cen.navigation.PathVectorLine;
import org.cen.ui.gameboard.IGameBoardService;
import org.springframework.beans.factory.annotation.Required;

public class NavigationMap extends AbstractNavigationMap {
	private IGameBoardService gameBoard;

	private double gridSize;

	private void buildGrid() {
		Rectangle2D bounds = gameBoard.getGameplayBounds();
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		int cx = (int) (width / gridSize) + 1;
		int cy = (int) (height / gridSize) + 1;
		Location[] l = new Location[cx * cy];
		for (int i = 0; i < cx; i++)
			for (int j = 0; j < cy; j++) {
				l[i * cy + j] = new Location("x" + i + " y" + j, (int) (i * gridSize), (int) (j * gridSize));
				addLocation(l[i * cy + j]);
			}

		for (int x = 0; x < cx; x++)
			for (int y = 0; y < cy; y++)
				for (int j = 0; j <= 1; j++)
					for (int k = -1; k <= 1; k++) {
						if (j == 0 && k <= 0)
							continue;
						int xx = x + k;
						int yy = y + j;
						if (xx >= 0 && xx < cx && yy >= 0 && yy < cy && !(j == 0 && k == 0))
							addVector(l[x * cy + y], l[xx * cy + yy]);
					}
	}

	private void buildGrid2() {
		addLocation(new Location("red-start", 0, 0));
		addLocation(new Location("blue-start", 0, 3000));
		addLocation(new Location("red1", 100, 700));
		addLocation(new Location("red2", 300, 700));
		addLocation(new Location("red3", 600, 700));
		addLocation(new Location("blue1", 100, 2300));
		addLocation(new Location("blue2", 300, 2300));
		addLocation(new Location("blue3", 600, 2300));
		addLocation(new Location("launch red", 1800, 700));
		addLocation(new Location("launch blue", 1800, 2300));

		HashMap<String, Location> map = new HashMap<String, Location>();
		Collection<Location> locations = getLocations();
		for (Location location : locations) {
			map.put(location.getName(), location);
		}

		addPath(map, "red-start", "red3");
		addPath(map, "blue-start", "blue3");

		addPath(map, "red1", "red2");
		addPath(map, "red2", "red3");

		addPath(map, "blue1", "blue2");
		addPath(map, "blue2", "blue3");

		addPath(map, "red2", "blue2");
		addPath(map, "red3", "blue3");

		addPath(map, "red3", "launch red");
		addPath(map, "blue3", "launch blue");
	}

	private void addPath(HashMap<String, Location> map, String origin, String... destinations) {
		for (String destination : destinations) {
			Location o = map.get(origin);
			Location d = map.get(destination);
			if (o != null && d != null) {
				PathVectorLine v = addVector(o, d);
				double angle = Math.atan2(d.getY() - o.getY(), d.getX() - o.getX());
				v.setType((int) (angle * 100));
			}
		}
	}

	@PostConstruct
	public void initialize() {
		buildGrid2();
	}

	@Required
	public void setGameBoard(IGameBoardService gameBoard) {
		this.gameBoard = gameBoard;
	}

	@Required
	public void setGridSize(double gridSize) {
		this.gridSize = gridSize;
	}
}
