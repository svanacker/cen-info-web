package org.cen.ui.web;

public class FormatItem {
	private int id;

	private String label;

	public FormatItem(String label, int id) {
		super();
		this.label = label;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
}
