package org.cen.vision.util;

import java.awt.geom.Point2D;
import java.util.EventListener;

public interface TargetListener extends EventListener {
	public void targetFound(Point2D position, double weight);

	public void targetFailure();
}
