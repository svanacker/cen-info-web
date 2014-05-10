package org.cen.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.cen.com.decoder.IInDataDecoder;
import org.cen.com.decoder.IInDataDecodingService;
import org.cen.com.decoder.impl.InDataDecodingService;
import org.cen.com.impl.ComServiceListener;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.com.out.OutDataListener;
import org.cen.com.tcp.TCPManager;
import org.cen.logging.LoggingUtils;
import org.cen.robot.services.IRobotServiceProvider;
import org.springframework.beans.factory.annotation.Required;

/**
 * Abstract Manager which is common between {@link TCPManager} and
 * {@link ComManager}.
 */
public abstract class AbstractComManager implements Runnable, IComService {

    @Override
    public void reconnect() {

    }

    private static final Logger LOGGER = LoggingUtils.getClassLogger();

    /**
     * Delay between each call to avoid saturation of link with the
     * microcontroller.
     */
    private static final int DELAY_BETWEEN_CALL_MS = 50;

    /**
     * State of the last wait for the ACK response.
     */
    protected boolean ackResponse;

    protected final StringBuilder currentString = new StringBuilder();

    protected boolean initialized;

    /**
     * Encapsulates the input serial stream.
     */
    protected InputStream inputStream;

    /**
     * Queue of outgoing serial data.
     */
    protected final BlockingQueue<OutData> outDataQueue = new LinkedBlockingQueue<OutData>();

    /**
     * Manage the output stream to the micro-controller.
     */
    protected OutputStream outputStream;

    /**
     * The properties to configure the serial port.
     */
    protected Properties properties;

    /**
     * The thread which send data.
     */
    protected Thread sendingThread;

    /**
     * Status of the sending thread.
     */
    protected boolean terminated;

    /**
     * Status flag indicated that the sending thread is waiting for
     * acknowledgement.
     */
    protected boolean waitForAck;

    /**
     * Current decoder for incomming serial data.
     */
    protected IInDataDecoder decoder;

    /**
     * Service object providing incoming data decoders.
     */
    protected final IInDataDecodingService decodingService = new InDataDecodingService();

    protected final IComServiceListener comServiceListener = new ComServiceListener();

    @Override
    public void addDebugListener(IComDebugListener listener) {
        comServiceListener.addDebugListener(listener);
    }

    @Override
    public void addInDataListener(InDataListener comDataListener) {
        comServiceListener.addInDataListener(comDataListener);
    }

    @Override
    public void addOutDataListener(OutDataListener comDataListener) {
        comServiceListener.addOutDataListener(comDataListener);
    }

    @Override
    public void fireDebugListener(String data) {
        comServiceListener.fireDebugListener(data);
    }

    @Override
    public void fireInDataListener(InData data) {
        comServiceListener.fireInDataListener(data);
    }

    @Override
    public void fireOutDataListener(OutData outData) {
        comServiceListener.fireOutDataListener(outData);
    }

    @Override
    public IInDataDecodingService getDecodingService() {
        return decodingService;
    }

    protected InData getInData(String string) throws IllegalComDataException {
        return decodingService.decode(string);
    }

    public Properties getProperties() {
        return properties;
    }

    @Required
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void setServicesProvider(IRobotServiceProvider provider) {
        provider.registerService(IComService.class, this);
    }

    @Override
    public void run() {
        LOGGER.config(Thread.currentThread().getName() + " started");

        if (outputStream == null) {
            LOGGER.warning("The serial output stream cannot be opened: no out data will be send");
        }
        while (!terminated) {
            OutData outData;
            String message = "";
            try {
                outData = outDataQueue.take();
                comServiceListener.fireOutDataListener(outData);
                message = outData.getMessage();
                LOGGER.fine(message);
                if (outputStream != null) {
                    outputStream.write(message.getBytes());
                    if (outData.getWaitForAck()) {
                        waitForAck();
                    }
                    LOGGER.fine("ack");
                }
            } catch (InterruptedException e) {
                terminated = true;
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.info("Problem when writing following message :" + message);
                LOGGER.info(e.getMessage());
            }
        }
        LOGGER.config(Thread.currentThread().getName() + " terminated");
    }

    protected void appendChar(int buffer) {
        currentString.append((char) buffer);
        if (decoder == null) {
            decoder = decodingService.getDecoder(currentString.toString());
        }
        if (decoder == null) {
            // Data not understood, discard the buffer
            currentString.setLength(0);
        } else {
            int currentStringLength = currentString.length();
            String header = currentString.substring(0, 1);
            int dataLength = decoder.getDataLength(header);
            if (currentStringLength == dataLength) {
                try {
                    InData data = getInData(currentString.toString());
                    comServiceListener.fireInDataListener(data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    currentString.setLength(0);
                    decoder = null;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.cen.com.ComService#writeOutData(org.cen.com.out.OutData)
     */
    @Override
    public void writeOutData(OutData outData) {
        outDataQueue.add(outData);
        try {
            Thread.sleep(DELAY_BETWEEN_CALL_MS);
        } catch (InterruptedException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    @Override
    public void writeInData(String inData) {
        comServiceListener.fireDebugListener(inData);
        byte[] bytes = inData.getBytes();
        for (byte c : bytes) {
            appendChar(c);
        }
    }

    private void waitForAck() throws IOException, InterruptedException {
        try {
            ackResponse = false;
            waitForAck = true;
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            waitForAck = false;
            throw e;
        }
        if (!ackResponse) {
            throw new IOException("ACK expected but not received");
        }
    }

}
