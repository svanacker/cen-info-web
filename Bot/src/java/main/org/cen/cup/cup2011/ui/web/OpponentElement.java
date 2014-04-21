package org.cen.cup.cup2011.ui.web;

public class OpponentElement extends Element {
	public OpponentElement(String name, ElementListener listener) {
		super(name, listener);
	}

	@Override
	public void setValue(boolean value) {
		this.value ^= value;
		if (this.value) {
			notifyChanged();
		}
	}
}
