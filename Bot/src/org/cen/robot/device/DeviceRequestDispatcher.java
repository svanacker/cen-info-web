package org.cen.robot.device;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class DeviceRequestDispatcher implements Runnable {
	private static final class PurgeRequest extends RobotDeviceRequest {
		public PurgeRequest() {
			super(null);
			priority = Integer.MAX_VALUE;
		}
	}

	protected static class RobotDeviceRequestComparator implements Comparator<RobotDeviceRequest> {
		public int compare(RobotDeviceRequest o1, RobotDeviceRequest o2) {
			int p1 = o1.getPriority();
			int p2 = o2.getPriority();
			if (p1 == p2) {
				return Long.signum(o1.getUID() - o2.getUID());
			} else if (p1 > p2) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	private static final class TerminateRequest extends RobotDeviceRequest {
		public TerminateRequest() {
			super(null);
			priority = Integer.MAX_VALUE;
		}
	}

	private IRobotDevicesHandler handler;

	private BlockingQueue<RobotDeviceRequest> queue = new PriorityBlockingQueue<RobotDeviceRequest>(15, new RobotDeviceRequestComparator());

	private boolean running = false;

	private boolean terminated = false;

	private Thread thread;

	private int uid = 0;

	private boolean stepByStep = false;

	public DeviceRequestDispatcher(IRobotDevicesHandler handler) {
		super();
		this.handler = handler;
		thread = new Thread(this, getClass().getName());
		thread.start();
	}

	private void debug(IRobotDevice device, RobotDeviceRequest request, RobotDeviceResult result) {
		handler.notifyDebug(device, request, result);
	}

	private IRobotDevice getTargetDevice(RobotDeviceRequest request) {
		String deviceName = request.getDeviceName();
		Map<String, IRobotDevice> devices = handler.getDevices();
		if (devices == null) {
			return null;
		} else {
			return devices.get(deviceName);
		}
	}

	private void handleRequest(RobotDeviceRequest request) {
		if (request instanceof PurgeRequest) {
			queue.clear();
			return;
		}
		IRobotDevice device = getTargetDevice(request);
		if (device == null) {
			return;
		}
		debug(device, request, null);
		device.handleRequest(request);
	}

	public boolean isRunning() {
		return running && !terminated;
	}

	public void join() throws InterruptedException {
		thread.join();
	}

	public void nextStep() {
		synchronized (this) {
			notify();
		}
	}

	public void purgeQueue() {
		queue.offer(new PurgeRequest());
	}

	public void run() {
		running = true;
		System.out.println("DeviceRequestDispatcher started");
		while (!terminated) {
			try {
				RobotDeviceRequest request = queue.take();
				handleRequest(request);
				if (stepByStep) {
					synchronized (this) {
						wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				terminated = true;
			}
		}
		System.out.println("DeviceRequestDispatcher terminated");
	}

	public void sendRequest(RobotDeviceRequest request) {
		request.uid = uid++;
		queue.offer(request);
	}

	public void setStypByStep(boolean value) {
		this.stepByStep = value;
	}

	public void terminate() {
		terminated = true;
		queue.offer(new TerminateRequest());
	}
}
