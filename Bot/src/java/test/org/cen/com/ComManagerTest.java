package org.cen.com;

import javax.comm.SerialPort;

import org.junit.Assert;
import org.junit.Test;

public class ComManagerTest {

	class SimpleComManager extends ComManager {

		@Override
		protected String getComPortName() {
			return "COM4";
		}

		public SerialPort getSerialPort() {
			return serialPort;
		}
	}

	@Test
	public void should_open_serialPort() {
		SimpleComManager comManager = new SimpleComManager();
		comManager.initialize();
		SerialPort serialPort = comManager.getSerialPort();

		Assert.assertNotNull(serialPort);
		comManager.shutdown();
	}

	@Test
	public void should_open_serialPort_several_times() {
		SimpleComManager comManager = new SimpleComManager();
		for (int i = 0; i < 5; i++) {
			comManager.initialize();
			SerialPort serialPort = comManager.getSerialPort();

			Assert.assertNotNull(serialPort);
			comManager.shutdown();
		}
	}
}
