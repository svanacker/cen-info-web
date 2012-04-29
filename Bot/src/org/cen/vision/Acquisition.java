package org.cen.vision;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Logger;

import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.control.FormatControl;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;

import org.cen.logging.LoggingUtils;

/**
 * Classe d'acquisition des images.
 * 
 * @author Emmanuel ZURMELY
 */
public class Acquisition {
	private final static Logger LOGGER = LoggingUtils.getClassLogger();

	private boolean active = false;

	private FrameGrabbingControl fgc;

	private Player player;

	private String mediaURI = "vfw://0";

	/**
	 * Constructor.
	 * 
	 * @throws NoPlayerException
	 * @throws CannotRealizeException
	 * @throws IOException
	 */
	public Acquisition() {
		super();
	}

	/**
	 * Ferme le périphérique d'acquisition vidéo.
	 */
	public void close() {
		if (!active) {
			return;
		}
		active = false;
		player.close();
		player.deallocate();
		player = null;
		fgc = null;
	}

	/**
	 * Returns the formats supported by the player.
	 * 
	 * @return an array of the supported formats
	 */
	public Format[] getAvailableFormats() {
		try {
			FormatControl fc = (FormatControl) player.getControl("javax.media.control.FormatControl");
			return fc.getSupportedFormats();
		} catch (Exception ex) {
			LOGGER.warning("Can not getAvailableFormats() : please check that your webcam is on");
		}
		return new Format[] {};
	}

	/**
	 * Renvoie une image à partir de la video courante
	 * 
	 * @return une image à partir de la video courante
	 */
	public Image getImage() {
		if (fgc != null) {
			Buffer buf = fgc.grabFrame();
			Image img = (new BufferToImage((VideoFormat) buf.getFormat())).createImage(buf);
			return img;
		}
		return null;
	}

	public String getMediaURI() {
		return mediaURI;
	}

	public boolean isActive() {
		return active;
	}

	public void open() throws NoPlayerException, CannotRealizeException, IOException {
		if (active) {
			return;
		}
		player = Manager.createRealizedPlayer(new MediaLocator(mediaURI));
		player.start();
		fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
		active = true;
	}

	public void selectFormat(Format format) {
		player.stop();
		FormatControl fc = (FormatControl) player.getControl("javax.media.control.FormatControl");
		fc.setFormat(format);
		player.start();
	}

	public void setMediaURI(String mediaURI) {
		this.mediaURI = mediaURI;
	}

	/**
	 * Waits for the initialization of this acquisition object to finish and to
	 * acquire a first image.
	 */
	public void waitForInput() {
		while (getImage() == null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
