package org.cen.robot.device.motor;

/**
 * Encapsulates information about a motor
 * @author svanacker
 */
public class MotorData {

	protected int position;
	
	public MotorData() {
		
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}
}
