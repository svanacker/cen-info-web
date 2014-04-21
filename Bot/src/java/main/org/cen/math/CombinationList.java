package org.cen.math;

import java.util.HashSet;
import java.util.Set;

public class CombinationList {
	private int count;

	private int dimensions;

	private int[] key;

	public CombinationList(int count, int dimensions) {
		super();
		this.count = count;
		this.dimensions = dimensions;
		key = new int[dimensions];
		for (int i = 0; i < dimensions; i++) {
			key[i] = 0;
		}
	}

	private boolean computeNextKey(int dimension) {
		boolean result = true;
		key[dimension]++;
		if (key[dimension] >= count) {
			if (dimension > 0) {
				result = computeNextKey(dimension - 1);
			} else {
				return false;
			}
			key[dimension] = 0;
		}
		return result;
	}

	public int[] getNextKey() {
		Set<Integer> set = new HashSet<Integer>();
		do {
			if (!computeNextKey(dimensions - 1)) {
				return null;
			}
		} while (!isValidKey(set));
		return key;
	}

	private boolean isValidKey(Set<Integer> set) {
		set.clear();
		for (int i = 0; i < dimensions; i++) {
			if (set.contains(key[i])) {
				return false;
			}
			set.add(key[i]);
		}
		return true;
	}

	public static void main(String[] args) {
		CombinationList l = new CombinationList(5, 5);
		for (;;) {
			int[] key = l.getNextKey();
			if (key == null) {
				break;
			}
			for (int i : key) {
				System.out.print(" " + i);
			}
			System.out.println("");
		}
	}
}
