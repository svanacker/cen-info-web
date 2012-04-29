package org.cen.simulRobot.device.configuration;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.configuration.ConfigurationResult;
import org.cen.simulRobot.device.com.AckSimulOutData;
import org.cen.simulRobot.device.configuration.com.ConfigurationSimulDataDecoder;
import org.cen.simulRobot.device.configuration.com.ConfigurationSimulReadInData;
import org.cen.simulRobot.device.configuration.com.ConfigurationSimulReadOutData;

public class ConfigurationSimulDevice extends AbstractRobotDevice implements InDataListener {
	public final static String NAME = "configuration";

	private RobotDeviceRequest request;

	/**
	 * Constructor.
	 */
	public ConfigurationSimulDevice() {
		super(NAME);
	}

	/**
	 * Constructor for a cup specific implementation.
	 * 
	 * @param name
	 *            the name of the device
	 */
	protected ConfigurationSimulDevice(String name) {
		super(name);
	}

	@Override
	public void debug(String debugAction) {
	}

	private ConfigurationResult getResult(ConfigurationSimulReadInData data) {
		return new ConfigurationSimulReadResult(request);
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new ConfigurationSimulDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		this.request = request;

		if (request instanceof ConfigurationSimulReadRequest) {
			int avalue = ((ConfigurationSimulReadRequest)request).getValeur();
			send(new ConfigurationSimulReadOutData(avalue));
		}
	}

	protected void notifyResult(ConfigurationResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.notifyListeners(this, result);
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof ConfigurationSimulReadInData) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new AckSimulOutData("c"));
			notifyResult(getResult((ConfigurationSimulReadInData) data));
		}
	}

	void send(OutData outData) {

		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
