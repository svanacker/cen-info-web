package org.cen.ui.rtp;

import java.awt.Image;
import java.io.IOException;

import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;

public class PushImageDataSource extends PushBufferDataSource {
	private PushBufferStream[] streams;

	private PushImageSourceStream imageStream;

	protected Image image;

	public PushImageDataSource() {
		super();
	}

	public void close() {
		imageStream.setEndOfStream(true);
		imageStream.update();
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

	public Image getImage() {
		return image;
	}

	@Override
	public PushBufferStream[] getStreams() {
		if (streams == null) {
			imageStream = new PushImageSourceStream(image);
			streams = new PushBufferStream[] { imageStream };
		}
		return streams;
	}

	public void setImage(Image image) {
		this.image = image;
		imageStream.setImage(image);
	}

	@Override
	public void start() throws IOException {
	}

	@Override
	public void stop() throws IOException {
	}

	public void update() {
		imageStream.update();
	}
}
