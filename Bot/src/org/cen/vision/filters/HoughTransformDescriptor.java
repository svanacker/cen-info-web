package org.cen.vision.filters;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.OperationDescriptorImpl;

public class HoughTransformDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {
	private static final long serialVersionUID = 1L;

	private static final int numSources = 1;

	private static final Class<?>[] paramClasses = { Double.class, Integer.class };

	private static final Object[] paramDefaults = { 0.5d, 6 };

	private static final String[] paramNames = { "threshold", "radius" };

	private static final String[][] resources = { { "GlobalName", "HoughTransform" }, { "LocalName", "HoughTransform" }, { "Vendor", "" }, { "Description", "HoughTransform" }, { "Version", "beta" }, };

	private static final String[] supportedModes = { "rendered" };

	/**
	 * Constructeur.
	 */
	public HoughTransformDescriptor() {
		super(resources, supportedModes, numSources, paramNames, paramClasses, paramDefaults, null);
	}

	public RenderedImage create(ParameterBlock pb, RenderingHints rh) {
		return new HoughTransformOpImage((RenderedImage) pb.getSource(0), pb.getDoubleParameter(0), pb.getIntParameter(1));
	}
}
