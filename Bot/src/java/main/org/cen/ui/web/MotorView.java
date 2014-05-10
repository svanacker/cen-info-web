package org.cen.ui.web;

import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.logging.LoggingUtils;
import org.cen.robot.attributes.IRobotDimension;
import org.cen.robot.control.PIDInstructionType;
import org.cen.robot.control.RobotControlEngine;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.motor.com.MotorOutData;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.navigation.RotationRequest;
import org.cen.robot.device.navigation.analysis.MotionInstructionOutData;
import org.cen.robot.device.navigation.analysis.PIDMotionDataOutData;
import org.cen.robot.device.navigation.com.CalibrationOutData;
import org.cen.robot.device.navigation.com.MoveBackwardOutData;
import org.cen.robot.device.navigation.com.MoveForwardOutData;
import org.cen.robot.device.navigation.com.RotateLeftOneWheelOutData;
import org.cen.robot.device.navigation.com.RotateLeftOutData;
import org.cen.robot.device.navigation.com.RotateRightOneWheelOutData;
import org.cen.robot.device.navigation.com.RotateRightOutData;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.device.navigation.parameters.MotionParametersData;
import org.cen.robot.device.navigation.parameters.ReadMotionParametersInData;
import org.cen.robot.device.navigation.parameters.ReadMotionParametersOutData;
import org.cen.robot.device.navigation.position.com.PositionAskInData;
import org.cen.robot.device.navigation.position.com.PositionAskOutData;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseInData;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseOutData;
import org.cen.robot.device.navigation.position.com.SetInitialPositionOutData;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

/**
 * Presentation Objet for the view Motor.
 */
public class MotorView implements IRobotService, ActionListener, InDataListener {

	private final static Logger LOGGER = LoggingUtils.getClassLogger();

	protected RobotControlEngine engine;

	private double angle;

	private double distance;

	protected long left;

	protected int leftMotorSpeed;

	protected int move;

	protected long right;

	protected int rightMotorSpeed;

	protected int rotate;

	protected int rotateOneWheel;

	private IRobotServiceProvider servicesProvider;

	private int pulses;

	private int positionX;

	private int positionY;

	private int alpha;

	private boolean notified;

	private boolean debugMotion;

	public MotorView() {
		super();
	}

	public int getMotionParameterCount() {
		return RobotControlEngine.MOTION_PARAMETERS_COUNT;
	}

	public MotionParametersData[] getMotionParametersData() {
		return engine.getParameters();
	}

