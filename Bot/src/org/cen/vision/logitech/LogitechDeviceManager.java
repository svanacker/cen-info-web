package org.cen.vision.logitech;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIClsid;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.win32.IJIDispatch;
import org.jinterop.dcom.win32.JIComFactory;

/**
 * Required configuration: limited user account "test" with password "test",
 * remote DCOM access for user "test", firewall: port TCP 135 + application
 * dllhost.exe, LogiDeviceManager.DeviceManager must be configured with
 * DllSurrogate:
 * [HKEY_LOCAL_MACHINE\SOFTWARE\Classes\AppID\{863C43AB-00EB-483F-B886-BFC137F3C779}]
 * "DllSurrogate"=""
 * 
 * @author Emmanuel ZURMELY
 */
public class LogitechDeviceManager {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	public static void main(String[] args) throws Exception {
		LogitechDeviceManager m = new LogitechDeviceManager();
		m.initialize("", "");
		m.enumerateDevices();
		String s = m.getVideoDeviceByIndex(0);
		m.setVideoDeviceProperty(s, VideoDeviceProperty.VDP_GAIN_CURRENTPOSITION, 3000);
		m.setVideoDeviceProperty(s, VideoDeviceProperty.VDP_EXPOSURE_CURRENTPOSITION, 95);

		try {
			// Wait that the server updates the webcam...
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(m.getVideoDeviceProperty(s, VideoDeviceProperty.VDP_GAIN_CURRENTPOSITION));
		System.out.println(m.getVideoDeviceProperty(s, VideoDeviceProperty.VDP_EXPOSURE_CURRENTPOSITION));

		m.uninitialize();
	}

	private IJIDispatch dispatch;

	private IJIComObject instance;

	private JIComServer server;

	private JISession session;

	public LogitechDeviceManager() throws Exception {
		JISystem.getLogger().setLevel(Level.WARNING);
		JISystem.setAutoRegisteration(true);
		session = JISession.createSession("", "test", "test");
		server = new JIComServer(JIClsid.valueOf("863C43AB-00EB-483F-B886-BFC137F3C779"), "localhost", session);
		instance = server.createInstance();
		dispatch = (IJIDispatch) JIComFactory.createCOMInstance(JIComFactory.IID_IDispatch, instance);
	}

	public void close() {
		uninitialize();
		try {
			JISession.destroySession(session);
		} catch (JIException e) {
			log(e);
		}
	}

	public void enumerateDevices() {
		try {
			dispatch.callMethod("EnumerateDevices");
		} catch (JIException e) {
			log(e);
		}
	}

	public String getVideoDeviceByIndex(int index) {
		String device = null;
		JIVariant[] result;
		try {
			result = dispatch.callMethodA("GetVideoDeviceByIndex", new Object[] { index });
			device = result[0].getObjectAsString().getString();
		} catch (JIException e) {
			log(e);
		}
		return device;
	}

	public Object getVideoDeviceProperty(String device, int property) {
		JIVariant[] result;
		try {
			result = dispatch.callMethodA("GetVideoDeviceProperty", new Object[] { new JIString(device), property });
			return result[0].getObject();
		} catch (JIException e) {
			log(e);
		}
		return null;
	}

	public void initialize(String bstrSubscriberID, String bstrApplicationPath) {
		try {
			dispatch.callMethod("Initialize", new Object[] { new JIString(bstrSubscriberID), new JIString(bstrApplicationPath) });
		} catch (JIException e) {
			log(e);
		}
	}

	private void log(JIException e) {
		LOGGER.warning(e.getMessage());
	}

	public void setVideoDeviceProperty(String device, int property, Object value) {
		if (value instanceof String) {
			value = new JIString((String) value);
		}
		try {
			dispatch.callMethod("SetVideoDeviceProperty", new Object[] { new JIString(device), property, value });
		} catch (JIException e) {
			log(e);
		}
	}

	public void uninitialize() {
		try {
			dispatch.callMethod("Uninitialize");
		} catch (JIException e) {
			log(e);
		}
	}
}
