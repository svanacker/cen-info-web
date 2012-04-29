package org.cen.ui.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.cen.robot.IRobotServiceProvider;
import org.cen.ui.rtp.VideoRecorder;

public class VideoRecorderView {
	private IRobotServiceProvider provider;

	private String status;

	private String destination;

	public VideoRecorderView() {
		super();
		destination = getNewDestination();
	}

	private String getNewDestination() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
		return System.getProperty("user.home") + File.separator + "robot-" + format.format(Calendar.getInstance().getTime()) + ".mpeg";
	}

	public void execute() {
		VideoRecorder recorder = provider.getService(VideoRecorder.class);
		if (recorder == null) {
			status = "video recorder service NOT present";
			return;
		}
		try {
			if (recorder.isRecording()) {
				recorder.stop();
				status = "stopped";
				destination = getNewDestination();
			} else {
				recorder.setDestination("file://" + destination);
				recorder.start();
				status = "recording";
			}
		} catch (Exception e) {
			status = e.toString();
			e.printStackTrace();
		}
	}

	public String getDestination() {
		return destination;
	}

	public String getStatus() {
		return status;
	}

	public String getText() {
		return isRecording() ? "Stop" : "Start";
	}

	private boolean isRecording() {
		VideoRecorder recorder = provider.getService(VideoRecorder.class);
		return (recorder == null) ? false : recorder.isRecording();
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		this.provider = provider;
		VideoRecorder recorder = provider.getService(VideoRecorder.class);
		if (recorder == null) {
			status = "video recorder service NOT present";
			return;
		} else {
			status = "video recorder service present";
		}
	}
}
