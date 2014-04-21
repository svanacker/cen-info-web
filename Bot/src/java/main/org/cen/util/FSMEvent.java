package org.cen.util;

public class FSMEvent {
	private final String propertyName;

	private final Object oldValue;

	private final Object newValue;

	private final String fsmName;

	public String getPropertyName() {
		return propertyName;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public String getFsmName() {
		return fsmName;
	}

	public FSMEvent(String fsmName, String propertyName, Object oldValue,
			Object newValue) {
		super();
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.fsmName = fsmName;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[fsm=" + fsmName + ", property="
				+ propertyName + ", old=" + oldValue + ", new=" + newValue
				+ "]";
	}
}