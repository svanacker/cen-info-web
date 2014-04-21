package org.cen.com.documentation;

import java.util.List;

/**
 * Class which can add some method signature from a device Data Signature.
 */
public class ComDataDocumentationMethodSignatureBuilder {

    private static final String DEVICE_NAME_AND_HEADER_SEPARATOR = ":";

    private static final String DEVICE_HEADER_AND_METHOD_NAME_SEPARATOR = "/";

    private static final String DEVICE_METHOD_NAME_AND_TYPE_SEPARATOR = ":";

    private static final String ARGUMENTS_SEPARATOR = " | ";

    private static final String ARGUMENTS_NAME_AND_TYPE_SEPARATOR = ";";

    private static final String ARGUMENTS_START_CHAR = "(";
    private static final String ARGUMENTS_STOP_CHAR = ")";

    public void addMethods(List<String> results, DeviceDataSignature signature) {
        DeviceMethodSignature[] methods = signature.methods();
        for (DeviceMethodSignature method : methods) {
            String methodDescriptorAsString = buildMethodSignature(signature, method);
            results.add(methodDescriptorAsString);
        }
    }

    /**
     * Build one line for a method signature (input or output), will all
     * arguments.
     * 
     * @param signature
     *            the signature of the method
     * @param method
     *            the method from which we build the signature
     * @return
     */
    protected String buildMethodSignature(DeviceDataSignature signature, DeviceMethodSignature method) {
        StringBuilder builder = new StringBuilder();

        // deviceName
        String deviceName = signature.deviceName();
        builder.append(deviceName);
        builder.append(DEVICE_NAME_AND_HEADER_SEPARATOR);

        // Header char
        String header = method.header();
        builder.append(header);
        builder.append(DEVICE_HEADER_AND_METHOD_NAME_SEPARATOR);

        // MethodName
        String methodName = method.methodName();
        builder.append(methodName);
        builder.append(DEVICE_METHOD_NAME_AND_TYPE_SEPARATOR);

        // Input/Output
        DeviceMethodType type = method.type();
        String typeShortName = type.getShortName();
        builder.append(typeShortName);
        builder.append(ARGUMENTS_START_CHAR);

        DeviceParameter[] parameters = method.parameters();
        boolean first = true;
        for (DeviceParameter parameter : parameters) {
            // separator
            if (!first) {
                builder.append(ARGUMENTS_SEPARATOR);
            }
            first = false;

            // argument name
            String name = parameter.name();
            builder.append(name);
            builder.append(ARGUMENTS_NAME_AND_TYPE_SEPARATOR);

            // type and length
            int l = parameter.length();
            DeviceParameterType parameterType = parameter.type();
            String parameterTypeShortName = parameterType.getShortName();
            builder.append(parameterTypeShortName);

            if (l == 0) {
                // Variable length parameter
                builder.append('?');
            } else {
                builder.append(l);
                builder.append(ARGUMENTS_NAME_AND_TYPE_SEPARATOR);
            }
            String unit = parameter.unit();
            builder.append(unit);
        }
        builder.append(ARGUMENTS_STOP_CHAR);
        String methodDescriptorAsString = builder.toString();
        return methodDescriptorAsString;
    }
}
