package org.cen.com;

import org.cen.com.out.OutData;

public class RobotInitializedSimulOutData extends OutData {
	String text;

	public RobotInitializedSimulOutData(String atexte){
		this.text = atexte;
	}

	@Override
	public String getArguments() {

		return text;
	}

	@Override
	public String getHeader() {
		return "x";
	}
}
