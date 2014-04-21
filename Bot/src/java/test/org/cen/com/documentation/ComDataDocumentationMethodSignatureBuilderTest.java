package org.cen.com.documentation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @see ComDataDocumentationHandler
 */
@DeviceDataSignature(deviceName = "deviceTest", methods = { @DeviceMethodSignature(header = "t", type = DeviceMethodType.OUTPUT, parameters = {
        @DeviceParameter(name = "index", length = 1, type = DeviceParameterType.UNSIGNED, unit = "Volt"),
        @DeviceParameter(name = "param2", length = 2, type = DeviceParameterType.UNSIGNED, unit = "Hertz"),
        @DeviceParameter(name = "speed", length = 2, type = DeviceParameterType.UNSIGNED, unit = "m/s") }) })
public class ComDataDocumentationMethodSignatureBuilderTest {

    @Test
    public void should_return_right_method_signature() {
        ComDataDocumentationMethodSignatureBuilder builder = new ComDataDocumentationMethodSignatureBuilder();

        DeviceDataSignature signature = getClass().getAnnotation(DeviceDataSignature.class);
        List<String> results = new ArrayList<>();
        builder.addMethods(results, signature);

        String expected = "deviceTest:t:o(index;u1;Volt | param2;u2;Hertz | speed;u2;m/s)";
        String actual = results.get(0);

        Assert.assertEquals(expected, actual);
    }
}
