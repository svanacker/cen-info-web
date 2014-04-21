package org.cen.vision.filters;

import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RIFRegistry;

public class ObjectsConfigurationOpImageTest {
	public static void main(String[] args) {
		registerOperators();
		ParameterBlock pb = new ParameterBlock();
		try {
			pb.addSource(ImageIO.read(new File("c:\\brw.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		RenderedOp c = JAI.create("ObjectsConfigurationFilter", pb);
		System.out.println(c.getProperty("Position1").toString());
		System.out.println(c.getProperty("Position2").toString());
		System.out.println(c.getProperty("Position3").toString());
	}

	private static void registerOperators() {
		String productName = "testjai";
		OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();

		ObjectsConfigurationDescriptor descriptor = new ObjectsConfigurationDescriptor();
		String operationName = "ObjectsConfigurationFilter";
		RenderedImageFactory rif = descriptor;
		or.registerDescriptor(descriptor);
		RIFRegistry.register(or, operationName, productName, rif);
	}
}
