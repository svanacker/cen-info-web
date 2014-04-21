package org.cen.ui.rtp;

import java.awt.Image;

import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.PushBufferStream;

public class PushImageSourceStream extends ImageSourceStream implements PushBufferStream {
	private BufferTransferHandler transferHandler;

	public PushImageSourceStream(Image image) {
		super(image);
	}

	@Override
	public void setTransferHandler(BufferTransferHandler handler) {
		System.out.println("setTransferHandler");
		transferHandler = handler;
	}

	public void update() {
		if (transferHandler != null) {
			transferHandler.transferData((PushBufferStream) this);
		}
	}
}
