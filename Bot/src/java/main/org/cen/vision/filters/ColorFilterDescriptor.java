package org.cen.vision.filters;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.ImageLayout;
import javax.media.jai.OperationDescriptorImpl;

/**
 * Descripteur du filtre de couleur.
 * 
 * @author Emmanuel ZURMELY
 */
public class ColorFilterDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {
	private static final long serialVersionUID = 1L;

	private static final int numSources = 1;

	private static final Class<?>[] paramClasses = { Double.class, float[].class, FilterInterruptor.class };

	private static final Object[] paramDefaults = { 0d, null, new FilterInterruptor() };

	private static final String[] paramNames = { "angle", "buffer", "interruptor" };

	private static final String[][] resources = { { "GlobalName", "ColorFilter" }, { "LocalName", "ColorFilter" }, { "Vendor", "" }, { "Description", "ColorFilter" }, { "Version", "beta" }, };

	private static final String[] supportedModes = { "rendered" };

	/**
	 * Constructeur.
	 */
	public ColorFilterDescriptor() {
		super(resources, supportedModes, numSources, paramNames, paramClasses, paramDefaults, null);
	}

	public RenderedImage create(ParameterBlock pb, RenderingHints rh) {
		return new ColorFilterOpImage(pb.getSources(), null, new ImageLayout(), pb.getDoubleParameter(0), (float[]) pb.getObjectParameter(1), (FilterInterruptor) pb.getObjectParameter(2));
	}
}
