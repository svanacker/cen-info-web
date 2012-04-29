package org.cen.com.documentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cen.com.IComService;
import org.cen.com.InDataDecoder;
import org.cen.com.out.OutData;
import org.cen.com.out.OutDataSender;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;

/**
 * Handles the documentation of communication incoming and outgoing data. Builds
 * a list of strings representing methods of devices with arguments and their
 * lengths and types. Syntax for device incoming data: <br>
 * i|device name|header|arg1;length1;type1|arg2;... Syntax for device outgoing
 * data:<br>
 * o|device name|header|arg1;length1;type1|arg2;... The lengths are given in
 * bytes used by the communication protocol. If the argument has a variable
 * length (strings for example), the character ? is used as length and the type
 * is not mentioned The types are:<br>
 * - u: unsigned<br>
 * - s: signed<br>
 * - -: no data type<br>
 * 
 * @author Emmanuel ZURMELY
 */
public class ComDataDocumentationHandler {

	private static final String TYPE_UNSPECIFIED = "-";

	private static final String TYPE_SIGNED = "s";

	private static final String TYPE_UNSIGNED = "u";

	private static final int INDEX_HEADER = 2;

	private final IRobotServiceProvider provider;

	private boolean warnIfUndocumented = true;

	/**
	 * Constructor.
	 * 
	 * @param provider
	 *            the services provider
	 */
	public ComDataDocumentationHandler(IRobotServiceProvider provider) {
		super();
		this.provider = provider;
	}

	private void addMethods(List<String> results, DeviceDataSignature signature) {
		DeviceMethodSignature[] methods = signature.methods();
		for (DeviceMethodSignature method : methods) {
			StringBuilder builder = new StringBuilder();
			switch (method.type()) {
			case INPUT:
				builder.append('i');
				break;
			case OUTPUT:
				builder.append('o');
				break;
			}
			builder.append('|');
			builder.append(signature.deviceName());
			builder.append('|');
			builder.append(method.header());
			DeviceParameter[] parameters = method.parameters();
			for (DeviceParameter parameter : parameters) {
				builder.append('|');
				builder.append(parameter.name());
				builder.append(';');
				int l = parameter.length();
				if (l == 0) {
					// Variable length parameter
					builder.append('?');
				} else {
					builder.append(l);
					builder.append(';');
					String s;
					switch (parameter.type()) {
					case UNSPECIFIED:
						s = TYPE_UNSPECIFIED;
						break;
					case SIGNED:
						s = TYPE_SIGNED;
						break;
					case UNSIGNED:
						s = TYPE_UNSIGNED;
						break;
					default:
						s = TYPE_UNSPECIFIED;
						break;
					}
					builder.append(s);
				}
				builder.append('|');
				builder.append(parameter.unit());
			}
			results.add(builder.toString());
		}
	}

	/**
	 * Compares the data descriptors and returns the differences.
	 * 
	 * @param descriptors1
	 *            first list of descriptors
	 * @param descriptors2
	 *            second list of descriptors
	 * @return the list of differences
	 */
	public List<String> compareDescriptors(List<String> descriptors1, List<String> descriptors2) {
		ArrayList<String> differences = new ArrayList<String>();
		Collections.sort(descriptors1);
		Collections.sort(descriptors2);
		int n1 = descriptors1.size();
		int n2 = descriptors2.size();
		int i1 = 0, i2 = 0;
		while (i1 < n1 || i2 < n2) {
			String s1 = getDescriptor(descriptors1, i1);
			String s2 = getDescriptor(descriptors2, i2);
			if (s1 == null) {
				differences.add("missing in source: " + s2);
				i2++;
				continue;
			}
			if (s2 == null) {
				differences.add("missing in destination: " + s1);
				i1++;
				continue;
			}
			int n = s1.compareTo(s2);
			if (n == 0) {
				// strings match
				i1++;
				i2++;
			} else {
				if (isSameMethod(s1, s2)) {
					// strings differ but are descriptors of the same method
					differences.add("difference between source: " + s1 + ", and destination: " + s2);
					i1++;
					i2++;
				} else if (n < 0) {
					differences.add("missing in destination: " + s1);
					i1++;
				} else {
					differences.add("missing in source: " + s2);
					i2++;
				}
			}
		}

		return differences;
	}

