package org.cen.navigation;

import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.elements.BezierCurve;

public class PathVectorSpline extends AbstractPathVector {

	protected double cpDistance1;

	protected double cpDistance2;

	protected double angle1;

	protected double angle2;

	public PathVectorSpline(Location l1, Location l2, double cpDistance1, double cpDistance2, double angle1, double angle2) {
		super(l1, l2);
		this.cpDistance1 = cpDistance1;
		this.cpDistance2 = cpDistance2;
		this.angle1 = angle1;
		this.angle2 = angle2;
	}

	public double getControlPointDistance(Location l) {
		if (l == l1) {
			return cpDistance1;
		} else if (l == l2) {
			return cpDistance2;
		} else {
			return 0;
		}
	}

	public double getControlPointAngle(Location l) {
		if (l == l1) {
			return angle1;
		} else if (l == l2) {
			return angle2;
		} else {
			return 0;
		}
	}

	@Override
	public double getCost() {
		// TODO mettre la longueur de la courbe de Bézier
		return l1.getDistance(l2);
	}

	@Override
	public IGameBoardElement getGameBoardElement(String name) {
		if (name == null || name.isEmpty()) {
			name = l1.name + "-" + l2.name;
		}
		return new BezierCurve(name, l1.getPosition(), l2.getPosition(), cpDistance1, cpDistance2, angle1, angle2);
	}
}
