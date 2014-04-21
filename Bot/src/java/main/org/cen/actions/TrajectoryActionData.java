package org.cen.actions;

import java.awt.geom.Point2D;

public class TrajectoryActionData {

    public Point2D position;

    public IGameAction action;

    public IGameActionHandler handler;

    public TrajectoryActionData(Point2D position, IGameAction action, IGameActionHandler handler) {
        super();
        this.position = position;
        this.action = action;
        this.handler = handler;
    }
}
