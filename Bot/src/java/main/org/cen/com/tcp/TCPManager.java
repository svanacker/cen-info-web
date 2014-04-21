package org.cen.com.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.cen.ApplicationConst;
import org.cen.com.AbstractComManager;
import org.cen.com.ComManager;
import org.cen.logging.LoggingUtils;

/**
 * Implementation of IComService based on a TCP socket.
 * 
 * @author Emmanuel ZURMELY
 * @version 27/03/2009
 */
public class TCPManager extends AbstractComManager {

	private class TCPConnectionsListener implements Runnable {

		private boolean listenerTerminated;

		private final int port;

		private ServerSocket server = null;

		public TCPConnectionsListener(int port) {
			super();
			this.port = port;
		}

		@Override
		public void run() {
			LOGGER.fine("thread started");
			try {
				server = new ServerSocket(port);
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "unable to bind the incomming socket", e);
				return;
			}
			while (!listenerTerminated) {
				try {
					LOGGER.finest("waiting for incomming connection");
					Socket s = server.accept();
					LOGGER.finest("accepted incomming connection");
					if (client == null) {
						LOGGER.finest("connecting to client");
						setClient(s);
					} else {
						LOGGER.finest("client already connected, new connection rejected");
						s.close();
					}
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "unable to accept an incomming connection", e);
				}
			}
			LOGGER.fine("thread terminated");
		}

		public void shutdown() {
			listenerTerminated = true;
			try {
				server.close();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "unable to accept an incomming connection", e);
			}
		}
	}

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private Socket client;

	private TCPConnectionsListener connectionListener;

	/**
	 * Constructor. Creates the connection to the COM PORT.
	 */
	protected TCPManager() {
		super();
	}

	@PostConstruct
	protected void initialize() {
		final int port = Integer.valueOf(properties.getProperty(ApplicationConst.PROPERTY_TCPPORT));
		connectionListener = new TCPConnectionsListener(port);
		Thread connectionListenerThread = new Thread(connectionListener, "TCPManagerConnectionsListenerThread");
		connectionListenerThread.start();

		terminated = false;
		sendingThread = new Thread(this, "TCPManagerSendingThread");
		sendingThread.start();
	}

	@Override
	public void reconnect() {
		LOGGER.fine("Resetting the TCP connection");
		shutdown();
		// clearing the outgoing queue
		outDataQueue.clear();
		initialize();
	}

	protected void setClient(final Socket s) {
		client = s;
		Thread inDataListenerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					LOGGER.fine("thread started");

					InputStream inputStream = s.getInputStream();
					outputStream = s.getOutputStream();
					while (!terminated) {
						int buffer = inputStream.read();

						if (buffer < 0) {
							LOGGER.finest("client connection has been closed");
							break;
						}

						comServiceListener.fireDebugListener(String.valueOf((char) buffer));

						if (waitForAck) {
							System.out.println(TCPManager.this);
							synchronized (TCPManager.this) {
								ackResponse = (buffer == ComManager.MICRO_CONTROLLER_ACK);
								TCPManager.this.notify();
								waitForAck = false;
							}
						}

						appendChar(buffer);
					}
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}

				LOGGER.fine("thread terminated");
				client = null;
			}
		}, "TCPInDataListenerThread");
		inDataListenerThread.start();
	}

	@PreDestroy
	protected void shutdown() {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				LOGGER.fine(e.getMessage());
			}
		}
		if (connectionListener != null) {
			connectionListener.shutdown();
		}
		if (sendingThread != null) {
			sendingThread.interrupt();
			try {
				sendingThread.join(100);
			} catch (InterruptedException e) {
				LOGGER.fine(e.getMessage());
			}
		}
	}

}
