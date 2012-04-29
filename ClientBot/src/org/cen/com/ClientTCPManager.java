package org.cen.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.event.EventListenerList;

import org.cen.ClientApplicationConst;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.in.RawInData;
import org.cen.com.out.OutData;
import org.cen.com.out.OutDataListener;
import org.cen.robot.IRobotServiceProvider;
import org.cen.simulRobot.device.lcd.com.LCDSimulDataDecoder;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of IComService based on a TCP socket.
 * 
 * @author Omar Benouamer
 * 
 */
public class ClientTCPManager implements Runnable, IComService {
	private class TCPConnectionsListener implements Runnable {
		private String host;

		private boolean listenerTerminated;
		private int port;

		private Socket s = null;

		public TCPConnectionsListener(int aport, String ahost) {
			super();
			this.port = aport;
			this.host = ahost;
		}

		@Override
		public void run() {

			while (!listenerTerminated) {
				try {
					if (server == null) {
						s = new Socket(host, port);
						if (s != null) {
							setServer(s);
							listenerTerminated = true;
						} else {
							s.close();
							s = null;
						}
					}
				} catch (IOException e) {
				}
			}
		}

		public void shutdown() {
			listenerTerminated = true;
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
				}
			}
		}
	}


	/**
	 * State of the last wait for the ACK response.
	 */
	private boolean ackResponse;

	private TCPConnectionsListener connectionListener;

	private final StringBuilder currentString = new StringBuilder();

	/**
	 * Current decoder for incomming serial data.
	 */
	private InDataDecoder decoder;

	/**
	 * Service object providing incoming data decoders.
	 */
	private final InDataDecodingService decodingService = new InDataDecodingService();

	private InputStream inputStream;

	private final EventListenerList listeners = new EventListenerList();

	/**
	 * Queue of outgoing serial data.
	 */
	private final BlockingQueue<OutData> outDataQueue = new LinkedBlockingQueue<OutData>();

	/**
	 * Manage the output stream to the javelin.
	 */
	private OutputStream outputStream;

	/**
	 * The properties to configure the serial port.
	 */
	protected Properties properties;

	private Thread sendingThread;

	private Socket server;

	/**
	 * Status of the sending thread.
	 */
	private boolean terminated;

	/**
	 * Constructor. Creates the connection to the COM PORT.
	 */
	protected ClientTCPManager() {
		super();
	}

	@Override
	public void addDebugListener(ComDebugListener listener) {
		listeners.add(ComDebugListener.class, listener);
	}

	@Override
	public void addInDataListener(InDataListener comDataListener) {
		listeners.add(InDataListener.class, comDataListener);
	}

	@Override
	public void addOutDataListener(OutDataListener comDataListener) {
		listeners.add(OutDataListener.class, comDataListener);
	}

	private void fireDebugListener(String data) {
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == ComDebugListener.class) {
				try {
					((ComDebugListener) l[i + 1]).onRawInData(new RawInData(data));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Notify the observers on data reception.
	 */
	private void fireInDataListener(InData data) {
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == InDataListener.class) {
				try {
					((InDataListener) l[i + 1]).onInData(data);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Notify the observers on data transmission.
	 */
	private void fireOutDataListener(OutData outData) {
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == OutDataListener.class) {
				try {
					((OutDataListener) l[i + 1]).onOutDataEvent(outData);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@Override
	public InDataDecodingService getDecodingService() {
		return decodingService;
	}

	private InData getInData(String string) throws IllegalComDataException {
		return decodingService.decode(string);
	}

	public Properties getProperties() {
		return properties;
	}


	@PostConstruct
	protected void initialize(){

	}
	public void internalInitialize() {
		final int port = Integer.valueOf(properties.getProperty(ClientApplicationConst.PROPERTY_TCPPORT));
		final String host = properties.getProperty(ClientApplicationConst.PROPERTY_TCPHOST);
		connectionListener = new TCPConnectionsListener(port, host);
		Thread connectionListenerThread = new Thread(connectionListener, "TCPManagerConnectionsListenerThread");
		connectionListenerThread.start();
		terminated = false;
		sendingThread = new Thread(this, "TCPManagerSendingThread");
		sendingThread.start();
	}

	@Override
	public void reconnect() {
		shutdown();
		outDataQueue.clear();
		internalInitialize();
	}

	@Override
	public void run() {
		while (!terminated) {
			OutData outData;
			String message = "";
			try {
				outData = outDataQueue.take();
				fireOutDataListener(outData);
				message = outData.getMessage();
				if (outputStream != null) {

					outputStream.write(message.getBytes());
				}
			} catch (InterruptedException e) {
				terminated = true;
			} catch (IOException e) {
			}
		}
	}

	@Required
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Events which is fired when data arrives.
	 */
	protected void setServer(final Socket s) {
		server = s;
		Thread inDataListenerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					//LOGGER.fine("thread started");

					InputStream inputStream = s.getInputStream();
					outputStream = s.getOutputStream();

					writeOutData(new RobotInitializedSimulOutData("XyY"));

					while (!terminated) {
						int buffer = inputStream.read();

						if (buffer < 0) {
							break;
						}

						fireDebugListener(String.valueOf((char) buffer));

						currentString.append((char) buffer);
						if (decoder == null) {
							decoder = decodingService.getDecoder(currentString.toString());
						}
						if (decoder == null) {
							// Data not understood, discard the buffer
							currentString.setLength(0);
						}//TODO supprimer ce traitement et passer � un LCDDevice � 4 caract�res
						/**/else if (decoder.getHandledHeaders().contains("L") && currentString.length() < 4){
							if(currentString.length() < 2){
							}else if(currentString.length() == 3){
								int lcdSize = (int)ComDataUtils.parseShortHex(currentString.substring(1, 3));
								((LCDSimulDataDecoder)decoder).setDataLength(lcdSize);
							}
							//TODO utiliser le inputStream.read(byte[] b)?
						} /**/else if (currentString.length() == decoder.getDataLength(currentString.substring(0, 1)) + 1) {
							InData data = null;
							try {
								data = getInData(currentString.toString());
								fireInDataListener(data);
							} catch (Exception ex) {
								System.out.println("data: " + data);
								ex.printStackTrace();
							} finally {
								currentString.setLength(0);
								decoder = null;
							}
						}
					}
				} catch (Exception e) {
				}

				server = null;
			}
		}, "TCPInDataListenerThread");
		inDataListenerThread.start();
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		provider.registerService(IComService.class, this);
	}

	@PreDestroy
	protected void shutdown() {
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
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
			}
		}
	}

	@Override
	public void writeOutData(OutData outData) {
		outDataQueue.add(outData);
	}

	@Override
	public void writeInData(String inData) {
		
	}
}
