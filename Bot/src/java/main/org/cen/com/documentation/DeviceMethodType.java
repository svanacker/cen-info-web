package org.cen.com.documentation;

/**
 * The device method type from the main board side.
 * 
 * @author Emmanuel ZURMELY
 */
public enum DeviceMethodType {

    /**
     * Input method, data coming to the main board.
     */
    INPUT("i"),

    /**
     * Output method, data going out of the main board.
     */
    OUTPUT("o");

    private String shortName;

    private DeviceMethodType(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
}
