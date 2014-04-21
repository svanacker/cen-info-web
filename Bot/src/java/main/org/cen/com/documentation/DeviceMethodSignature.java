package org.cen.com.documentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describe a remote method called with its name, and all of its parameter.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeviceMethodSignature {
    
    /**
     * The header as first characters to determine which devices is concerned by the call.
     * @return
     */
	String header();

	/**
	 * If data comes from the main board or is sent by the main Board.
	 * @return
	 */
	DeviceMethodType type();

	/**
	 * Parameters array for the device method.
	 * @return
	 */
	DeviceParameter[] parameters();
}
