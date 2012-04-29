package org.cen.ui.web;

import java.io.Serializable;

public class OutDataDescriptor implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cup;
	private String header;
	private String name;

	public OutDataDescriptor(String cup, String name, String header) {
		super();
		this.cup = cup;
		this.name = name;
		this.header = header;
	}

	public String getCup() {
		return cup;
	}

	public String getHeader() {
		return header;
	}

	public String getName() {
		return name;
	}
}
