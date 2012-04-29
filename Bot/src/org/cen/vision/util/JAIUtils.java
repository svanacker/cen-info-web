package org.cen.vision.util;

import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.registry.RIFRegistry;

import org.cen.vision.filters.CalibrationFilterDescriptor;
import org.cen.vision.filters.ColorFilterDescriptor;
import org.cen.vision.filters.GrowingRegionDescriptor;
import org.cen.vision.filters.TargetFilterDescriptor;

public class JAIUtils {
	public static void registerOperators() {
		String productName = "testjai";
		OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();

		ColorFilterDescriptor colorFilterDescriptor = new ColorFilterDescriptor();
		String operationName = "ColorFilter";
		RenderedImageFactory rif = colorFilterDescriptor;
		or.registerDescriptor(colorFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);

		TargetFilterDescriptor targetFilterDescriptor = new TargetFilterDescriptor();
		operationName = "TargetFilter";
		rif = targetFilterDescriptor;
		or.registerDescriptor(targetFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);

		CalibrationFilterDescriptor calibrationFilterDescriptor = new CalibrationFilterDescriptor();
		operationName = "CalibrationFilter";
		rif = calibrationFilterDescriptor;
		or.registerDescriptor(calibrationFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);

		GrowingRegionDescriptor growingRegionFilterDescriptor = new GrowingRegionDescriptor();
		operationName = "GrowingRegionFilter";
		rif = growingRegionFilterDescriptor;
		or.registerDescriptor(growingRegionFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);
	}
}
