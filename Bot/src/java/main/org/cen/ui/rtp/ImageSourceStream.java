package org.cen.ui.rtp;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;

import org.apache.commons.io.output.ByteArrayOutputStream;

public abstract class ImageSourceStream {
	private VideoFormat format;

	private Image image;

	private int frameRate = 1;

	private boolean eof = false;

	public ImageSourceStream(Image image) {
		super();
		this.image = image;
		int h = image.getHeight(null);
		int w = image.getWidth(null);
		System.out.println("Image size: " + w + " x " + h);
		format = new VideoFormat(VideoFormat.JPEG, new Dimension(w, h), Format.NOT_SPECIFIED, Format.byteArray, frameRate);
	}

	public boolean endOfStream() {
		System.out.println("endOfStream");
		return eof;
	}

	public ContentDescriptor getContentDescriptor() {
		System.out.println("getContentDescriptor");
		return new ContentDescriptor(ContentDescriptor.RAW);
	}

	public long getContentLength() {
		System.out.println("getContentLength");
		return 0;
	}

	public Object getControl(String arg0) {
		System.out.println("getControl");
		return null;
	}

	public Object[] getControls() {
		System.out.println("getControls");
		return new Object[0];
	}

	public Format getFormat() {
		System.out.println("getFormat");
		return format;
	}

	public void read(Buffer buf) throws IOException {
		System.out.println("read");

		if (eof) {
			buf.setEOM(true);
			buf.setOffset(0);
			buf.setLength(0);
			return;
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int h = image.getHeight(null);
		int w = image.getWidth(null);
		BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		im.getGraphics().drawImage(image, 0, 0, null);
		ImageIO.write(im, "jpg", os);
		os.close();

		// Buffer b = ImageToBuffer.createBuffer(image, frameRate);
		// arg0.copy(b);

		byte[] data = os.toByteArray();

		buf.setOffset(0);
		buf.setLength(data.length);
		buf.setFlags(buf.getFlags() | Buffer.FLAG_KEY_FRAME);

		buf.setFormat(format);
		buf.setData(data);

		System.out.println("Length: " + data.length);
	}

	public void read2(Buffer buf) throws IOException {
		System.out.println("read");
		// Open a random access file for the next image.
		RandomAccessFile raFile;
		raFile = new RandomAccessFile("d:\\brw.jpg", "r");

		byte data[] = null;

		// Check the input buffer type & size.

		if (buf.getData() instanceof byte[])
			data = (byte[]) buf.getData();

		// Check to see the given buffer is big enough for the frame.
		if (data == null || data.length < raFile.length()) {
			data = new byte[(int) raFile.length()];
			buf.setData(data);
		}

		// Read the entire JPEG image from the file.
		raFile.readFully(data, 0, (int) raFile.length());

		System.err.println("    read " + raFile.length() + " bytes.");

		buf.setOffset(0);
		buf.setLength((int) raFile.length());
		buf.setFormat(format);
		buf.setFlags(buf.getFlags() | Buffer.FLAG_KEY_FRAME);

		// Close the random access file.
		raFile.close();

	}

	public void setEndOfStream(boolean b) {
		eof = b;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
