package org.cen.navigation;

import org.cen.geom.Point2D;

public class Triangulation {

    public Point2D anglesToPos(double alpha1, double alpha2, double alpha3, double L, double l) {
        // orientation
        // thetar=atan(((l*ta3+2*L)*(ta2-ta1)-2*l*ta1*(ta2-ta3))/(2*l*(ta2-ta3)-(l+2*L*ta3)*(ta2-ta1)));
        double thetar = -alpha1
                - Math.atan((-l * Math.tan(alpha1 - alpha3) + 2 * L)
                        * Math.tan(alpha1 - alpha2)
                        / (2 * L * Math.tan(alpha1 - alpha2) * Math.tan(alpha1 - alpha3) - l
                                * Math.tan(alpha1 - alpha2) + 2 * l * Math.tan(alpha1 - alpha3)));
        if (thetar < 0)
            thetar = 2 * Math.PI + thetar;
        // position
        double x = -l * Math.tan(thetar + alpha1) / (Math.tan(thetar + alpha2) - Math.tan(thetar + alpha1));
        double y = L + l * Math.tan(thetar + alpha1) * Math.tan(thetar + alpha2)
                / (Math.tan(thetar + alpha1) - Math.tan(thetar + alpha2));
        return new Point2D.Double(x, y);
    }

    public double[] posToAngles(double x, double y, double theta, double L, double l) {
        double alpha1 = Math.atan((L - y) / (l - x)) - theta;
        double alpha2 = Math.atan((L - y) / (-x)) - theta;
        double alpha3 = Math.atan((-y) / ((l / 2) - x)) - theta;
        // translation de 180� de alpha2
        if (alpha1 < 0)
            alpha1 = 2 * Math.PI + alpha1;
        if (alpha2 + theta < 0)
            alpha2 = alpha2 + Math.PI;
        if (alpha2 < 0)
            alpha2 = 2 * Math.PI + alpha2;
        if (alpha3 + theta < 0)
            alpha3 = 2 * Math.PI + alpha3;
        else
            alpha3 = alpha3 + Math.PI;
        if (alpha3 < 0)
            alpha3 = 2 * Math.PI + alpha3;
        return new double[] { alpha1, alpha2, alpha3 };
    }
}
