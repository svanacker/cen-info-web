package org.cen.ui.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.robot.control.ComputedPIDDataHistoryItem;
import org.cen.robot.control.MotionData;
import org.cen.robot.control.MotionEndInfoData;
import org.cen.robot.control.MotionErrorData;
import org.cen.robot.control.MotionInstructionData;
import org.cen.robot.control.PIDDataHistory;
import org.cen.robot.control.PIDMotionData;
import org.cen.robot.control.PIDMotionDataList;
import org.cen.robot.control.RobotControlEngine;
import org.cen.robot.control.graph.PIDMotionDataXYDatasetFactory;
import org.cen.robot.control.graph.sample.PIDMotionDataSample;
import org.cen.robot.device.navigation.analysis.MotionInstructionInData;
import org.cen.robot.device.navigation.analysis.PIDMotionDataInData;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Get graphical statistics about PID correction.
 */
public class ChartView implements IRobotService, ActionListener, InDataListener {

	protected RobotControlEngine engine;

	protected int index = 0;

	public void setSelectedIndex(int index) {
		this.index = index;
	}

	private IRobotServiceProvider servicesProvider;

	public PIDMotionData getSelectedPidMotionData() {
		PIDMotionDataList pidMotionDataList = getMotionDataList();
		if (pidMotionDataList == null) {
			return null;
		}
		if (index == -1 || index >= pidMotionDataList.size()) {
			return null;
		}
		PIDMotionData result = pidMotionDataList.getMotionData(index);
		if (result == null) {
			result = pidMotionDataList.last();
		}
		return result;
	}

	public List<SelectItem> getMotionDataItems() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		PIDMotionDataList motionDataList = getMotionDataList();
		int count = 0;
		for (PIDMotionData motionData : motionDataList) {
			SelectItem item = new SelectItem(count, motionData.getLabel());
			result.add(item);
			count++;
		}

		return result;
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof MotionInstructionInData) {
			MotionInstructionInData instructionInData = (MotionInstructionInData) data;
			MotionInstructionData motionInstructionInData = instructionInData.getMotionInstructionInData();

			PIDMotionDataList pidMotionDataList = getMotionDataList();
			PIDMotionData motionData = pidMotionDataList.last();
			int index = motionInstructionInData.getIndex();
			PIDDataHistory pidDataHistory = motionData.getDataHistory()[index];
			MotionInstructionData instruction = pidDataHistory.getInstruction();
			instruction.load(motionInstructionInData);
		} else if (data instanceof PIDMotionDataInData) {
			PIDMotionDataInData pidMotionData = (PIDMotionDataInData) data;

			PIDMotionDataList pidMotionDataList = getMotionDataList();
			PIDMotionData motionData = pidMotionDataList.last();

			int index = pidMotionData.getIndex();
			PIDDataHistory pidDataHistory = motionData.getDataHistory()[index];
			ComputedPIDDataHistoryItem item = new ComputedPIDDataHistoryItem();

			MotionErrorData err = item.getErr();
			// TODO
			// err.setDerivativeError(pidMotionData.getErrorDataDerivativeError());
			// TODO
			// err.setIntegralError(pidMotionData.getErrorDataIntegralError());
			err.setError(pidMotionData.getErrorDataError());
			// TODO
			// err.setPreviousError(pidMotionData.getErrorDataPreviousError());

			MotionData motion = item.getMotion();
			motion.setPosition(pidMotionData.getPosition());
			// TODO motion.setOldPosition(pidMotionData.getOldPosition());
			motion.setU(pidMotionData.getU());

			MotionEndInfoData motionEnd = item.getMotionEnd();
			motionEnd.setAbsDeltaPositionIntegral(pidMotionData.getEndInfoAbsDeltaPositionIntegral());
			// TODO motionEnd.setMaxTime(pidMotionData.getEndInfoMaxTime());
			motionEnd.setTime(pidMotionData.getEndInfoTime());
			motionEnd.setuIntegral(pidMotionData.getEndInfoUIntegral());

			pidDataHistory.addHistory(item);
		}
	}

	public void onChange(ValueChangeEvent event) {
		Object newValue = event.getNewValue();
		if (newValue != null) {
			int index = Integer.valueOf(newValue.toString());
			setSelectedIndex(index);
		}
	}

	public int getCount() {
		PIDMotionDataList motionDataList = getMotionDataList();
		if (motionDataList == null) {
			return 0;
		}
		return motionDataList.size();
	}

	public ChartView() {
		clear();

	}

	@Override
	public void setServicesProvider(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
		getRobotControlEngine();

		addSampleMotionData();

		IComService comService = servicesProvider.getService(IComService.class);
		comService.addInDataListener(this);
	}

	private void addSampleMotionData() {
		PIDMotionData pidMotionData = PIDMotionDataSample.create();
		getMotionDataList().addPIDMotionData(pidMotionData);
	}

	private void getRobotControlEngine() {
		engine = RobotUtils.getRobot(servicesProvider).getAttribute(RobotControlEngine.class);
	}

	public IRobotServiceProvider getServicesProvider() {
		return servicesProvider;
	}

	public PieDataset getPieDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("A", 52);
		dataset.setValue("B", 18);
		dataset.setValue("C", 30);
		return dataset;
	}

	public XYDataset getXYDataset() {
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("A", new double[][] { { 2004, 2005, 2006, 2007, 2008, 2009, 2011 }, { 90, 56, 62, 41, 27, 8, 8 } });
		return dataset;
	}

	public XYDataset getPositionDataset() {
		PIDMotionData pidMotionData = getSelectedPidMotionData();
		XYDataset dataset = PIDMotionDataXYDatasetFactory.createPositionDataset(pidMotionData);

		return dataset;
	}

	public XYDataset getSpeedDataset() {
		PIDMotionData pidMotionData = getSelectedPidMotionData();
		XYDataset dataset = PIDMotionDataXYDatasetFactory.createSpeedDataset(pidMotionData);

		return dataset;
	}

	public void clear() {
		index = -1;
		PIDMotionDataList motionDataList = getMotionDataList();
		if (motionDataList != null) {
			motionDataList.clear();
		}
	}

	public void addMotionData() {
		PIDMotionData data = new PIDMotionData();
		getMotionDataList().addPIDMotionData(data);
	}

	private PIDMotionDataList getMotionDataList() {
		if (engine != null) {
			return engine.getPidMotionDataList();
		}
		return null;
	}

	@Override
	public void processAction(ActionEvent event) throws AbortProcessingException {
		UIComponent component = (UIComponent) event.getSource();
		String actionName = component.getId();
		if (actionName.equals("addMotionData")) {
			addMotionData();
		} else if (actionName.equals("addSampleMotionData")) {
			addSampleMotionData();
		} else if (actionName.equals("clear")) {
			clear();
		}
	}

}
