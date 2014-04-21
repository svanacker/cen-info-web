package org.cen.com.documentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeviceParameter {
    
    /**
     * Name of the parameter.
     * @return
     */
	String name();

	/**
	 * Length of the parameter (Ex : 2, 4 ...)
	 * @return
	 */
	int length();

	/**
	 * To know if the value is signed or not !
	 * @return
	 */
	DeviceParameterType type();

	/**
	 * Unit (hint)
	 * @return
	 */
	String unit();
}
