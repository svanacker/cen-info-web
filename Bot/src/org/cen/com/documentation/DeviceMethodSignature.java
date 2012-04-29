package org.cen.com.documentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeviceMethodSignature {
	public String header();

	public DeviceMethodType type();

	public DeviceParameter[] parameters();
}
