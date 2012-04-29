package org.cen.vision.dataobjects;

/**
 * Descriptor of the required calibration operation.
 * 
 * @author Emmanuel ZURMELY
 */
public class CalibrationDescriptor {
	protected CalibrationData[] data;

	protected long maxCalibrationTime = Long.MAX_VALUE;

	protected int maxRetryCount = Integer.MAX_VALUE;

	/** The calibration that we select */
	protected String selectedCalibration;

	public CalibrationDescriptor(CalibrationData[] data) {
		super();
		this.data = data;
	}

	public void clear() {
		for (CalibrationData d : data)
			d.clear();
	}

	public CalibrationData[] getData() {
		return data;
	}

	public CalibrationData getData(String name) {
		for (CalibrationData d : data)
			if (d.getName().equals(name))
				return d;
		return null;
	}

	public long getMaxCalibrationTime() {
		return maxCalibrationTime;
	}

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	public CalibrationData getSelectedCalibration() {
		return getData(selectedCalibration);
	}

	public void setData(CalibrationData[] data) {
		this.data = data;
	}

	public void setMaxCalibrationTime(long maxCalibrationTime) {
		this.maxCalibrationTime = maxCalibrationTime;
	}

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	public void setSelectedCalibration(String selectedCalibration) {
		this.selectedCalibration = selectedCalibration;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (CalibrationData d : data)
			s.append(d.toString());
		return s.toString();
	}
}
