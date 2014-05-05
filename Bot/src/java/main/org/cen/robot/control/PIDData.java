package org.cen.robot.control;

import java.util.Properties;

import org.cen.math.PropertiesMathUtils;

/**
 * Describe a PID Control for a part of the robot (alpha / theta, or an other
 * engine)
 */
public class PIDData {

	private static final String PROPERTY_D = "d";

	private static final String PROPERTY_I = "i";

	private static final String PROPERTY_MAX_I = "maxI";

	private static final String PROPERTY_P = "p";

	/** The index of the PID. */
	protected int index;

	/** InstructionType. */
	protected PIDInstructionType instructionType;

	/** Type. */
	protected PIDType pidType;

	/** The correction for derived Term */
	protected int d;

	/** The correction for integral Term */
	protected int i;

	/** The maximal integral Term */
	protected int maxI;

	/** The correction for proportionnal term */
	protected int p;

	/**
	 * Return the name of a PID.
	 */
	public String getName() {
		String typeName = pidType.getName();
		String instructionName = instructionType.getName();

		String result = typeName + "/" + instructionName;

		return result;
	}

	// private final EventListenerList listeners = new EventListenerList();

	/**
	 * Default Constructor
	 */
	public PIDData() {
		super();
	}

	public PIDData(Properties properties, String prefix) {
		super();
		setFromProperties(properties, prefix);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PIDData) {
			PIDData data = (PIDData) obj;
			return (data.d == d) && (data.i == i) && (data.maxI == maxI) && (data.p == p);
		} else {
			return super.equals(obj);
		}
	}

	/**
	 * Notify all observers that requires to be notify when pid data changes
	 */
	private void firePIDChangeListener() {
		// Object[] l = listeners.getListenerList();
		// for (int j = l.length - 2; j >= 0; j -= 2) {
		// if (l[j] == PIDChangeListener.class) {
		// try {
		// ((PIDChangeListener) l[j + 1]).onPIDChange(this);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// }
		// }
	}

	public int getIndex() {
		return index;
	}

	public int getD() {
		return d;
	}

	public int getI() {
		return i;
	}

	public int getMaxI() {
		return maxI;
	}

	public int getP() {
		return p;
	}

	public void setIndex(int index) {
		if (this.index != index) {
			this.index = index;
			firePIDChangeListener();
		}
	}

	public void set(PIDData data) {
		index = data.index;
		d = data.d;
		i = data.i;
		maxI = data.maxI;
		p = data.p;
	}

	public void setD(int d) {
		if (this.d != d) {
			this.d = d;
			firePIDChangeListener();
		}
	}

	public void setFromProperties(Properties properties, String prefix) {
		p = (int) PropertiesMathUtils.getDouble(properties, prefix + PROPERTY_P);
		i = (int) PropertiesMathUtils.getDouble(properties, prefix + PROPERTY_I);
		d = (int) PropertiesMathUtils.getDouble(properties, prefix + PROPERTY_D);
		maxI = (int) PropertiesMathUtils.getDouble(properties, prefix + PROPERTY_MAX_I);
	}

	public void setI(int i) {
		if (this.i != i) {
			this.i = i;
			firePIDChangeListener();
		}
	}

	// public void addPIDChangeListener(PIDChangeListener pidChangeListener) {
	// listeners.add(PIDChangeListener.class, pidChangeListener);
	// }

	public void setMaxI(int maxI) {
		if (this.maxI != maxI) {
			this.maxI = maxI;
			firePIDChangeListener();
		}

	}

	public void setP(int p) {
		if (this.p != p) {
			this.p = p;
			firePIDChangeListener();
		}
	}

	public PIDInstructionType getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(PIDInstructionType instructionType) {
		this.instructionType = instructionType;
	}

	public PIDType getPidType() {
		return pidType;
	}

	public void setPidType(PIDType pidType) {
		this.pidType = pidType;
	}

	/**
	 * Change in one method all parameters of PIDEngine
	 * 
	 * @param p
	 *            new proportionnal term
	 * @param i
	 *            new integral term
	 * @param d
	 *            new derivative term
	 */
	public void setValues(int index, int p, int i, int d, int maxI) {
		setIndex(index);
		setP(p);
		setI(i);
		setD(d);
		setMaxI(maxI);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[name=" + getName() + ", index=" + index + ", p=" + p + ", i=" + i + ", d=" + d
				+ ", maxI=" + maxI + "]";
	}
}
