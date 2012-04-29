package org.cen.vision.filters;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.ImageLayout;
import javax.media.jai.OperationDescriptorImpl;

public class GrowingRegionDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {
	private static final long serialVersionUID = 1L;

	private static final int numSources = 1;

	private static final Class<?>[] paramClasses = { Double.class, Integer.class, Integer.class, FilterInterruptor.class };

	private static final Object[] paramDefaults = { 750d, 1, 1, new FilterInterruptor() };

	private static final String[] paramNames = { "threshold", "stepX", "stepY", "interruptor" };

	private static final String[][] resources = { { "GlobalName", "GrowingRegionFilter" }, { "LocalName", "GrowingRegionFilter" }, { "Vendor", "" }, { "Description", "GrowingRegionFilter" }, { "Version", "beta" }, };

	private static final String[] supportedModes = { "rendered" };

	/**
	 * Constructs a GrowingRegionDescriptor.
	 */
	public GrowingRegionDescriptor() {
		super(resources, supportedModes, numSources, paramNames, paramClasses, paramDefaults, null);
	}

	public RenderedImage create(ParameterBlock pb, RenderingHints rh) {
		return new GrowingRegionOpImage(pb.getSources(), null, new ImageLayout(), pb.getDoubleParameter(0), pb.getIntParameter(1), pb.getIntParameter(2), (FilterInterruptor) pb.getObjectParameter(3));
	}
}