	private Set<InDataDecoder> getDecoders() {
		IComService comService = provider.getService(IComService.class);
		return comService.getDecodingService().getDecoders();
	}

	private String getDescriptor(List<String> descriptors1, int index) {
		if (descriptors1.size() <= index) {
			return null;
		} else {
			return descriptors1.get(index);
		}
	}

	private Set<Class<? extends OutData>> getEncoders() {
		HashSet<Class<? extends OutData>> results = new HashSet<Class<? extends OutData>>();
		IRobotDevicesHandler handler = provider.getService(IRobotDevicesHandler.class);
		Collection<IRobotDevice> devices = handler.getDevices().values();
		for (IRobotDevice device : devices) {
			Class<? extends IRobotDevice> deviceInterface = device.getClass();
			OutDataSender sender = deviceInterface.getAnnotation(OutDataSender.class);
			if (sender != null) {
				for (Class<? extends OutData> outDataClass : sender.classes()) {
					results.add(outDataClass);
				}
			}
		}
		return results;
	}

	/**
	 * Returns the list of the methods descriptors.
	 * 
	 * @return the list of the methods descriptors
	 */
	public List<String> getMethodDescriptors() {
		List<String> results = new ArrayList<String>();
		List<String> fromInDataDecoders = getSignatureFromInDataDecoders();
		List<String> fromOutData = getSignatureFromOutData();
		results.addAll(fromInDataDecoders);
		results.addAll(fromOutData);

		return results;
	}

	private List<String> getSignatureFromInDataDecoders() {
		List<String> results = new ArrayList<String>();
		Set<InDataDecoder> decoders = getDecoders();
		for (InDataDecoder decoder : decoders) {
			DeviceDataSignature signature = decoder.getClass().getAnnotation(DeviceDataSignature.class);
			if (signature == null) {
				// The decoder has no signature
				if (warnIfUndocumented) {
					results.add("undocumented decoder: " + decoder.getClass().getName());
				}
			} else {
				addMethods(results, signature);
			}
		}
		return results;
	}

	private List<String> getSignatureFromOutData() {
		List<String> results = new ArrayList<String>();
		Set<Class<? extends OutData>> encoders = getEncoders();
		for (Class<? extends OutData> encoder : encoders) {
			DeviceDataSignature signature = encoder.getAnnotation(DeviceDataSignature.class);
			if (signature == null) {
				// The encoder has no signature
				if (warnIfUndocumented) {
					results.add("undocumented encoder: " + encoder.getClass().getName());
				}
			} else {
				addMethods(results, signature);
			}
		}
		return results;
	}

	/**
	 * Returns the flag that determines whether the undocumented data are listed
	 * or not.
	 * 
	 * @return TRUE if undocumented data are listed, FALSE otherwise
	 */
	public boolean getWarnIfUndocumented() {
		return warnIfUndocumented;
	}

	private boolean isSameMethod(String s1, String s2) {
		String[] l1 = s1.split("\\|");
		String[] l2 = s2.split("\\|");
		if (l1.length >= INDEX_HEADER && l2.length >= INDEX_HEADER) {
			for (int i = 0; i <= INDEX_HEADER; i++) {
				if (!l1[i].equals(l2[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets then flag that determines whether the undocumented data are listed
	 * or not.
	 * 
	 * @param warnIfUndocumented
	 *            TRUE to list undocumented data, FALSE otherwise
	 */
	public void setWarnIfUndocumented(boolean warnIfUndocumented) {
		this.warnIfUndocumented = warnIfUndocumented;
	}
}
