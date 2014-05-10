package org.cen.ui.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.lang3.StringUtils;
import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.logging.LoggingUtils;
import org.cen.robot.control.PIDData;
import org.cen.robot.control.PIDInstructionType;
import org.cen.robot.control.PIDType;
import org.cen.robot.control.RobotControlEngine;
import org.cen.robot.device.pid.com.MotionEndDetectionParameter;
import org.cen.robot.device.pid.com.ReadMotionEndDetectionParameterInData;
import org.cen.robot.device.pid.com.ReadMotionEndDetectionParametersOutData;
import org.cen.robot.device.pid.com.ReadPIDInData;
import org.cen.robot.device.pid.com.ReadPIDOutData;
import org.cen.robot.device.pid.com.WritePIDOutData;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

/**
 * Encapsulate information about PID.
 */
public class PIDView implements IRobotService, InDataListener, ActionListener {

	private final static Logger LOGGER = LoggingUtils.getClassLogger();

	protected RobotControlEngine engine;

	private IRobotServiceProvider servicesProvider;

	private final List<PIDType> types;

	private final List<PIDInstructionType> instructionTypes;

	/**
	 * Constructor.
	 */
	public PIDView() {
		super();
		instructionTypes = new ArrayList<PIDInstructionType>();
		for (PIDInstructionType instructionType : PIDInstructionType.values()) {
			instructionTypes.add(instructionType);
		}
		types = new ArrayList<PIDType>();
		for (PIDType type : PIDType.values()) {
			types.add(type);
		}
	}

	public MotionEndDetectionParameter getEndDetectionParameter() {
		MotionEndDetectionParameter result = engine.getEndDetectionParameter();

		return result;
	}

	public RobotControlEngine getEngine() {
		return engine;
	}

	public int getCount() {
		return RobotControlEngine.PID_COUNT;
	}

	public int getInstructionCount() {
		return PIDInstructionType.COUNT;
	}

	public String getInstructionLabel(int instructionTypeIndex) {
		PIDInstructionType instructionType = RobotControlEngine.getInstructionType(instructionTypeIndex);
		String result = instructionType.getName();

		return result;
	}

	public String getTypeLabel(int typeIndex) {
		PIDType type = RobotControlEngine.getType(typeIndex);
		String result = type.getName();

		return result;
	}

	public Collection<PIDType> getTypes() {
		return types;
	}

	public Collection<PIDInstructionType> getInstructionTypes() {
		return instructionTypes;
	}

	public int getTypeCount() {
		return PIDType.COUNT;
	}

	public PIDData getPIDData(int index) {
		PIDData result = engine.getPIDData(index);

		return result;
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof ReadPIDInData) {
			ReadPIDInData pidInData = (ReadPIDInData) data;
			PIDData pidData = pidInData.getData();

			PIDInstructionType instructionType = pidData.getInstructionType();
			PIDType pidType = pidData.getPidType();
			engine.setPIDData(pidData, instructionType, pidType);
		}
		if (data instanceof ReadMotionEndDetectionParameterInData) {
			ReadMotionEndDetectionParameterInData endDetectionParameterInData = (ReadMotionEndDetectionParameterInData) data;
			MotionEndDetectionParameter endDetectionParameter = endDetectionParameterInData.getData();
			engine.setEndDetectionParameter(endDetectionParameter);
		}
		synchronized (this) {
			// continue view rendering:
			// TODO : wait all ReadPIDInData
			notify();
		}
	}

	private void readAllPIDData() {

		for (PIDType pidType : PIDType.values()) {
			for (PIDInstructionType pidInstructionType : PIDInstructionType.values()) {
				refreshPIDData(pidType, pidInstructionType);
			}
		}
	}

	private void refreshPIDData(PIDType pidType, PIDInstructionType pidInstructionType) {
		IComService comService = servicesProvider.getService(IComService.class);
		int indexOfPid = RobotControlEngine.getIndexOfPid(pidInstructionType, pidType);
		ReadPIDOutData outData = new ReadPIDOutData(indexOfPid);
		comService.writeOutData(outData);
		synchronized (this) {
			// wait for the response
			try {
				wait();
			} catch (InterruptedException e) {
				LOGGER.warning(e.getMessage());
			}
		}
	}

	@Override
	public void processAction(ActionEvent e) {
		UIComponent c = (UIComponent) e.getSource();
		String actionName = c.getId();
		System.out.println(actionName);
		if (actionName.startsWith("refresh_")) {
			actionName = actionName.substring("refresh_".length());
			String[] values = StringUtils.split(actionName, "_");
			int pidTypeIndex = Integer.valueOf(values[0]);
			int pidInstructionTypeIndex = Integer.valueOf(values[1]);
			PIDType pidType = PIDType.values()[pidTypeIndex];
			PIDInstructionType pidInstructionType = PIDInstructionType.values()[pidInstructionTypeIndex];
			refreshPIDData(pidType, pidInstructionType);
		}
	}

	public void refreshAllPids() {
		readAllPIDData();
	}

	public void refreshEndMotionParameters() {
		IComService comService = servicesProvider.getService(IComService.class);
		ReadMotionEndDetectionParametersOutData outData = new ReadMotionEndDetectionParametersOutData();
		comService.writeOutData(outData);
	}

	private void sendData(int index, PIDData pidData) {
		IComService comService = servicesProvider.getService(IComService.class);
		WritePIDOutData pidOutData = new WritePIDOutData(index, pidData);
		comService.writeOutData(pidOutData);
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		engine = RobotUtils.getRobot(provider).getAttribute(RobotControlEngine.class);

		IComService comService = servicesProvider.getService(IComService.class);
		comService.addInDataListener(this);
	}
}
