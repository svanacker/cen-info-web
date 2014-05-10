package org.cen.navigation;

import org.cen.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;

/**
 * Carte de navigation.
 * 
 * @author Emmanuel ZURMELY
 */
public abstract class AbstractNavigationMap implements INavigationMap {

	/**
	 * Poids maximal des chemins. Ne pas utiliser de valeur supérieure.
	 */
	public final static int MAX_WEIGHT = 1000000;

	/**
	 * Collection des noeuds.
	 */
	protected Map<String, Location> locationsMap = new HashMap<String, Location>();

	/**
	 * Collection des chemins.
	 */
	protected Collection<IPathVector> vectors = new ArrayList<IPathVector>();

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	public AbstractNavigationMap() {
		super();
	}

	@Override
	public void addLocation(Location location) {
		locationsMap.put(location.getName(), location);
	}

	public void addLocation(String locationName, double x, double y) {
		addLocation(locationName, (int) x, (int) y);
	}

	public void addLocation(String locationName, int x, int y) {
		Location location = new Location(locationName, x, y);
		addLocation(location);
	}

	@Override
	public void addPath(String... locations) {
		Location begin;
		Location end = null;
		for (String s : locations) {
			begin = end;
			end = locationsMap.get(s);
			if (end == null) {
				logInvalidLocation(s);
			} else if (begin != null) {
				PathVectorLine v = addVector(begin, end);
				double angle = Math.atan2(end.getY() - begin.getY(), end.getX()
						- begin.getX());
				v.setType((int) (angle * 100));
			}
		}
	}

	public void addSplinePath(String location1, String location2,
			int cpDistance1, int cpDistance2, double angle1, double angle2) {
		Location start = locationsMap.get(location1);
		if (start == null) {
			logInvalidLocation(location1);
		}
		Location end = locationsMap.get(location2);
		if (end == null) {
			logInvalidLocation(location2);
		}
		addVector(new PathVectorSpline(start, end, cpDistance1, cpDistance2,
				angle1, angle2));
	}

	@Override
	public void addVector(IPathVector v) {
		vectors.add(v);
	}

	@Override
	public PathVectorLine addVector(Location l1, Location l2) {
		PathVectorLine v = new PathVectorLine(l1, l2);
		addVector(v);
		return v;
	}

	@Override
	public void decayWeights(int value) {
		for (IPathVector vector : vectors) {
			if (vector instanceof PathVectorLine) {
				PathVectorLine v = (PathVectorLine) vector;
				int weight = v.getWeight();
				weight -= value;
				if (weight < 0) {
					weight = 0;
				}
				v.setWeight(weight);
			}
		}
	}

	@Override
	public Collection<Location> getLocations() {
		return locationsMap.values();
	}

	@Override
	public Map<String, Location> getLocationsMap() {
		return locationsMap;
	}

	@Override
	public Location getNearestLocation(Point2D position) {
		Location p = new Location("tmp", (int) position.getX(),
				(int) position.getY());
		Location nearest = null;
		int min = Integer.MAX_VALUE;
		for (IPathVector v : vectors) {
			for (Location l : v.getLocations()) {
				int d = l.getDistance(p);
				if (d < min) {
					min = d;
					nearest = l;
				}
			}
		}
		return nearest;
	}

	@Override
	public IPathVector getPathVector(Location l1, Location l2) {
		for (IPathVector v : vectors) {
			if (v.containsLocation(l1) && v.getOtherEnd(l1) == l2) {
				return v;
			}
		}
		return null;
	}

	@Override
	public Collection<IPathVector> getPathVectors() {
		return vectors;
	}

	protected void logInvalidLocation(String s) {
		LOGGER.severe("Unknown location: " + s);
	}

	@Override
	public void reset() {
		LOGGER.config("Resetting the navigation map");
		vectors.clear();
		locationsMap.clear();
	}

	@Override
	public void updateWeights(Point2D position, double radius,
			int additionalWeight, int maxWeight) {
		Location l = new Location("tmp", (int) position.getX(),
				(int) position.getY());
		for (IPathVector v : vectors) {
			Location l1 = v.getStart();
			Location l2 = v.getEnd();

			int d1 = l1.getDistance(l);
			int d2 = l2.getDistance(l);
			if (d1 < radius || d2 < radius) {
				if (v instanceof PathVectorLine) {
					PathVectorLine vector = (PathVectorLine) v;
					int weight = vector.getWeight();
					weight += additionalWeight;
					vector.setWeight(Math.min(weight, maxWeight));
				}
			}
		}
	}
}
