package org.cen.robot.device;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.event.EventListenerList;

import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;

public class RobotDevicesHandler implements IRobotService, IRobotDevicesHandler {
	private Map<String, IRobotDevice> devices;

	private DeviceRequestDispatcher requestsDispatcher;

	private DeviceResultDispatcher resultsDispatcher;

	private EventListenerList listeners;

	public RobotDevicesHandler() {
		super();
	}

	@Override
	public void addDeviceDebugListener(RobotDeviceDebugListener listener) {
		listeners.add(RobotDeviceDebugListener.class, listener);
	}

	@Override
	public void addDeviceListener(RobotDeviceListener listener) {
		listeners.add(RobotDeviceListener.class, listener);
	}

	@Override
	public IRobotDevice getDevice(String name) {
		return devices.get(name);
	}

	@Override
	public Map<String, IRobotDevice> getDevices() {
		return devices;
	}

	@Override
	public Object getProperty(String deviceName, String propertyName) {
		IRobotDevice device = getDevice(deviceName);
		if (device != null) {
			return device.getProperty(propertyName);
		} else {
			return null;
		}
	}

	@Override
	public DeviceRequestDispatcher getRequestDispatcher() {
		return requestsDispatcher;
	}

	@Override
	public DeviceResultDispatcher getResultDispatcher() {
		return resultsDispatcher;
	}

	@PostConstruct
	protected void initialize() {
		devices = new HashMap<String, IRobotDevice>();
		listeners = new EventListenerList();
		requestsDispatcher = new DeviceRequestDispatcher(this);
		resultsDispatcher = new DeviceResultDispatcher(this);
	}

	@Override
	public void notifyDebug(IRobotDevice device, RobotDeviceRequest request, RobotDeviceResult result) {
		// Guaranteed to return a non-null array
		Object[] l = listeners.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == RobotDeviceDebugListener.class) {
				RobotDeviceDebugListener listener = ((RobotDeviceDebugListener) l[i + 1]);
				if (listener.getDeviceName().equals(device.getName())) {
					listener.debugEvent(new RobotDeviceDebugEvent(request, result));
				}
			}
		}
	}

	@Override
	public void notifyListeners(IRobotDevice device, RobotDeviceResult result) {
		// Guaranteed to return a non-null array
		Object[] l = listeners.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == RobotDeviceListener.class) {
				RobotDeviceListener listener = ((RobotDeviceListener) l[i + 1]);
				if (device.getName().equals(listener.getDeviceName())) {
					listener.handleResult(result);
				}
			}
		}
		notifyDebug(device, result.request, result);
	}

	@Override
	public void registerDevice(IRobotDevice device) {
		devices.put(device.getName(), device);
	}

	@Override
	public void removeDeviceDebugListener(RobotDeviceDebugListener listener) {
		listeners.remove(RobotDeviceDebugListener.class, listener);
	}

	@Override
	public void removeDeviceListener(RobotDeviceListener listener) {
		listeners.remove(RobotDeviceListener.class, listener);
	}

	@Override
	public void sendRequest(RobotDeviceRequest request) {
		requestsDispatcher.sendRequest(request);
	}

	@Override
	public void sendResult(IRobotDevice device, RobotDeviceResult result) {
		resultsDispatcher.notifyResult(device, result);
	}

	@Override
	public void setProperty(String deviceName, String propertyName, Object value) {
		IRobotDevice device = getDevice(deviceName);
		if (device != null) {
			device.setProperty(propertyName, value);
		}
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		provider.registerService(IRobotDevicesHandler.class, this);
	}

	@PreDestroy
	protected void shutdown() {
		if (requestsDispatcher != null) {
			requestsDispatcher.terminate();
			requestsDispatcher = null;
		}
		if (resultsDispatcher != null) {
			resultsDispatcher.terminate();
			resultsDispatcher = null;
		}
		if (listeners != null) {
			listeners = null;
		}
		if (devices != null) {
			devices.clear();
			devices = null;
		}
	}

	@Override
	public void unregisterDevice(IRobotDevice device) {
		devices.remove(device.getName());
	}
}
