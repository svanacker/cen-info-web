package org.cen.robot.control.graph.sample;

import java.util.List;

import org.cen.robot.control.MotionInstructionData;
import org.cen.robot.control.PIDDataHistory;
import org.cen.robot.control.ComputedPIDDataHistoryItem;
import org.cen.robot.control.PIDInstructionType;
import org.cen.robot.control.PIDMotionData;

public class PIDMotionDataSample {

	public static PIDMotionData create() {
		PIDMotionData result = new PIDMotionData();

		PIDDataHistory pidDataThetaHistory = result.getDataHistory()[PIDInstructionType.THETA.getIndex()];
		List<ComputedPIDDataHistoryItem> thetaHistory = pidDataThetaHistory.getHistory();
		PIDDataHistory pidDataAlphaHistory = result.getDataHistory()[PIDInstructionType.ALPHA.getIndex()];
		List<ComputedPIDDataHistoryItem> alphaHistory = pidDataAlphaHistory.getHistory();

		// Computed Position
		MotionInstructionData thetaInstruction = MotionInstructionDataSample.createGoThetaSample();
		pidDataThetaHistory.setInstruction(thetaInstruction);
		MotionInstructionData alphaInstruction = MotionInstructionDataSample.createGoAlphaSample();
		pidDataAlphaHistory.setInstruction(alphaInstruction);

		// real Position

		addHistoryItem(thetaHistory, 0.0f, 0.0f);
		addHistoryItem(thetaHistory, 10.0f, 40.0f);
		addHistoryItem(thetaHistory, 20.0f, 100.0f);
		addHistoryItem(thetaHistory, 30.0f, 200.0f);
		addHistoryItem(thetaHistory, 40.0f, 400.0f);
		addHistoryItem(thetaHistory, 50.0f, 600.0f);
		addHistoryItem(thetaHistory, 60.0f, 800.0f);
		addHistoryItem(thetaHistory, 70.0f, 1000.0f);
		addHistoryItem(thetaHistory, 80.0f, 1100.0f);
		addHistoryItem(thetaHistory, 90.0f, 1160.0f);
		addHistoryItem(thetaHistory, 100.0f, 1200.0f);

		for (int i = 0; i < 10; i++) {
			addHistoryItem(alphaHistory, i * 10.0f, 0.0f);
		}

		return result;
	}

	protected static void addHistoryItem(List<ComputedPIDDataHistoryItem> history, float pidTime, float position) {
		ComputedPIDDataHistoryItem item = new ComputedPIDDataHistoryItem();
		item.setPidTime(pidTime);
		item.getMotion().setPosition(position);
		history.add(item);
	}
}
