package org.cen.cup.cup2010.navigation;

import org.cen.geom.Point2D;

public class CornControlPoint2010 extends ControlPoint2010 {
	private Corn2010 corn;

	public CornControlPoint2010(Point2D point, String commands, Corn2010 corn) {
		super(point, commands);
		this.corn = corn;
	}

	public Corn2010 getTarget() {
		return corn;
	}

	@Override
	public boolean isEnabled() {
		return corn.isAvailable();
	}
}
