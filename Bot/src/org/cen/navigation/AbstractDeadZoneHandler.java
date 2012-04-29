package org.cen.navigation;

import java.awt.geom.Point2D;
import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceProvider;

public abstract class AbstractDeadZoneHandler implements Runnable {
	private int weightDecay;

	private int collisionWeight;

	private int collsionRadius;

	private boolean active;

	private boolean enabled;

	private IRobotServiceProvider servicesProvider;

	private Thread thread;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private int collisionMaxWeight;

	public AbstractDeadZoneHandler(IRobotServiceProvider servicesProvider, int pCollisionRadius, int pCollsionWeight, int pCollisionMaxWeight, int pWeightDecay) {
		super();
		LOGGER.config("initializing dead zone handler");
		this.servicesProvider = servicesProvider;
		active = true;
		this.collsionRadius = pCollisionRadius;
		this.collisionWeight = pCollsionWeight;
		this.collisionMaxWeight = pCollisionMaxWeight;
		this.weightDecay = pWeightDecay;
		thread = new Thread(this, getClass().getSimpleName());
		thread.start();
	}

	public void handleDeadZone(Point2D position) {
		// Mise à jour des poids de la carte de navigation
		ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
		trajectoryService.getNavigationMap().updateWeights(position, collsionRadius, collisionWeight, collisionMaxWeight);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void run() {
		LOGGER.fine("starting dead zone handler thread");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (active) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (active && enabled) {
				ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
				trajectoryService.getNavigationMap().decayWeights(weightDecay);
			}
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void shutdown() {
		active = false;
		thread.interrupt();
	}
}
