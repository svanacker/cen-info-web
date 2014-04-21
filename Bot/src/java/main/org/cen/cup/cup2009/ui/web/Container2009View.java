package org.cen.cup.cup2009.ui.web;

import org.cen.cup.cup2009.device.Sequence2009Request;
import org.cen.cup.cup2009.device.lift.Lift2009Request;
import org.cen.cup.cup2009.device.lift.Lift2009Request.Action;
import org.cen.cup.cup2009.device.lintel.Lintel2009Request;
import org.cen.cup.cup2009.device.plier.Plier2009Request;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.navigation.RotationRequest;
import org.cen.robot.device.navigation.StopRequest;

public class Container2009View implements IRobotService {
	private IRobotServiceProvider servicesProvider;

	// Up / Down of Lift

	public void miniDown() {
		sendRequest(new Lift2009Request(Action.SMALL_DOWN, 7));
	}

	public void miniUp() {
		sendRequest(new Lift2009Request(Action.SMALL_UP, -7));
	}

	public void down() {
		sendRequest(new Lift2009Request(Action.DOWN, 40));
	}

	public void up() {
		sendRequest(new Lift2009Request(Action.DOWN, -40));
	}

	public void gotoBottom() {
		sendRequest(new Lift2009Request(Action.BOTTOM));
	}

	private DeviceRequestDispatcher getDispatcher() {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		return handler.getRequestDispatcher();
	}

	// ---- PLIER -----

	// Lateral Movement of Plier

	public void openPlier() {
		sendRequest(new Plier2009Request(org.cen.cup.cup2009.device.plier.Plier2009Request.Action.OPEN));
	}

	public void closePlier() {
		sendRequest(new Plier2009Request(org.cen.cup.cup2009.device.plier.Plier2009Request.Action.CLOSE));
	}

	public void left() {
		sendRequest(new Plier2009Request(org.cen.cup.cup2009.device.plier.Plier2009Request.Action.LEFT));
	}

	public void middle() {
		sendRequest(new Plier2009Request(org.cen.cup.cup2009.device.plier.Plier2009Request.Action.MIDDLE));
	}

	public void right() {
		sendRequest(new Plier2009Request(org.cen.cup.cup2009.device.plier.Plier2009Request.Action.RIGHT));
	}

	// ---- LINTEL -----

	// Open / Close of Lintel Plier

	public void openLintel() {
		sendRequest(new Lintel2009Request(org.cen.cup.cup2009.device.lintel.Lintel2009Request.Action.OPEN));
	}

	public void closeLintel() {
		sendRequest(new Lintel2009Request(org.cen.cup.cup2009.device.lintel.Lintel2009Request.Action.CLOSE));
	}

	// Deploy / Undeploy of Lintel

	public void deployLintel() {
		sendRequest(new Lintel2009Request(org.cen.cup.cup2009.device.lintel.Lintel2009Request.Action.DEPLOY));
	}

	public void undeployLintel() {
		sendRequest(new Lintel2009Request(org.cen.cup.cup2009.device.lintel.Lintel2009Request.Action.UNDEPLOY));
	}

	private void sendRequest(RobotDeviceRequest request) {
		getDispatcher().sendRequest(request);
	}

	// ---- SEQUENCE ----

	public void takeColumn() {
		sendRequest(new Sequence2009Request(org.cen.cup.cup2009.device.Sequence2009Request.Action.TAKE_COLUMN));
	}

	public void prepareToBuild() {
		sendRequest(new Sequence2009Request(
				org.cen.cup.cup2009.device.Sequence2009Request.Action.PREPARE_TO_BUILD_FIRST_CONSTRUCTION));
	}

	public void goForward() {
		sendRequest(new Sequence2009Request(org.cen.cup.cup2009.device.Sequence2009Request.Action.FORWARD));
	}

	public void buildFirstColumnsAndLintelType1() {
		sendRequest(new Sequence2009Request(
				org.cen.cup.cup2009.device.Sequence2009Request.Action.FIRST_BUILD_COLUMNS_TYPE_1));
		sendRequest(new Sequence2009Request(
				org.cen.cup.cup2009.device.Sequence2009Request.Action.FIRST_BUILD_LINTEL_TYPE1));
	}

	public void buildFirstColumnsAndLintelType2() {
		sendRequest(new Sequence2009Request(
				org.cen.cup.cup2009.device.Sequence2009Request.Action.FIRST_BUILD_COLUMNS_AND_LINTEL_TYPE_2));
	}

	public void loadSecondLintel() {
		sendRequest(new Sequence2009Request(org.cen.cup.cup2009.device.Sequence2009Request.Action.LOAD_SECOND_LINTEL));
	}

	public void buildSecondLintel() {
		sendRequest(new Sequence2009Request(
				org.cen.cup.cup2009.device.Sequence2009Request.Action.SECOND_LINTEL_CONSTRUCTION));
	}

	public void motorTest() {
		// Long Forward
		sendRequest(new StopRequest());
		MoveRequest m = new MoveRequest(800);
		sendRequest(m);
		delay(m.getEstimatedTime());
		sendRequest(new StopRequest());
		// Long Backward
		m = new MoveRequest(-800);
		sendRequest(m);
		delay(m.getEstimatedTime());
		sendRequest(new StopRequest());

		// Middle Forward
		m = new MoveRequest(400);
		sendRequest(m);
		delay(m.getEstimatedTime());
		sendRequest(new StopRequest());
		// Middle Backward
		m = new MoveRequest(-400);
		sendRequest(m);
		delay(m.getEstimatedTime());
		sendRequest(new StopRequest());

		// Short Forward
		m = new MoveRequest(200);
		sendRequest(m);
		delay(m.getEstimatedTime());
		sendRequest(new StopRequest());
		// Short Backward
		m = new MoveRequest(-200);
		sendRequest(m);
		delay(m.getEstimatedTime());
		sendRequest(new StopRequest());

		// Long Rotation
		RotationRequest r = new RotationRequest(Math.PI);
		sendRequest(r);
		delay(2000);
		sendRequest(new StopRequest());

		r = new RotationRequest(-Math.PI);
		sendRequest(r);
		delay(2000);
		sendRequest(new StopRequest());

		// Middle Rotation
		r = new RotationRequest(Math.PI / 2);
		sendRequest(r);
		delay(2000);
		sendRequest(new StopRequest());

		r = new RotationRequest(-Math.PI / 2);
		sendRequest(r);
		delay(2000);
		sendRequest(new StopRequest());

		// Short Rotation
		r = new RotationRequest(Math.PI / 6);
		sendRequest(r);
		delay(2000);
		sendRequest(new StopRequest());

		r = new RotationRequest(-Math.PI / 6);
		sendRequest(r);
		delay(2000);
		sendRequest(new StopRequest());

	}

	public void completeSequence() {
		takeColumn();
		delay(1000);
		for (int i = 0; i < 4; i++) {
			goForward();
			delay(5000);
			takeColumn();
			delay(1000);
		}
		buildFirstColumnsAndLintelType1();
	}

	public void delay(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}
}
