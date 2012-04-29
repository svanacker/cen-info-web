package org.cen.robot.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsulates a list of PIDMotionData (useful to compare differents graphs).
 */
public class PIDMotionDataList implements Iterable<PIDMotionData> {

	protected List<PIDMotionData> list;

	public PIDMotionDataList() {
		list = new ArrayList<PIDMotionData>();
	}

	@Override
	public Iterator<PIDMotionData> iterator() {
		return list.iterator();
	}

	public PIDMotionData addPIDMotionData(PIDMotionData data) {
		list.add(data);
		return data;
	}

	public void clear() {
		list.clear();
	}

	public PIDMotionData last() {
		int size = list.size();
		if (size > 0) {
			return list.get(size - 1);
		}
		return null;
	}

	public int size() {
		return list.size();
	}

	public PIDMotionData getMotionData(int index) {
		return list.get(index);
	}
}
