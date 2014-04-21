package org.cen.robot.device;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DeviceResultDispatcher implements Runnable {
	private class RobotDeviceResultNotification {
		public IRobotDevice device;

		public RobotDeviceResult result;

		public RobotDeviceResultNotification(IRobotDevice device, RobotDeviceResult result) {
			super();
			this.device = device;
			this.result = result;
		}
	}

	private final class TerminateNotification extends RobotDeviceResultNotification {
		public TerminateNotification() {
			super(null, null);
		}
	}

	private IRobotDevicesHandler handler;

	private BlockingQueue<RobotDeviceResultNotification> queue = new ArrayBlockingQueue<RobotDeviceResultNotification>(15);

	private boolean running = false;

	private boolean terminated = false;

	private Thread thread;

	public DeviceResultDispatcher(IRobotDevicesHandler handler) {
		super();
		this.handler = handler;
		thread = new Thread(this, getClass().getName());
		thread.start();
	}

	private void handleNotification(RobotDeviceResultNotification notification) {
		IRobotDevice device = notification.device;
		RobotDeviceResult result = notification.result;
		handler.notifyDebug(device, (result == null) ? null : result.request, result);
		handler.notifyListeners(device, result);
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

	public void notifyResult(IRobotDevice device, RobotDeviceResult result) {
		RobotDeviceResultNotification notification = new RobotDeviceResultNotification(device, result);
		queue.offer(notification);
	}

	public void run() {
		running = true;
		System.out.println("DeviceResultDispatcher started");
		while (!terminated) {
			try {
				RobotDeviceResultNotification notification = queue.take();
				if (notification instanceof TerminateNotification) {
					break;
				}
				handleNotification(notification);
			} catch (InterruptedException e) {
				e.printStackTrace();
				terminated = true;
			}
		}
		System.out.println("DeviceResultDispatcher terminated");
	}

	public void terminate() {
		terminated = true;
		queue.offer(new TerminateNotification());
	}
}
