package org.cen.navigation.recorder;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.cen.navigation.INavigationMap;
import org.cen.navigation.IPathVector;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.navigation.RotationRequest;
import org.cen.util.Holder;

/**
 * Service responsible for computing the trajectories used by the navigation
 * service.
 * 
 * @author Emmanuel ZURMELY
 */
public class TrajectoryService implements ITrajectoryService {

    /**
     * Value used by angle comparisons.
     */
    private static final double EPSILON = 0.0001;

    /**
     * Computes the angle needed to be well positioned between two locations.
     * 
     * @return the angle between two locations
     */
    protected static double getAngle(Location start, Location end) {
        double xDiff = end.getX() - start.getX();
        double yDiff = end.getY() - start.getY();

        return Math.atan2(yDiff, xDiff);
    }

    /**
     * Computes the angle needed to be well positioned between two locations.
     * 
     * @return the angle between two locations
     */
    protected static double getAngle(Point2D start, Point2D end) {
        double xDiff = end.getX() - start.getX();
        double yDiff = end.getY() - start.getY();

        return Math.atan2(yDiff, xDiff);
    }

    /**
     * Minimum angle value handled when computing the trajectory.
     */
    private double rotationAngleThreshold = Math.toRadians(1);

    private INavigationMap map;

    private IRobotServiceProvider serviceProvider;

    @Override
    public double buildTrajectoryRequests(List<Point2D> path, double orientation, List<RobotDeviceRequest> requests,
            boolean backward) {
        double previousAngle = orientation;
        int distance = 0;
        Point2D currentLocation = path.get(0);
        // Loop the path
        for (Point2D location : path) {
            if (!currentLocation.equals(location)) {
                // Determines if we must rotate the robot
                double angle = getAngle(currentLocation, location);
                if (backward) {
                    angle += Math.PI;
                }

                double diffAngle = (angle - previousAngle) % (Math.PI * 2d);

                // Rotation of the robot
                if (diffAngle >= Math.PI - EPSILON) {
                    diffAngle -= 2d * Math.PI;
                } else if (diffAngle <= -Math.PI + EPSILON) {
                    diffAngle += 2d * Math.PI;
                }

                // The robot must rotate
                if (Math.abs(diffAngle) > rotationAngleThreshold) {
                    // We create a move instruction with the accumulated
                    // distance
                    if (distance > 0) {
                        MoveRequest instruction = new MoveRequest(distance);
                        requests.add(instruction);
                        // Resets distance
                        distance = 0;
                    }

                    RotationRequest instruction = new RotationRequest(diffAngle);
                    requests.add(instruction);
                    orientation += diffAngle;
                }
                // Go from current Point to next Point
                distance += location.distance(currentLocation);
                previousAngle = angle;
            }
            currentLocation = location;
        }
        // At the end, we add the last instruction
        if (distance > 0) {
            if (backward) {
                distance = -distance;
            }
            MoveRequest instruction = new MoveRequest(distance);
            requests.add(instruction);
        }

        return orientation % (Math.PI * 2d);
    }

    private Location extractMin(Set<Location> unhandled, Set<Location> handled, Map<Location, Integer> distances) {
        // Recherche le noeud le plus proche en terme de coût
        Location min = null;
        int dmin = Integer.MAX_VALUE;
        for (Location l : unhandled) {
            int d = getCost(distances, l);
            if (d <= dmin) {
                dmin = d;
                min = l;
            }
        }
        unhandled.remove(min);
        handled.add(min);
        return min;
    }

