package org.cen.math;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

	@Test
	public void should_convert_decidegree_in_mmRad() {
		double result = MathUtils.deciDegreeToRad(3600d);

		Assert.assertEquals(Math.PI * 2d, result);
	}

	@Test
	public void should_convert_centiRad_in_degree() {
		double result = MathUtils.radToDeciDegree(Math.PI * 2d);

		Assert.assertEquals(3600d, result);
	}

	@Test
	public void should_convert_deciDegree_in_degree() {
		double result = MathUtils.deciDegreeToDegree(10d);

		Assert.assertEquals(1d, result);
	}

	@Test
	public void should_convert_degree_in_deciDegree() {
		double result = MathUtils.degreeToDeciDegree(1d);

		Assert.assertEquals(10d, result);
	}
}
