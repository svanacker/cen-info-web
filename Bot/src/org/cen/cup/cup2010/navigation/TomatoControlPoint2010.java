package org.cen.cup.cup2010.navigation;

import java.awt.geom.Point2D;

public class TomatoControlPoint2010 extends ControlPoint2010 {
	private Tomato2010 tomato;

	private boolean finalPoint;

	public TomatoControlPoint2010(Point2D point, String commands, Tomato2010 tomato, boolean finalPoint) {
		super(point, commands);
		this.tomato = tomato;
		this.finalPoint = finalPoint;
	}

	public Tomato2010 getTarget() {
		return tomato;
	}

	@Override
	public boolean isEnabled() {
		return tomato.isAvailable();
	}

	public boolean isFinalPoint() {
		return finalPoint;
	}

	@Override
	public boolean mustStop() {
		return finalPoint;
	}
}
