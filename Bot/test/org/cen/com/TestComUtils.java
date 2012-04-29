package org.cen.com;

import java.util.Enumeration;

import javax.comm.CommPortIdentifier;

import com.sun.comm.Win32Driver;

public class TestComUtils {

	public static void waitForSerialPort(String port) {
		System.out.println("waiting for com port: " + port);
		try {
			Win32Driver w32Driver = new Win32Driver();
			CommPortIdentifier portId = null;
			while (true) {
				w32Driver.initialize();
				Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();

				// Loop after the port List
				while (portList.hasMoreElements()) {
					portId = (CommPortIdentifier) portList.nextElement();
					// Test if the port is serial Port
					if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
						if (portId.getName().equals(port)) {
							System.out.println("Port " + port + " found");
							return;
						}
					}
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		waitForSerialPort("COM4");
	}
}
