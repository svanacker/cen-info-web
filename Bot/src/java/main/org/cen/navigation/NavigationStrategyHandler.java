package org.cen.navigation;

import org.cen.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.math.CombinationList;
import org.cen.util.Holder;

/**
 * Handles the navigation strategy.
 * 
 * @author Emmanuel ZURMELY
 */
public class NavigationStrategyHandler {
	private Collection<LocationGroup> locationGroups = new ArrayList<LocationGroup>();

	private ITrajectoryService trajectoryService;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private static final int FACTOR_INTEREST = 500;

	private static final int FACTOR_COST = 1;

	public NavigationStrategyHandler(ITrajectoryService trajectoryService) {
		super();
		this.trajectoryService = trajectoryService;
	}

	public void addLocationGroup(LocationGroup group) {
		locationGroups.add(group);
	}

	public void addLocationGroup(Set<PathSegment> segments, LocationGroup group, String... locations) {
		Map<String, Location> mapLocations = new HashMap<String, Location>();
		for (Location l : trajectoryService.getNavigationMap().getLocations()) {
			mapLocations.put(l.getName(), l);
		}

		Set<PathSegment> groupSegments = group.getSegments();
		Location start = null;
		for (String s : locations) {
			Location end = mapLocations.get(s);
			if (end == null) {
				LOGGER.severe("Unknown location: " + s);
				return;
			}
			if (start != null) {
				PathSegment segment = findSegment(segments, start, end);
				if (segment == null) {
					LOGGER.severe("Invalid segment :" + start.getName() + "," + end.getName());
					return;
				}
				groupSegments.add(segment);
			}
			start = end;
		}
		addLocationGroup(group);
	}

	private LocationGroupStats analyzeGroup(Point2D position, double orientation, LocationGroup group) {
		int interest = group.getInterest();
		Holder<Integer> cost = new Holder<Integer>(0);
		INavigationMap navigationMap = trajectoryService.getNavigationMap();

		List<Location> bestPath = null;
		int bestCost = Integer.MAX_VALUE;

		List<Location> locations = new ArrayList<Location>(group.getLocations());
		int count = locations.size();
		if (count == 0) {
			return new LocationGroupStats(group, Integer.MAX_VALUE, 0, null);
		}
		CombinationList combinations = new CombinationList(count, count);

		int[] key;
		key = combinations.getNextKey();
		while (key != null) {
			// Détermine le coût des distances
			Location start = navigationMap.getNearestLocation(position);
			List<Location> path = new ArrayList<Location>();
			path.add(start);
			int keyCost = 0;
			for (int i = 0; i < key.length; i++) {
				Location end = locations.get(key[i]);
				List<Location> p = trajectoryService.getPath(start, end, cost);
				p.remove(0);
				path.addAll(p);
				start = end;
				keyCost += cost.getValue();
			}

			// Ajoute le coût des rotations
			double lastAngle = orientation;
			start = path.get(0);
			for (int i = 1; i < path.size(); i++) {
				Location end = path.get(i);
				double angle = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX());
				keyCost += ((Math.abs(lastAngle - angle) * 100) % 314) * 2;
				start = end;
			}

			// Conserve la trajectoire la moins coûteuse
			if (keyCost < bestCost) {
				bestCost = keyCost;
				bestPath = path;
			}
			key = combinations.getNextKey();
		}

		return new LocationGroupStats(group, bestCost * FACTOR_COST, interest * FACTOR_INTEREST, bestPath);
	}

	public List<LocationGroupStats> analyzeGroups(Point2D position, double orientation) {
		List<LocationGroupStats> results = new ArrayList<LocationGroupStats>();
		for (LocationGroup group : locationGroups) {
			results.add(analyzeGroup(position, orientation, group));
		}
		Collections.sort(results, new LocationGroupStatsComparator());
		return results;
	}

	private PathSegment findSegment(Set<PathSegment> segments, Location start, Location end) {
		for (PathSegment s : segments) {
			if (s.isSameSegment(start, end)) {
				return s;
			}
		}
		return null;
	}
}
