package org.cen.ui.rtp;

import java.awt.Image;

import javax.media.protocol.PullBufferStream;

public class PullImageSourceStream extends ImageSourceStream implements PullBufferStream {
	public PullImageSourceStream(Image image) {
		super(image);
	}

	public boolean willReadBlock() {
		return false;
	}
}
