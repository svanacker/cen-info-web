package org.cen.robot.device.motor;

import java.io.Serializable;

/**
 * Encapsulates a command for a motor.
 */
public class MotorMoveData implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String label;

	protected String moveId;

	protected int moveCode;

	protected int speed;

	public MotorMoveData() {

	}

	public MotorMoveData(String label, String moveId, int moveCode, int speed) {
		super();
		this.label = label;
		this.moveId = moveId;
		this.moveCode = moveCode;
		this.speed = speed;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMoveId() {
		return moveId;
	}

	public void setMoveId(String moveId) {
		this.moveId = moveId;
	}

	public int getMoveCode() {
		return moveCode;
	}

	public void setMoveCode(int moveCode) {
		this.moveCode = moveCode;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}	

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[moveId=" + moveId + ", moveCode="
				+ moveCode + ", label=" + label + ", speed=" + speed + "]";
	}

}
