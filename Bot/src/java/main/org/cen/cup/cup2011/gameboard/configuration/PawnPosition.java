package org.cen.cup.cup2011.gameboard.configuration;

import org.cen.geom.Point2D;
import java.util.EnumSet;
import java.util.Set;

public class PawnPosition {
	private static final double PRESENCE_THRESHOLD = 0.01d;

	private Point2D position;

	private Set<PawnConfiguration> configuration;

	private long timeStamp;

	private double presenceProbability;

	public PawnPosition(Point2D position) {
		this(position, EnumSet.of(PawnConfiguration.SINGLE));
	}

	public PawnPosition(Point2D position, Set<PawnConfiguration> configuration) {
		super();
		this.position = position;
		this.configuration = configuration;
		this.timeStamp = System.currentTimeMillis();
		this.presenceProbability = 1d;
	}

	public void adjustPresenceProbability(double factor, double value) {
		double d = presenceProbability;
		d *= factor;
		d += value;
		if (d > 1) {
			presenceProbability = 1d;
		} else if (d < 0) {
			presenceProbability = 0.0d;
		} else {
			presenceProbability = d;
		}
	}

	public Set<PawnConfiguration> getConfiguration() {
		return configuration;
	}

	public Point2D getPosition() {
		return position;
	}

	public double getPresenceProbability() {
		return presenceProbability;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * return true if there is a Pawn on the square
	 * 
	 * @return
	 */
	public boolean isPresent() {
		/* 
		 * ****************************** prepare test presence des pions
		 * *******************************
		 */
		EnumSet<PawnConfiguration> s = EnumSet.of(PawnConfiguration.SINGLE, PawnConfiguration.DOUBLE, PawnConfiguration.KING, PawnConfiguration.QUEEN);
		s.retainAll(configuration);
		/*  ****************************** fin ******************************* */
		return presenceProbability > PRESENCE_THRESHOLD && !s.isEmpty();
	}

	public void setConfiguration(Set<PawnConfiguration> configuration) {
		this.configuration = configuration;
	}

	public void setPresenceProbability(double presenceProbability) {
		this.presenceProbability = presenceProbability;
	}

	@Override
	public String toString() {
		return "PawnPosition [position=" + position + ", presenceProbability=" + presenceProbability + "]";
	}
}
