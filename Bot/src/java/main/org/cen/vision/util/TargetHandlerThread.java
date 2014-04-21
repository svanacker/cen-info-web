package org.cen.vision.util;

public class TargetHandlerThread implements Runnable {
	private TargetHandler handler;

	private boolean paused;

	private long sleepingTime = 250;

	private boolean stopped;

	private final Thread thread;

	public TargetHandlerThread(TargetHandler handler) {
		super();
		this.handler = handler;
		paused = true;
		thread = new Thread(this, getClass().getSimpleName());
		thread.start();
	}

	public long getSleepingTime() {
		return sleepingTime;
	}

	public boolean isRunning() {
		return thread.isAlive() && !paused;
	}

	public void join() throws InterruptedException {
		thread.join();
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
		synchronized (this) {
			notify();
		}
	}

	public void run() {
		sleep();
		while (!stopped) {
			handler.update();
			sleep();
		}
	}

	public void setSleepingTime(long sleepingTime) {
		this.sleepingTime = sleepingTime;
	}

	private void sleep() {
		try {
			if (paused)
				synchronized (this) {
					wait();
				}
			else
				Thread.sleep(sleepingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		stopped = true;
		if (paused)
			synchronized (this) {
				notify();
			}
	}
}
