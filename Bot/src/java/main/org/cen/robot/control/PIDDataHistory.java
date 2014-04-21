package org.cen.robot.control;

import java.util.ArrayList;
import java.util.List;

/**
 * Encasulates the historic of a PID correction during motion.
 */
public class PIDDataHistory {

	/** Computes the parameters used to control motion. */
	protected MotionInstructionData instruction;

	/** An history of all PID computation. */
	public List<ComputedPIDDataHistoryItem> history;

	public PIDDataHistory() {
		instruction = new MotionInstructionData();
		history = new ArrayList<ComputedPIDDataHistoryItem>();
	}

	public void addHistory(ComputedPIDDataHistoryItem item) {
		history.add(item);
	}

	public List<ComputedPIDDataHistoryItem> getHistory() {
		return history;
	}

	public MotionInstructionData getInstruction() {
		return instruction;
	}

	public void setInstruction(MotionInstructionData instruction) {
		this.instruction = instruction;
	}
}
