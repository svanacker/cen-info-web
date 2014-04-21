package org.cen.cup.cup2011.ui.web;

public class Element {
	public boolean value;

	private ElementListener listener;

	private String name;

	public Element(String name, ElementListener listener) {
		super();
		this.name = name;
		this.listener = listener;
	}

	public String getName() {
		return name;
	}

	public boolean getValue() {
		return value;
	}

	protected void notifyChanged() {
		if (listener != null) {
			listener.changed(this);
		}
	}

	public void setValue(boolean value) {
		this.value = value;
		notifyChanged();
	}
}