package org.cen.vision.coordinates;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.vecmath.Vector3d;

import junit.framework.Assert;

import org.cen.math.Size2D;
import org.junit.Test;

public class CoordinatesUnitTest {

	/**
	 * We test that gameBoardToScreen(screenToGameBoard(x, y)) = (x, y)
	 * and that screenToGameBoard(gameBoardToScreen(x, y)) = (x, y)
	 */
	@Test
	public void testGameBoardToScreen1() {
		Coordinates coordinates = new Coordinates();
		
		coordinates.setImageDimension(new Dimension(320, 240));
		coordinates.setInclination(Math.toRadians(40d));
		coordinates.setOrigin(new Vector3d(-200, 100, 400));
		coordinates.setVisionAngles(new Size2D(Math.toRadians(45), Math.toRadians(45 * 0.75)));
		
		double x = Math.random() * 320d;
		double y = Math.random() * 240d;
		
		Point2D p1 = coordinates.gameBoardToScreen(x, y);
		Point2D p2 = coordinates.screenToGameBoard((int) p1.getX(), (int) p1.getY());
		
		System.out.println("x:" + x);
		System.out.println("y:" + y);
		System.out.println("resultX:" + p2.getX());
		System.out.println("resultY:" + p2.getY());
		
		final int TOLERATED_ERROR = 2;
		Assert.assertTrue(Math.abs(x - p2.getX()) < TOLERATED_ERROR);
		Assert.assertTrue(Math.abs(y - p2.getY()) < TOLERATED_ERROR);
	}
}
