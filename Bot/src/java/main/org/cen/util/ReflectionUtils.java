package org.cen.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ReflectionUtils {
	/**
	 * Returns the class name of the caller at the specified stack depth.
	 * 
	 * @param depth
	 *            the stack depth: 0 for the direct caller, 1 for the caller of
	 *            the caller, and so on...
	 * @return the class name of the caller
	 */
	public static final String getCallerClassName(int depth) {
		return getStack()[depth + 2].getClassName();
	}

	protected static final StackTraceElement[] getStack() {
		try {
			throw new Exception();
		} catch (Exception e) {
			return e.getStackTrace();
		}
	}

	/**
	 * Invokes the method of the object instance annotated with the specified
	 * annotation.
	 * 
	 * @param annotationClass
	 *            the class of the annotation
	 * @param instance
	 *            the object instance
	 * @param parameters
	 *            the parameters with which to invoke the method
	 */
	public static void invoke(Class<? extends Annotation> annotationClass, Object instance, Object[] parameters) {
		Class<?> instanceClass = instance.getClass();
		Method[] methods = instanceClass.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(annotationClass)) {
				method.setAccessible(true);
				try {
					method.invoke(instance, parameters);
				} catch (Exception e) {
					String name = getCallerClassName(1);
					Logger.getLogger(name).throwing(name, method.getName(), e);
					e.printStackTrace();
				}
				return;
			}
		}
	}
}