    private int getCost(Map<Location, Integer> costs, Location l) {
        // Recherche le coût absolu au noeud spécifié
        Integer d = costs.get(l);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d.intValue();
        }
    }

    @Override
    public INavigationMap getNavigationMap() {
        return map;
    }

    private Collection<IPathVector> getOutgoingVectors(Set<Location> handled, Location l) {
        Collection<IPathVector> c = new ArrayList<IPathVector>();
        for (IPathVector v : map.getPathVectors()) {
            if (v.containsLocation(l) && !handled.contains(v.getOtherEnd(l))) {
                c.add(v);
            }
        }
        return c;
    }

    /**
     * Renvoie la liste des positions constituant le chemin le plus court
     * reliant les deux positions spécifiées.
     * 
     * @param start
     *            le point de départ
     * @param end
     *            le point d'arrivée
     * @return la liste des noeuds sur le chemin le plus court entre le départ
     *         et l'arrivée spécifiés
     */
    @Override
    public synchronized List<Location> getPath(Location start, Location end) {
        Holder<Integer> cost = new Holder<Integer>(0);
        return getPath(start, end, cost);
    }

    /**
     * Renvoie la liste des positions constituant le chemin le plus court
     * reliant les deux positions spécifiées.
     * 
     * @param start
     *            le point de départ
     * @param end
     *            le point d'arrivée
     * @param cost
     *            le coût de la trajectoire
     * @return la liste des noeuds sur le chemin le plus court entre le départ
     *         et l'arrivée spécifiés
     */
    @Override
    public synchronized List<Location> getPath(Location start, Location end, Holder<Integer> cost) {
        Set<Location> locations = new HashSet<Location>();
        for (IPathVector v : map.getPathVectors()) {
            locations.add(v.getStart());
            locations.add(v.getEnd());
        }

        Set<Location> unhandled = new HashSet<Location>(locations);
        Set<Location> handled = new HashSet<Location>();
        Map<Location, Location> previous = new HashMap<Location, Location>();
        Map<Location, Integer> costs = new HashMap<Location, Integer>();

        Location l1;

        costs.put(start, Integer.valueOf(0));
        while (!unhandled.isEmpty()) {
            // Recherche le noeud le plus proche parmis les noeuds restants
            l1 = extractMin(unhandled, handled, costs);
            // Liste des chemins menant à ce noeud
            Collection<IPathVector> outgoingVectors = getOutgoingVectors(handled, l1);
            for (IPathVector v : outgoingVectors) {
                // Recherche parmi ces chemins du plus court
                Location l2 = v.getOtherEnd(l1);
                int dl1 = getCost(costs, l1);
                int dl2 = getCost(costs, l2);
                int d = dl1 + (int) v.getCost();
                if (d < dl2) {
                    // Affectation de la distance absolue du noeud
                    setCost(costs, l2, d);
                    setPrevious(previous, l2, l1);
                }
            }
        }

        // Coût
        cost.setValue(getCost(costs, end));
        // Liste des noeuds du résultat
        List<Location> path = new ArrayList<Location>();
        l1 = end;
        while (l1 != start && l1 != null) {
            path.add(l1);
            l1 = getPrevious(previous, l1);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

    @Override
    public synchronized List<Location> getPath(Point2D origin, Point2D destination) {
        Location start = map.getNearestLocation(origin);
        Location end = map.getNearestLocation(destination);
        return getPath(start, end);
    }

    @Override
    public List<Point2D> getPathFromLocations(List<Location> currentPath) {
        List<Point2D> points = new ArrayList<Point2D>();
        for (Location l : currentPath) {
            points.add(l.getPosition());
        }
        return points;
    }

    private Location getPrevious(Map<Location, Location> previous, Location l) {
        return previous.get(l);
    }

    @Override
    public List<RobotDeviceRequest> getRequests(List<Location> path) {
        List<RobotDeviceRequest> list = new ArrayList<RobotDeviceRequest>();

        RobotPosition robotPosition = RobotUtils.getRobotAttribute(RobotPosition.class, serviceProvider);

        Location currentLocation = map.getNearestLocation(robotPosition.getCentralPoint());
        // Get the angle of the robot
        double previousAngle = robotPosition.getAlpha();
        int distance = 0;
        boolean ok = false;
        // Loop through the path
        for (Location location : path) {
            if (ok) {

                // Determined if we must rotate the robot
                double angle = getAngle(currentLocation, location);

                double diffAngle = angle - previousAngle;

                // The robot must rotate
                if (diffAngle != 0) {

                    // We create a move instruction with the accumulated
                    // distance
                    if (distance > 0) {
                        MoveRequest instruction = new MoveRequest(distance);
                        list.add(instruction);
                        // Resets distance
                        distance = 0;
                    }
                    // Rotation of the robot
                    if (diffAngle >= Math.PI) {
                        diffAngle -= 2d * Math.PI;
                    } else if (diffAngle <= -Math.PI) {
                        diffAngle += 2d * Math.PI;
                    }

                    RotationRequest instruction = new RotationRequest(diffAngle);
                    list.add(instruction);
                }
                // Go from current Point to next Point
                distance += location.getDistance(currentLocation);
                previousAngle = angle;
            }
            // As soon as we have the same currentLocation, we can compute the
            // instruction
            if (currentLocation.equals(location)) {
                ok = true;
            } else {
                currentLocation = location;
            }
        }
        // At the end, we add the last instruction
        if (distance > 0) {
            MoveRequest instruction = new MoveRequest(distance);
            list.add(instruction);
        }

        return list;
    }

    @Override
    public double getRotationAngleThreshold() {
        return rotationAngleThreshold;
    }

    public double getRotationAngleThresholdInDegrees() {
        return Math.toDegrees(rotationAngleThreshold);
    }

    @PostConstruct
    public void initialize() {
        if (map != null) {
            map.reset();
        }
    }

    private void setCost(Map<Location, Integer> distances, Location l, int d) {
        distances.put(l, new Integer(d));
    }

    @Override
    public void setNavigationMap(INavigationMap map) {
        this.map = map;
    }

    private void setPrevious(Map<Location, Location> previous, Location l, Location p) {
        previous.put(l, p);
    }

    @Override
    public void setRotationAngleThreshold(double rotationAngleThreshold) {
        this.rotationAngleThreshold = rotationAngleThreshold;
    }

    public void setRotationAngleThresholdInDegrees(double rotationAngleThreshold) {
        this.rotationAngleThreshold = Math.toRadians(rotationAngleThreshold);
    }

    @Override
    public void setServicesProvider(IRobotServiceProvider provider) {
        serviceProvider = provider;
        provider.registerService(ITrajectoryService.class, this);
    }
}
