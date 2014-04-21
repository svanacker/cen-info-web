package org.cen.com.documentation;

/**
 * The parameter type of a device method.
 * 
 * @author Emmanuel ZURMELY
 */
public enum DeviceParameterType {

    /**
     * Signed value.
     */
    SIGNED("s"),

    /**
     * Unsigned value.
     */
    UNSIGNED("u"),

    /**
     * Unspecified value type.
     */
    UNSPECIFIED("-");

    private String shortName;

    private DeviceParameterType(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
}
