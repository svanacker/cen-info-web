package org.cen.com.documentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeviceDataSignature {

	/**
	 * The name of the device.
	 */
	public String deviceName();

	/**
	 * The method to do something on devices.
	 */
	public DeviceMethodSignature[] methods();
}
