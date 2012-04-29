package org.cen.com;

import java.io.IOException;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import org.cen.ApplicationConst;
import org.cen.logging.LoggingUtils;

import com.sun.comm.Win32Driver;

/**
 * Communication between the PC and the main Board of the robot. Manages inData,
 * outData, acknowlegment.
 * 
 * @author svanacker
 * @version 19/03/2006
 */
public class ComManager extends AbstractComManager implements SerialPortEventListener {

	private static final int SERIAL_PORT_SPEED = 115200;

	private static final int SERIAL_PORT_TIME_OUT = 5000;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	/**
	 * The encapsulation of the serial Port for communication between PC and
	 * board.
	 */
	protected SerialPort serialPort;

	/**
	 * Constructor. Creates the connection to the COM PORT.
	 */
	protected ComManager() {
		super();
	}

	private void initializeWin32DriverIfNecessary() {
		if (!initialized) {
			LOGGER.config("Initializing Win32Driver");
			initialized = true;
			Win32Driver w32Driver = new Win32Driver();
			w32Driver.initialize();
		}
	}

	private void initSerialPort(CommPortIdentifier portId) throws PortInUseException, IOException,
			TooManyListenersException, UnsupportedCommOperationException {
		LOGGER.config("Port " + portId.getName() + " used");
		serialPort = (SerialPort) portId.open("SimpleReadApp", SERIAL_PORT_TIME_OUT);
		inputStream = serialPort.getInputStream();
		outputStream = serialPort.getOutputStream();
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
		serialPort.setSerialPortParams(SERIAL_PORT_SPEED, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	protected void initialize() {
		String port = getComPortName();
		try {
			initializeWin32DriverIfNecessary();

			CommPortIdentifier portId = null;
			Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

			// Loop after the port List
			while (portList.hasMoreElements()) {
				CommPortIdentifier id = portList.nextElement();
				// Test if the port is serial Port
				int portType = id.getPortType();
				if (portType == CommPortIdentifier.PORT_SERIAL) {
					String portName = id.getName();
					LOGGER.config("Port " + portName + " found");
					if (portName.equals(port)) {
						portId = id;
					}
				}
			}
			if (portId != null) {
				initSerialPort(portId);
			}
		} catch (javax.comm.PortInUseException piue) {
			LOGGER.warning("The Port cannot be opened");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		terminated = false;
		sendingThread = new Thread(this, "ComManagerSendingThread");
		sendingThread.start();
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (inputStream == null) {
			LOGGER.warning("The serial input Stream can not be opened : no in data will be managed");
			return;
		}
		int eventType = event.getEventType();
		switch (eventType) {
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			try {
				while (inputStream.available() > 0) {
					int buffer = inputStream.read();

					// ignores the character NUL
					if (buffer == 0) {
						continue;
					}

					String bufferAsString = String.valueOf((char) buffer);
					comServiceListener.fireDebugListener(bufferAsString);

					if (waitForAck) {
						boolean b;
						synchronized (this) {
							b = (buffer == IComService.MICRO_CONTROLLER_ACK);
							ackResponse = b;
							notify();
							waitForAck = false;
						}
						// continue if the ack was received
						if (b) {
							continue;
						}
					}

					appendChar(buffer);
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
			break;
		}
	}

	protected String getComPortName() {
		String result = properties.getProperty(ApplicationConst.PROPERTY_COMPORT);
		return result;
	}

	@Override
	public void reconnect() {
		LOGGER.fine("Resetting the COM port");
		shutdown();
		// clearing the outgoing queue
		outDataQueue.clear();
		initialize();
	}

	@PreDestroy
	protected void shutdown() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
			serialPort = null;
		}
		if (sendingThread != null) {
			sendingThread.interrupt();
			try {
				sendingThread.join(100);
			} catch (InterruptedException e) {
				LOGGER.fine(e.getMessage());
			}
			sendingThread = null;
		}
	}

}
