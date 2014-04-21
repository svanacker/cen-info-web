package org.cen.cup.cup2011.gameboard.configuration;

import java.awt.geom.Point2D;
import java.util.List;

public interface IGameboardAnalysisListener {
	public void finished(List<Point2D> points);
}
