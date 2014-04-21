package org.cen.robot.control;

import org.cen.robot.brain.MotionProfileType;

/**
 * Encapsulates data which give the profile of the instruction.
 */
public class MotionInstructionData {

	/** Index of instruction (alpha / theta). */
	protected int index;

	/** the position which must be reached when using classic implementation */
	protected float nextPosition;

	/** The acceleration which is asked : > 0 */
	protected float a;

	/** The maximal speed which is asked : > 0 */
	protected float speed;

	/** The maximal speed which can be reached : negative or positive */
	protected float speedMax;

	/** The acceleration time > 0 */
	protected float t1;

	/** The time before deceleration > 0 */
	protected float t2;

	/** The time after deceleration > 0 */
	protected float t3;

	/** The first position after accceleration time */
	protected float p1;

	/** The first position after speed constant */
	protected float p2;

	/** The type of the trajectory (TRAPEZE / TRIANGLE) */
	protected MotionProfileType profileType;

	/** The type of motion (GO, ROTATION, MAINTAIN_POSITION). */
	protected PIDMotionType motionType;

	/** The type of pid which must be used. */
	protected PIDType pidType;

	public MotionInstructionData() {

	}

	public void load(MotionInstructionData other) {
		nextPosition = other.getNextPosition();
		a = other.getA();
		speed = other.getSpeed();
		speedMax = other.getSpeedMax();
		t1 = other.getT1();
		t2 = other.getT2();
		t3 = other.getT3();
		p1 = other.getP1();
		p2 = other.getP2();
		profileType = other.getProfileType();
		motionType = other.getMotionType();
		pidType = other.getPidType();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public float getNextPosition() {
		return nextPosition;
	}

	public void setNextPosition(float nextPosition) {
		this.nextPosition = nextPosition;
	}

	public float getA() {
		return a;
	}

	public void setA(float a) {
		this.a = a;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeedMax() {
		return speedMax;
	}

	public void setSpeedMax(float speedMax) {
		this.speedMax = speedMax;
	}

	public float getT1() {
		return t1;
	}

	public void setT1(float t1) {
		this.t1 = t1;
	}

	public float getT2() {
		return t2;
	}

	public void setT2(float t2) {
		this.t2 = t2;
	}

	public float getT3() {
		return t3;
	}

	public void setT3(float t3) {
		this.t3 = t3;
	}

	public float getP1() {
		return p1;
	}

	public void setP1(float p1) {
		this.p1 = p1;
	}

	public float getP2() {
		return p2;
	}

	public void setP2(float p2) {
		this.p2 = p2;
	}

	public MotionProfileType getProfileType() {
		return profileType;
	}

	public void setProfileType(MotionProfileType profileType) {
		this.profileType = profileType;
	}

	public PIDMotionType getMotionType() {
		return motionType;
	}

	public void setMotionType(PIDMotionType motionType) {
		this.motionType = motionType;
	}

	public PIDType getPidType() {
		return pidType;
	}

	public void setPidType(PIDType pidType) {
		this.pidType = pidType;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[nextPosition=" + nextPosition + ", a=" + a + ", speed=" + speed + ", speedMax="
				+ speedMax + ", t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + ", p1=" + p1 + ", p2=" + p2 + ", profileType="
				+ profileType + ", motionType=" + motionType + ", pidType=" + pidType + "]";
	}
}
