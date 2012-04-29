package org.cen.ui.rtp;

import java.awt.Image;
import java.io.IOException;

import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;

public class PullImageDataSource extends PullBufferDataSource {
	private PullBufferStream[] streams;

	private PullImageSourceStream imageStream;

	public PullImageDataSource(Image image) {
		super();
		imageStream = new PullImageSourceStream(image);
		streams = new PullBufferStream[] { imageStream };
	}

	@Override
	public void connect() throws IOException {
	}

	@Override
	public void disconnect() {
	}

	@Override
	public String getContentType() {
		return ContentDescriptor.RAW;
	}

	@Override
	public Object getControl(String arg0) {
		return null;
	}

	@Override
	public Object[] getControls() {
		return new Object[0];
	}

	@Override
	public Time getDuration() {
		return DURATION_UNKNOWN;
	}

	@Override
	public PullBufferStream[] getStreams() {
		return streams;
	}

	@Override
	public void start() throws IOException {
	}

	@Override
	public void stop() throws IOException {
	}
}
