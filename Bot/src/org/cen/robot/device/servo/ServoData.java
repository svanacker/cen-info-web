package org.cen.robot.device.servo;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import org.cen.logging.LoggingUtils;

/**
 * Encapsulates the data of a servo.
 */
public class ServoData {
	private final static Logger logger = LoggingUtils.getClassLogger();

	/** The id of the servo. */
	private int id;

	/**
	 * The value of a servo. Typically between 1000 and 2000, maybe more or
	 * less.
	 */
	private int value;

	/** The speed which must be used to reach the position. */
	private int speed;

	private final EventListenerList listeners = new EventListenerList();

	/** Default value for the servo. */
	public static final int DEFAULT_VALUE = 1500;
	
	/** Default speed for the servo. */
	public static final int DEFAULT_SPEED = 0;

	public ServoData(int id) {
		super();
		this.id = id;
		this.value = DEFAULT_VALUE;
		this.speed = DEFAULT_SPEED;
	}

	public void addServoChangeListener(ServoChangeListener servoChangeListener) {
		listeners.add(ServoChangeListener.class, servoChangeListener);
	}

	/**
	 * Notify all observers that requires to be notify when pid data changes
	 */
	private void fireServoChangeListener() {
		Object[] l = listeners.getListenerList();
		for (int j = l.length - 2; j >= 0; j -= 2) {
			if (l[j] == ServoChangeListener.class) {
				try {
					((ServoChangeListener) l[j + 1]).onServoChange(this);
				} catch (Exception ex) {
					logger.log(Level.WARNING, ex.getMessage());
				}
			}
		}
	}

	private void changed() {
		fireServoChangeListener();
	}

	public int getId() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		if (value == this.value) {
			return;
		}
		this.value = value;
		changed();
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		if (speed == this.speed) {
			return;
		}
		this.speed = speed;
		changed();
	}
	
	public void clear() {
		setValue(DEFAULT_VALUE);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id: " + id + ", speed: " + speed + ", value: " + value + "]";
	}
}
