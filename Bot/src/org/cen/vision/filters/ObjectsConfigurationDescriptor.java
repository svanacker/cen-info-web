package org.cen.vision.filters;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.OperationDescriptorImpl;

public class ObjectsConfigurationDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {
	private static final long serialVersionUID = 1L;

	private static final int numSources = 1;

	private static final Class<?>[] paramClasses = { Rectangle.class, Rectangle.class, Rectangle.class };

	private static final Object[] paramDefaults = { null, null, null };

	private static final String[] paramNames = { "Position1", "Position2", "Position3" };

	private static final String[][] resources = { { "GlobalName", "ObjectsConfigurationFilter" }, { "LocalName", "ObjectsConfigurationFilter" }, { "Vendor", "" }, { "Description", "ObjectsConfigurationFilter" }, { "Version", "beta" }, };

	private static final String[] supportedModes = { "rendered" };

	/**
	 * Constructs a ObjectsConfigurationDescriptor.
	 */
	public ObjectsConfigurationDescriptor() {
		super(resources, supportedModes, numSources, paramNames, paramClasses, paramDefaults, null);
	}

	public RenderedImage create(ParameterBlock pb, RenderingHints rh) {
		return new ObjectsConfigurationOpImage((RenderedImage) pb.getSource(0), (Rectangle) pb.getObjectParameter(0), (Rectangle) pb.getObjectParameter(1), (Rectangle) pb.getObjectParameter(2));
	}
}
