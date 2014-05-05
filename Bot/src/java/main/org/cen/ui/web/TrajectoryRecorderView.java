package org.cen.ui.web;

import java.util.Iterator;
import java.util.List;

import org.cen.navigation.recorder.ITrajectoryRecorder;
import org.cen.navigation.recorder.TrajectoryRecord;
import org.cen.robot.IRobotServiceProvider;

public class TrajectoryRecorderView {
	private IRobotServiceProvider servicesProvider;

	public void delete() {
		Iterator<TrajectoryRecord> i = getRecords().iterator();
		while (i.hasNext()) {
			if (i.next().isActive()) {
				i.remove();
			}
		}
	}

	public List<TrajectoryRecord> getRecords() {
		ITrajectoryRecorder r = servicesProvider.getService(ITrajectoryRecorder.class);
		return r.getRecords();
	}

	public boolean isRecording() {
		ITrajectoryRecorder r = servicesProvider.getService(ITrajectoryRecorder.class);
		return r.isRecording();
	}

	public void setServicesProvider(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
	}

	public void start() {
		ITrajectoryRecorder r = servicesProvider.getService(ITrajectoryRecorder.class);
		r.start();
	}

	public void stop() {
		ITrajectoryRecorder r = servicesProvider.getService(ITrajectoryRecorder.class);
		r.stop();
	}
}
