package org.cen.com.out;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to reference the list of OutData which must be known in the system.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OutDataSender {

    /**
     * List of "OutData" classes.
     * 
     * @return
     */
    public Class<? extends OutData>[] classes();
}
