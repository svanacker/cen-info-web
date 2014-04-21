package org.cen.robot.control.graph;

import java.util.ArrayList;
import java.util.List;

import org.cen.robot.control.ComputedPIDDataHistoryItem;
import org.cen.robot.control.MotionData;
import org.cen.robot.control.MotionInstructionData;
import org.cen.robot.control.PIDDataHistory;
import org.cen.robot.control.PIDInstructionType;
import org.cen.robot.control.PIDMotionData;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Build a dataset for JFreeChart to analyse EngineData.
 */
public class PIDMotionDataXYDatasetFactory {

	public static XYDataset createSpeedDataset(PIDMotionData pidMotionData) {
		DefaultXYDataset result = new DefaultXYDataset();

		if (pidMotionData == null) {
			return result;
		}

		PIDDataHistory[] dataHistory = pidMotionData.getDataHistory();

		for (PIDInstructionType instructionType : PIDInstructionType.values()) {
			PIDDataHistory pidDataHistory = dataHistory[instructionType.getIndex()];

			// computed trajectory
			List<XYData> computedPositionDataList = new ArrayList<XYData>();
			MotionInstructionData instruction = pidDataHistory.getInstruction();
			computedPositionDataList.add(new XYData(0.0f, 0.0f));
			computedPositionDataList.add(new XYData(instruction.getT1(), instruction.getP1() / instruction.getT1()));
			computedPositionDataList.add(new XYData(instruction.getT2(), (instruction.getP2() - instruction.getP1())
					/ (instruction.getT2() - instruction.getT1())));
			computedPositionDataList.add(new XYData(instruction.getT3(), (instruction.getNextPosition() - instruction
					.getP2()) / (instruction.getT3() - instruction.getT2())));

			addSeries(result, "computed speed :" + instructionType.getName(), computedPositionDataList);

			// real trajectory
			List<XYData> realSpeedDataList = new ArrayList<XYData>();

			List<ComputedPIDDataHistoryItem> history = pidDataHistory.getHistory();

			float previousPidTime = 0.0f;
			float previousPosition = 0.0f;
			for (int i = 0; i < history.size(); i++) {
				ComputedPIDDataHistoryItem item = history.get(i);

				MotionData motionData = item.getMotion();

				float position = motionData.getPosition();

				float pidTime = item.getPidTime();
				XYData data = null;
				float diffTime = pidTime - previousPidTime;

				if (diffTime != 0.0f) {
					data = new XYData(pidTime, (position - previousPosition) / diffTime);
				} else {
					data = new XYData(pidTime, 0.0f);
				}
				previousPidTime = pidTime;
				previousPosition = position;
				realSpeedDataList.add(data);
			}
			addSeries(result, "real speed : " + instructionType.getName(), realSpeedDataList);
		}
		return result;
	}

	public static XYDataset createPositionDataset(PIDMotionData pidMotionData) {

		DefaultXYDataset result = new DefaultXYDataset();

		if (pidMotionData == null) {
			return result;
		}

		PIDDataHistory[] dataHistory = pidMotionData.getDataHistory();

		for (PIDInstructionType instructionType : PIDInstructionType.values()) {
			PIDDataHistory pidDataHistory = dataHistory[instructionType.getIndex()];

			// computed trajectory
			List<XYData> computedPositionDataList = new ArrayList<XYData>();
			MotionInstructionData instruction = pidDataHistory.getInstruction();
			computedPositionDataList.add(new XYData(0.0f, 0.0f));
			computedPositionDataList.add(new XYData(instruction.getT1(), instruction.getP1()));
			computedPositionDataList.add(new XYData(instruction.getT2(), instruction.getP2()));
			computedPositionDataList.add(new XYData(instruction.getT3(), instruction.getNextPosition()));

			addSeries(result, "computed position :" + instructionType.getName(), computedPositionDataList);

			// real trajectory
			List<XYData> realPositionDataList = new ArrayList<XYData>();

			List<ComputedPIDDataHistoryItem> history = pidDataHistory.getHistory();

			for (int i = 0; i < history.size(); i++) {
				ComputedPIDDataHistoryItem item = history.get(i);

				MotionData motionData = item.getMotion();

				float position = motionData.getPosition();

				XYData data = new XYData(item.getPidTime(), position);
				realPositionDataList.add(data);
			}
			addSeries(result, "real position : " + instructionType.getName(), realPositionDataList);
		}
		return result;
	}

	private static void addSeries(DefaultXYDataset dataset, String name, List<XYData> xyDatas) {
		int size = xyDatas.size();
		double[][] values = new double[2][size];

		for (int i = 0; i < size; i++) {
			XYData xyData = xyDatas.get(i);
			values[0][i] = xyData.getX();
			values[1][i] = xyData.getY();
		}
		dataset.addSeries(name, values);
	}

}
