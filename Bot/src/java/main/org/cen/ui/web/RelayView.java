package org.cen.ui.web;

import java.util.logging.Logger;

import org.cen.com.IComService;
import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.relay.RelayChangeListener;
import org.cen.robot.device.relay.RelayData;
import org.cen.robot.device.relay.com.RelayOutData;

/**
 * Presentation object for the relay.
 */
public class RelayView implements IRobotService, RelayChangeListener {
	private final static Logger logger = LoggingUtils.getClassLogger();

	private int count = 1;

	private RelayData[] data;

	private int maxCount;

	private IRobotServiceProvider servicesProvider;

	public RelayView() {
		super();
		setMaxCount(8);
	}

	public int getCount() {
		return count;
	}

	public RelayData[] getData() {
		return data;
	}

	public int getMaxCount() {
		return maxCount;
	}
	
	@Override
	public void onRelayChange(RelayData relayData) {
		logger.finest("relay change: " + relayData.toString());
		sendRelayData(relayData);
	}

	protected void sendRelayData(RelayData relayData) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new RelayOutData(relayData));
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setData(RelayData[] data) {
		this.data = data;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
		if (data == null) {
			data = new RelayData[maxCount];
		}
		else if (data.length < maxCount) {
			RelayData[] oldData = data;
			data = new RelayData[maxCount];
			for (int i = 0; i < oldData.length; i++) {
				data[i] = oldData[i];
			}
		}
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				data[i] = new RelayData(i + 1);
				data[i].addRelayChangeListener(this);
			}
		}
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}

}
