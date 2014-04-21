package org.cen.ui.rtp;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.media.ConfigureCompleteEvent;
import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;

public class VideoRecorder implements IRobotService, ControllerListener, DataSinkListener {
	private IRobotServiceProvider provider;

	private final Object waitSync = new Object();

	private boolean stateTransitionOK = true;

	private final Object waitFileSync = new Object();

	private boolean fileDone = false;

	private boolean fileSuccess = true;

	private Timer timer;

	private boolean stopped = false;

	private boolean recording = false;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private String destination;

	protected void close(Processor p, DataSink dsink) {
		// Wait for EndOfStream event.
		waitForFileDone();

		// Cleanup.
		try {
			dsink.close();
		} catch (Exception e) {
		}
		p.removeControllerListener(this);

		recording = false;

		LOGGER.fine("Processing done");
	}

	@Override
	public void controllerUpdate(ControllerEvent event) {
		if (event instanceof ConfigureCompleteEvent || event instanceof RealizeCompleteEvent
				|| event instanceof PrefetchCompleteEvent) {
			synchronized (waitSync) {
				stateTransitionOK = true;
				waitSync.notifyAll();
			}
		} else if (event instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) {
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		} else if (event instanceof EndOfMediaEvent) {
			event.getSourceController().stop();
			event.getSourceController().close();
		}
	}

	/**
	 * Create the DataSink.
	 */
	DataSink createDataSink(Processor p, MediaLocator ml) {
		DataSource ds;

		if ((ds = p.getDataOutput()) == null) {
			LOGGER.warning("The processor does not have an output DataSource");
			return null;
		}

		DataSink dsink;

		try {
			LOGGER.config("Creating the data sink for " + ml);
			dsink = Manager.createDataSink(ds, ml);
			dsink.open();
		} catch (Exception e) {
			LOGGER.warning("Cannot create the DataSink: " + e);
			return null;
		}

		return dsink;
	}

	@Override
	public void dataSinkUpdate(DataSinkEvent event) {
		if (event instanceof EndOfStreamEvent) {
			synchronized (waitFileSync) {
				fileDone = true;
				waitFileSync.notifyAll();
			}
		} else if (event instanceof DataSinkErrorEvent) {
			synchronized (waitFileSync) {
				fileDone = true;
				fileSuccess = false;
				waitFileSync.notifyAll();
			}
		}
	}

	public boolean isRecording() {
		return recording;
	}

	private void record(final PushImageDataSource dataSource, MediaLocator mediaLocator) {
		final Processor p;

		try {
			LOGGER.config("Creating processor");
			p = Manager.createProcessor(dataSource);
		} catch (Exception e) {
			LOGGER.warning("Cannot create a processor from the data source");
			e.printStackTrace();
			System.err.println("Cannot create a processor from the data source.");
			return;
		}

		p.addControllerListener(this);

		// Put the Processor into configured state so we can set
		// some processing options on the processor.
		LOGGER.config("Configuring processor");
		p.configure();
		if (!waitForState(p, Processor.Configured)) {
			LOGGER.warning("Failed to configure the processor.");
			return;
		}

		// Set the output content descriptor to QuickTime.
		p.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.QUICKTIME));

		// Query for the processor for supported formats.
		// Then set it on the processor.
		TrackControl tcs[] = p.getTrackControls();
		Format f[] = tcs[0].getSupportedFormats();
		if (f == null || f.length <= 0) {
			LOGGER.warning("The mux does not support the input format: " + tcs[0].getFormat());
			return;
		}

		tcs[0].setFormat(f[0]);

		LOGGER.finest("Setting the track format to: " + f[0]);

		// We are done with programming the processor. Let's just
		// realize it.
		LOGGER.config("Realizing processor");
		p.realize();
		if (!waitForState(p, Controller.Realized)) {
			LOGGER.warning("Failed to realize the processor.");
			return;
		}

		// Now, we'll need to create a DataSink.
		final DataSink dsink;
		if ((dsink = createDataSink(p, mediaLocator)) == null) {
			LOGGER.warning("Failed to create a DataSink for the given output MediaLocator: " + mediaLocator);
			return;
		}

		dsink.addDataSinkListener(this);
		fileDone = false;
		stopped = false;

		LOGGER.config("Stating processing");

		// OK, we can now start the actual transcoding.
		try {
			p.start();
			dsink.start();
		} catch (IOException e) {
			LOGGER.warning("IO error during processing");
			return;
		}

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (stopped) {
					timer.cancel();

					// closes the data source
					dataSource.close();

					close(p, dsink);
				} else {
					try {
						dataSource.update();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		timer = new Timer();
		timer.schedule(task, 0, 1000);

		recording = true;
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		this.provider = provider;
		provider.registerService(VideoRecorder.class, this);
	}

	public void shutdown() {
		stop();
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void start() {
		if (recording) {
			return;
		}

		record(new GameBoardImageDataSource(provider), new MediaLocator(destination));
	}

	public void stop() {
		if (!recording) {
			return;
		}

		stopped = true;
	}

	/**
	 * Block until file writing is done.
	 */
	boolean waitForFileDone() {
		synchronized (waitFileSync) {
			try {
				while (!fileDone) {
					waitFileSync.wait();
				}
			} catch (Exception e) {
			}
		}
		return fileSuccess;
	}

	/**
	 * Block until the processor has transitioned to the given state. Return
	 * false if the transition failed.
	 */
	boolean waitForState(Processor p, int state) {
		synchronized (waitSync) {
			try {
				while (p.getState() < state && stateTransitionOK) {
					waitSync.wait();
				}
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	}
}
