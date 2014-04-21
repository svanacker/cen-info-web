package org.cen.robot.device.lcd;

import java.util.ArrayList;
import java.util.List;

import org.cen.com.out.OutData;
import org.cen.robot.device.lcd.com.LcdPrintOutData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @see LcdDevice
 */
public class LcdDeviceTest {

	@Test
	public void should_handle_shortText() {
		LcdDevice device = new LcdDevice();

		LcdWriteRequest request = new LcdWriteRequest("AB");
		List<OutData> outDataList = device.getOutData(request);

		OutData outData = outDataList.get(0);

		Assert.assertEquals("L0241420000", outData.getMessage());
	}

	@Test
	public void should_handle_shortText2() {
		LcdDevice device = new LcdDevice();

		LcdWriteRequest request = new LcdWriteRequest("ABCD");
		List<OutData> outDataList = device.getOutData(request);

		OutData outData = outDataList.get(0);

		Assert.assertEquals("L0441424344", outData.getMessage());
	}

	@Test
	public void should_handle_hello_world() {
		LcdDevice device = new LcdDevice();

		LcdWriteRequest request = new LcdWriteRequest("Hello World!");
		List<OutData> outDataList = device.getOutData(request);

		List<OutData> expectedDataList = new ArrayList<OutData>();

		expectedDataList.add(new LcdPrintOutData("Hell"));
		expectedDataList.add(new LcdPrintOutData("o Wo"));
		expectedDataList.add(new LcdPrintOutData("rd!"));

		for (int i = 0; i < 2; i++) {
			OutData outData = outDataList.get(i);
			OutData expectedData = expectedDataList.get(i);
			Assert.assertEquals(expectedData, outData);
		}
	}

	@Test
	public void should_handle_cls() {
		LcdDevice device = new LcdDevice();

		String clsString = String.valueOf(LcdPrintOutData.CLS);
		LcdWriteRequest request = new LcdWriteRequest(clsString);
		List<OutData> outDataList = device.getOutData(request);

		List<OutData> expectedDataList = new ArrayList<OutData>();

		expectedDataList.add(new LcdPrintOutData(clsString));

		OutData outData = outDataList.get(0);
		OutData expectedData = expectedDataList.get(0);
		Assert.assertEquals(expectedData, outData);
	}
}