	public void calibrate() {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new CalibrationOutData());
	}

	public void doBackward() {
		sendMoveBackwardData(move);
	}

	public void doForward() {
		sendMoveForwardData(move);
	}

	public void doRotationLeft() {
		sendRotationLeftData(rotate);
	}

	public void doRotationRight() {
		sendRotationRightData(rotate);
	}

	public void doRotationOneWheelLeft() {
		sendRotationOneWheelLeftData(rotateOneWheel);
	}

	public void doRotationOneWheelRight() {
		sendRotationOneWheelRightData(rotateOneWheel);
	}

	public void doStop() {
		sendStopData();
	}

	public double getAngle() {
		return angle;
	}

	public double getDistance() {
		return distance;
	}

	public String getDistanceData() {
		IRobotDimension dimension = RobotUtils.getRobotAttribute(IRobotDimension.class, servicesProvider);
		return Double.toString(dimension.getLeftMotor().pulseToDistance(pulses));
	}

	public long getLeft() {
		return left;
	}

	public int getLeftMotorSpeed() {
		return leftMotorSpeed;
	}

	public int getMove() {
		return move;
	}

	public String getMoveData() {
		IRobotDevicesHandler devicesHandler = servicesProvider.getService(IRobotDevicesHandler.class);
		NavigationDevice navigation = (NavigationDevice) devicesHandler.getDevice(NavigationDevice.NAME);
		MoveRequest r = new MoveRequest(distance);
		OutData data = navigation.getOutData(r).get(0);
		return data.getMessage();
	}

	public String getPulses() {
		return "0x" + Integer.toHexString(pulses);
	}

	public long getRight() {
		return right;
	}

	public int getRightMotorSpeed() {
		return rightMotorSpeed;
	}

	public int getRotate() {
		return rotate;
	}

	public int getRotateOneWheel() {
		return rotateOneWheel;
	}

	public void setRotateOneWheel(int rotateOneWheel) {
		this.rotateOneWheel = rotateOneWheel;
	}

	public String getRotateData() {
		IRobotDevicesHandler devicesHandler = servicesProvider.getService(IRobotDevicesHandler.class);
		NavigationDevice navigation = (NavigationDevice) devicesHandler.getDevice(NavigationDevice.NAME);
		RotationRequest r = new RotationRequest(Math.toRadians(angle));
		OutData data = navigation.getOutData(r).get(0);
		return data.getMessage();
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof ReadPositionPulseInData) {
			ReadPositionPulseInData positionData = (ReadPositionPulseInData) data;
			left = positionData.getLeft();
			right = positionData.getRight();
		} else if (data instanceof PositionAskInData) {
			PositionAskInData positionInData = (PositionAskInData) data;
			positionX = (int) positionInData.getX();
			positionY = (int) positionInData.getY();
			alpha = (int) Math.toDegrees(positionInData.getAlpha());
		} else if (data instanceof ReadMotionParametersInData) {
			ReadMotionParametersInData inData = (ReadMotionParametersInData) data;
			MotionParametersData newMotionParametersData = inData.getData();
			int motionType = newMotionParametersData.getMotionType();
			MotionParametersData motionParametersData = getMotionParametersData()[motionType];
			motionParametersData.setAcceleration(newMotionParametersData.getAcceleration());
			motionParametersData.setSpeed(newMotionParametersData.getSpeed());
		}
		synchronized (this) {
			notified = true;
			// continue view rendering
			notify();
		}
	}

	@Override
	public void processAction(ActionEvent e) {
		UIComponent c = (UIComponent) e.getSource();
		UIComponent parent = c.getParent();
		String motorName = parent.getId();
		String actionName = c.getId();
		System.out.println(motorName);
		System.out.println(actionName);

		if (actionName.equals("resetMotors")) {
			setLeftMotorSpeed(0);
			setRightMotorSpeed(0);
			sendMotorData(0, 0);
		} else if (actionName.equals("goMotors")) {
			sendMotorData(leftMotorSpeed, rightMotorSpeed);
		}
	}

	private void readPositionData() {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new ReadPositionPulseOutData());
		synchronized (this) {
			notified = false;
			// wait for the response
			try {
				wait(1000);
			} catch (InterruptedException e) {
				LOGGER.warning(e.getMessage());
			}
			if (!notified) {
				// no notification = timeout
				LOGGER.fine("readPositionData timeout");
			}
		}
	}

	private void readAbsolutePositionData() {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new PositionAskOutData());
		synchronized (this) {
			notified = false;
			// wait for the response
			try {
				wait(1000);
			} catch (InterruptedException e) {
				LOGGER.warning(e.getMessage());
			}
			if (!notified) {
				// no notification = timeout
				LOGGER.fine("readAbsolutePositionData timeout");
			}
		}
	}

	private void readMotionParametersData() {
		IComService comService = servicesProvider.getService(IComService.class);
		for (int motionType = 0; motionType < getMotionParameterCount(); motionType++) {
			ReadMotionParametersOutData outData = new ReadMotionParametersOutData(motionType);
			comService.writeOutData(outData);
		}
		synchronized (this) {
			notified = false;
			// wait for the response
			try {
				wait(1000);
			} catch (InterruptedException e) {
				LOGGER.warning(e.getMessage());
			}
			if (!notified) {
				// no notification = timeout
				LOGGER.fine("readAbsolutePositionData timeout");
			}
		}
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			LOGGER.warning(e.getMessage());
		}
	}

	/**
	 * Ask very often the microcontroller about motion information.
	 */
	public void doDebugMotion() {
		IComService comService = servicesProvider.getService(IComService.class);

		// Wait
		// Global motion instruction
		MotionInstructionOutData alphaMotionInstructionOutData = new MotionInstructionOutData(PIDInstructionType.ALPHA);
		MotionInstructionOutData thetaMotionInstructionOutData = new MotionInstructionOutData(PIDInstructionType.THETA);
		comService.writeOutData(alphaMotionInstructionOutData);
		comService.writeOutData(thetaMotionInstructionOutData);

		int MAX = 100;
		for (int i = 0; i < MAX; i++) {
			PIDMotionDataOutData alphaOutData = new PIDMotionDataOutData(PIDInstructionType.ALPHA);
			PIDMotionDataOutData thetaOutData = new PIDMotionDataOutData(PIDInstructionType.THETA);
			comService.writeOutData(alphaOutData);
			comService.writeOutData(thetaOutData);
		}
	}

	public void refreshPosition() {
		readPositionData();
	}

	public void refreshMotionParameters() {
		readMotionParametersData();
	}

	public void refreshAbsolutePosition() {
		readAbsolutePositionData();
	}

	public void clearAbsolutePosition() {
		sendAbsolutePositionReset();
	}

	private void sendAbsolutePositionReset() {
		IComService comService = servicesProvider.getService(IComService.class);
		SetInitialPositionOutData outData = new SetInitialPositionOutData(0, 0, 0);
		comService.writeOutData(outData);
	}

	private void sendMotorData(int leftSpeed, int rightSpeed) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new MotorOutData(leftSpeed, rightSpeed));
	}

	// move

	private void sendMoveForwardData(int mm) {
		IComService comService = servicesProvider.getService(IComService.class);
		OutData outData = new MoveForwardOutData(mm);
		comService.writeOutData(outData);
		doDebugMotion();
	}

	private void sendMoveBackwardData(int mm) {
		IComService comService = servicesProvider.getService(IComService.class);
		OutData outData = new MoveBackwardOutData(mm);
		comService.writeOutData(outData);
		doDebugMotion();
	}

	// rotation

	private void sendRotationLeftData(int degree) {
		IComService comService = servicesProvider.getService(IComService.class);
		double angle = Math.toRadians(degree);
		OutData outData = new RotateLeftOutData(angle);
		comService.writeOutData(outData);
		doDebugMotion();
	}

	private void sendRotationRightData(int degree) {
		IComService comService = servicesProvider.getService(IComService.class);
		double mmRad = Math.toRadians(degree);
		OutData outData = new RotateRightOutData(mmRad);
		comService.writeOutData(outData);
		doDebugMotion();
	}

	private void sendRotationOneWheelLeftData(int degree) {
		IComService comService = servicesProvider.getService(IComService.class);
		double mmRad = Math.toRadians(degree);
		OutData outData = new RotateLeftOneWheelOutData(mmRad);
		comService.writeOutData(outData);
		doDebugMotion();
	}

	private void sendRotationOneWheelRightData(int degree) {
		IComService comService = servicesProvider.getService(IComService.class);
		double mmRad = Math.toRadians(degree);
		OutData outData = new RotateRightOneWheelOutData(mmRad);
		comService.writeOutData(outData);
		doDebugMotion();
	}

	private void sendStopData() {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new StopOutData());
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setLeft(long left) {
		this.left = left;
	}

	public void setLeftMotorSpeed(int leftMotorSpeed) {
		this.leftMotorSpeed = leftMotorSpeed;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public void setPulses(String pulses) {
		if (pulses.startsWith("0x")) {
			this.pulses = Integer.parseInt(pulses.substring(2), 16);
			if (this.pulses > 0x7FFF) {
				this.pulses = -(0x10000 - this.pulses);
			}
		} else {
			this.pulses = Integer.parseInt(pulses);
		}
	}

	public void setRight(long right) {
		this.right = right;
	}

	public void setRightMotorSpeed(int rightMotorSpeed) {
		this.rightMotorSpeed = rightMotorSpeed;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		engine = RobotUtils.getRobot(provider).getAttribute(RobotControlEngine.class);
		IComService comService = servicesProvider.getService(IComService.class);
		comService.addInDataListener(this);
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public boolean isDebugMotion() {
		return debugMotion;
	}

	public void setDebugMotion(boolean debugMotion) {
		this.debugMotion = debugMotion;
	}
}
