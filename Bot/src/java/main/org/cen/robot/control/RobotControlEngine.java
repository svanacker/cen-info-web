package org.cen.robot.control;

import java.util.Properties;

import org.cen.robot.IRobotAttribute;
import org.cen.robot.device.navigation.parameters.MotionParametersData;
import org.cen.robot.device.pid.com.MotionEndDetectionParameter;

/**
 * Describe the system which control the robot. The control is based on a double
 * control with alpha and theta parameter which controls the direction (alpha)
 * and the distance (theta).
 * 
 * @author svanacker
 * @version 10/03/2007
 */
public class RobotControlEngine implements IRobotAttribute {

	/** How much there is PID value. */
	public static final int PID_COUNT = PIDType.values().length * PIDInstructionType.values().length;

	/** PID Datas. */
	private final PIDData[][] pidParametersDatas = new PIDData[PIDInstructionType.COUNT][PIDType.COUNT];

	/** End Trajectory detection data. */
	private MotionEndDetectionParameter endDetectionParameter = new MotionEndDetectionParameter();

	public static final int MOTION_PARAMETERS_COUNT = 4;

	/** Default Motion Parameter. */
	private final MotionParametersData[] parameters = new MotionParametersData[MOTION_PARAMETERS_COUNT];

	public MotionParametersData[] getParameters() {
		return parameters;
	}

	public PIDData[][] getPidParametersData() {
		return pidParametersDatas;
	}

	public PIDData[] getPidParametersDatas() {
		PIDData[] result = new PIDData[PID_COUNT];

		for (PIDInstructionType instructionType : PIDInstructionType.values()) {
			for (PIDType pidType : PIDType.values()) {
				int indexOfPid = getIndexOfPid(instructionType, pidType);
				result[indexOfPid] = pidParametersDatas[instructionType.getIndex()][pidType.getIndex()];
			}
		}

		return result;
	}

	public void setEndDetectionParameter(MotionEndDetectionParameter endDetectionParameter) {
		this.endDetectionParameter = endDetectionParameter;
	}

	public MotionEndDetectionParameter getEndDetectionParameter() {
		return endDetectionParameter;
	}

	/** History of all Motions. */
	private final PIDMotionDataList pidMotionDataList = new PIDMotionDataList();

	public PIDMotionDataList getPidMotionDataList() {
		return pidMotionDataList;
	}

	/**
	 * Constructor. Builds some parameters
	 */
	public RobotControlEngine() {
		super();
		init();
	}

	public RobotControlEngine(Properties properties, String prefix) {
		super();
		setFromProperties(properties, prefix);
		init();
	}

	private void init() {
		initPIDDatas();
		initMotionParameters();
	}

	private void initPIDDatas() {
		for (PIDInstructionType instructionType : PIDInstructionType.values()) {
			for (PIDType pidType : PIDType.values()) {
				PIDData pidData = new PIDData();
				pidData.setPidType(pidType);
				pidData.setInstructionType(instructionType);
				int indexOfPid = getIndexOfPid(instructionType, pidType);
				pidData.setIndex(indexOfPid);
				setPIDData(pidData, instructionType, pidType);
			}
		}
	}

	private void initMotionParameters() {
		for (int i = 0; i < MOTION_PARAMETERS_COUNT; i++) {
			parameters[i] = new MotionParametersData();
		}
		parameters[0].setName("forward");
		parameters[1].setName("rotation");
		parameters[2].setName("oneWheel");
		parameters[3].setName("maintainPosition");
	}

	public void setPIDData(PIDData pidData, PIDInstructionType instructionType, PIDType pidType) {
		int instructionTypeIndex = instructionType.getIndex();
		int pidTypeIndex = pidType.getIndex();
		if (pidParametersDatas[instructionTypeIndex][pidTypeIndex] != null) {
			pidParametersDatas[instructionTypeIndex][pidTypeIndex].set(pidData);
		} else {
			pidParametersDatas[instructionTypeIndex][pidTypeIndex] = pidData;
		}
	}

	public PIDData getPIDData(PIDInstructionType instructionType, PIDType pidType) {
		PIDData result = pidParametersDatas[instructionType.getIndex()][pidType.getIndex()];
		return result;
	}

	public PIDData getPIDData(int index) {
		PIDInstructionType pidInstructionType = getInstructionType(index);
		PIDType pidType = getType(index);
		PIDData result = getPIDData(pidInstructionType, pidType);
		return result;
	}

	public static int getIndexOfPid(PIDInstructionType instructionType, PIDType pidType) {
		return pidType.getIndex() * PIDInstructionType.COUNT + instructionType.getIndex();
	}

	public static PIDInstructionType getInstructionType(int index) {
		return PIDInstructionType.values()[index % PIDInstructionType.COUNT];
	}

	public static PIDType getType(int index) {
		return PIDType.values()[index / PIDInstructionType.COUNT];
	}

	public void setFromProperties(Properties properties, String prefix) {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[]";
	}
}
