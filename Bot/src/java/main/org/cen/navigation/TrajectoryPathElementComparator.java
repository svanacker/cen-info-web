package org.cen.navigation;

import org.cen.geom.Point2D;
import java.util.Comparator;

public class TrajectoryPathElementComparator implements Comparator<TrajectoryPathElement> {

    private final Point2D start;

    public TrajectoryPathElementComparator(Point2D start) {
        super();
        this.start = start;
    }

    @Override
    public int compare(TrajectoryPathElement o1, TrajectoryPathElement o2) {
        Point2D position1 = o1.getPosition();
        double d1 = position1.distance(start);

        Point2D position2 = o2.getPosition();
        double d2 = position2.distance(start);

        int result = Double.compare(d1, d2);

        return result;
    }
}
