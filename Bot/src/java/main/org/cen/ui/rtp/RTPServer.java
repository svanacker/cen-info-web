package org.cen.ui.rtp;

import java.awt.Dimension;
import java.net.InetAddress;

import javax.media.Codec;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Format;
import javax.media.Manager;
import javax.media.Owned;
import javax.media.Player;
import javax.media.Processor;
import javax.media.control.QualityControl;
import javax.media.control.TrackControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;

public class RTPServer {
	class StateListener implements ControllerListener {

		public void controllerUpdate(ControllerEvent ce) {

			// If there was an error during configure or
			// realize, the processor will be closed
			if (ce instanceof ControllerClosedEvent)
				setFailed();

			// All controller events, send a notification
			// to the waiting thread in waitForState method.
			if (ce instanceof ControllerEvent) {
				synchronized (getStateLock()) {
					getStateLock().notifyAll();
				}
			}
		}
	}

	private Integer stateLock = new Integer(0);

	private boolean failed = false;

	public RTPServer(DataSource dataSource) throws Exception {
		super();

		DataSource dataOutput = dataSource;
		Processor processor = null;

		try {
			processor = Manager.createProcessor(dataSource);
			boolean result = waitForState(processor, Processor.Configured);
			if (!result) {
				System.err.println("Couldn't configure processor");
			}

			ContentDescriptor cd = new ContentDescriptor(
					ContentDescriptor.RAW_RTP);
			processor.setContentDescriptor(cd);

			Format supported[];
			Format chosen;
			boolean atLeastOneTrack = false;

			// Get the tracks from the processor
			TrackControl[] tracks = processor.getTrackControls();

			// Program the tracks.
			for (int i = 0; i < tracks.length; i++) {
				if (tracks[i].isEnabled()) {

					supported = tracks[i].getSupportedFormats();

					// We've set the output content to the RAW_RTP.
					// So all the supported formats should work with RTP.
					// We'll just pick the first one.

					if (supported.length > 0) {
						if (supported[0] instanceof VideoFormat) {
							// For video formats, we should double check the
							// sizes since not all formats work in all sizes.
							chosen = checkForVideoSizes(tracks[i].getFormat(),
									supported[0]);
						} else
							chosen = supported[0];
						tracks[i].setFormat(chosen);
						System.err.println("Track " + i
								+ " is set to transmit as:");
						System.err.println("  " + chosen);
						atLeastOneTrack = true;
					} else
						tracks[i].setEnabled(false);
				} else
					tracks[i].setEnabled(false);
			}

			if (!atLeastOneTrack) {
				System.err
						.println("Couldn't set any of the tracks to a valid RTP format");
			}

			result = waitForState(processor, Controller.Realized);
			if (!result) {
				System.err.println("Couldn't realize processor");
			}

			// Set the JPEG quality to .5.
			setJPEGQuality(processor, 0.5f);

			dataOutput = processor.getDataOutput();
		} catch (Exception e) {
			processor = null;
			System.err.println(e);
		}

		// create the RTP Manager
		RTPManager rtpManager = RTPManager.newInstance();

		InetAddress ipAddress = InetAddress.getByName("224.1.1.0");
		System.out.println(ipAddress);

		// create the local endpoint for the local interface on
		// any local port
		SessionAddress localAddress = new SessionAddress(
				InetAddress.getLocalHost(), 3000);

		// initialize the RTPManager
		rtpManager.initialize(localAddress);

		// add the ReceiveStreamListener if you need to receive data
		// and do other application specific stuff
		// ...

		// specify the remote endpoint of this unicast session
		// the address string and port numbers in the following lines
		// need to be replaced with your values.
		// InetAddress ipAddress = InetAddress.getByName("127.0.0.1");

		SessionAddress remoteAddress = new SessionAddress(ipAddress, 3000);

		// open the connection
		rtpManager.addTarget(remoteAddress);

		// create a send stream for the output data source of a processor
		// and start it
		SendStream sendStream = rtpManager.createSendStream(dataOutput, 0);
		sendStream.start();
		System.out.println("RTP started");

		if (processor != null) {
			processor.start();
			System.out.println("Processor started");
		}

		if (dataSource instanceof PushImageDataSource) {
			for (int i = 0; i < 30; i++) {
				((PushImageDataSource) dataSource).update();
				Thread.sleep(1000);
			}
		}

		// send data and do other application specific stuff,
		// ...

		// // close the connection if no longer needed.
		// rtpManager.removeTarget(remoteAddress, "client disconnected.");
		//
		// // call dispose at the end of the life-cycle of this RTPManager so
		// // it is prepared to be garbage-collected.
		// rtpManager.dispose();
	}

	Format checkForVideoSizes(Format original, Format supported) {
		int width, height;
		Dimension size = ((VideoFormat) original).getSize();
		Format jpegFmt = new Format(VideoFormat.JPEG_RTP);
		Format h263Fmt = new Format(VideoFormat.H263_RTP);

		if (supported.matches(jpegFmt)) {
			// For JPEG, make sure width and height are divisible by 8.
			width = (size.width % 8 == 0 ? size.width
					: (int) (size.width / 8) * 8);
			height = (size.height % 8 == 0 ? size.height
					: (int) (size.height / 8) * 8);
		} else if (supported.matches(h263Fmt)) {
			// For H.263, we only support some specific sizes.
			if (size.width < 128) {
				width = 128;
				height = 96;
			} else if (size.width < 176) {
				width = 176;
				height = 144;
			} else {
				width = 352;
				height = 288;
			}
		} else {
			// We don't know this particular format. We'll just
			// leave it alone then.
			return supported;
		}

		return (new VideoFormat(null, new Dimension(width, height),
				Format.NOT_SPECIFIED, null, Format.NOT_SPECIFIED))
				.intersects(supported);
	}

	Integer getStateLock() {
		return stateLock;
	}

	void setFailed() {
		failed = true;
	}

	/**
	 * Setting the encoding quality to the specified value on the JPEG encoder.
	 * 0.5 is a good default.
	 */
	void setJPEGQuality(Player p, float val) {

		Control cs[] = p.getControls();
		QualityControl qc = null;
		VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);

		// Loop through the controls to find the Quality control for
		// the JPEG encoder.
		for (int i = 0; i < cs.length; i++) {

			if (cs[i] instanceof QualityControl && cs[i] instanceof Owned) {
				Object owner = ((Owned) cs[i]).getOwner();

				// Check to see if the owner is a Codec.
				// Then check for the output format.
				if (owner instanceof Codec) {
					Format fmts[] = ((Codec) owner)
							.getSupportedOutputFormats(null);
					for (int j = 0; j < fmts.length; j++) {
						if (fmts[j].matches(jpegFmt)) {
							qc = (QualityControl) cs[i];
							qc.setQuality(val);
							System.err.println("- Setting quality to " + val
									+ " on " + qc);
							break;
						}
					}
				}
				if (qc != null)
					break;
			}
		}
	}

	private synchronized boolean waitForState(Processor p, int state) {
		p.addControllerListener(new StateListener());
		failed = false;

		// Call the required method on the processor
		if (state == Processor.Configured) {
			p.configure();
		} else if (state == Processor.Realized) {
			p.realize();
		}

		// Wait until we get an event that confirms the
		// success of the method, or a failure event.
		// See StateListener inner class
		while (p.getState() < state && !failed) {
			synchronized (getStateLock()) {
				try {
					getStateLock().wait();
				} catch (InterruptedException ie) {
					return false;
				}
			}
		}

		if (failed)
			return false;
		else
			return true;
	}

}
