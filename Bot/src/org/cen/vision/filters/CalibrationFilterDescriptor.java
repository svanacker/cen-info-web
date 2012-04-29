package org.cen.vision.filters;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.OperationDescriptorImpl;

public class CalibrationFilterDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {
	private static final long serialVersionUID = 1L;
	
	private static final int numSources = 1;

	private static final Class<?>[] paramClasses = { Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Double.class };

	private static final Object[] paramDefaults = { 16, 1, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, .1 };

	private static final String[] paramNames = { "hBands", "vBands", "ymin", "ymax", "xmin", "xmax", "saturationThreshold" };

	private static final String[][] resources = { { "GlobalName", "CalibrationFilter" }, { "LocalName", "CalibrationFilter" }, { "Vendor", "" }, { "Description", "CalibrationFilter" }, { "Version", "beta" }, };

	private static final String[] supportedModes = { "rendered" };

	/**
	 * Constructs a CalibrationFilterDescriptor.
	 */
	public CalibrationFilterDescriptor() {
		super(resources, supportedModes, numSources, paramNames, paramClasses, paramDefaults, null);
	}

	public RenderedImage create(ParameterBlock pb, RenderingHints rh) {
		return new CalibrationFilterOpImage((RenderedImage) pb.getSource(0), pb.getIntParameter(0), pb.getIntParameter(1), pb.getIntParameter(2), pb.getIntParameter(3), pb.getIntParameter(4), pb.getIntParameter(5), pb.getDoubleParameter(6));
	}
}
