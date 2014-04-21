package org.cen.vision.filters;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.OperationDescriptorImpl;

public class TargetFilterDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {
	private static final long serialVersionUID = 1L;

	private static final int numSources = 1;

	private static final Class<?>[] paramClasses = { Double.class, Double.class, Double.class, Double.class };

	private static final Object[] paramDefaults = { 0d, .1d, .9d, 4d };

	private static final String[] paramNames = { "filteredColor", "saturationThreshold", "intensityThreshold", "slope" };

	private static final String[][] resources = { { "GlobalName", "TargetFilter" }, { "LocalName", "TargetFilter" }, { "Vendor", "" }, { "Description", "TargetFilter" }, { "Version", "beta" }, };

	private static final String[] supportedModes = { "rendered" };

	/**
	 * Constructs a TargetFilterDescriptor.
	 */
	public TargetFilterDescriptor() {
		super(resources, supportedModes, numSources, paramNames, paramClasses, paramDefaults, null);
	}

	public RenderedImage create(ParameterBlock pb, RenderingHints rh) {
		return new TargetFilterOpImage((RenderedImage) pb.getSource(0), pb.getDoubleParameter(0), pb.getDoubleParameter(1), pb.getDoubleParameter(2), pb.getDoubleParameter(3));
	}
}
