package org.cen.cup.cup2010.navigation;

public class Corn2010 {
	private boolean available = true;

	private String name;

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[name=" + name + ", available=" + available + "]";
	}
}
