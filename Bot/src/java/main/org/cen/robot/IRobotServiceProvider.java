package org.cen.robot;

import java.util.Set;

/**
 * Interface of the robot services provider.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IRobotServiceProvider {

    /**
     * Return the service implementation of the specified service interface.
     * 
     * @param <E>
     *            an interface extending IRobotService
     * @param serviceType
     *            the class type of the required service
     * @return the registered implementation for the required service
     */
    <E extends IRobotService> E getService(Class<E> serviceType);

    /**
     * Returns a set of all registered services.
     * 
     * @return a set of all registered services
     */
    Set<IRobotService> getServices();

    /**
     * Registers a service.
     * 
     * @param <E>
     *            an interface extending IRobotService
     * @param serviceType
     *            the class type of the service to register
     * @param service
     *            the service implementation
     */
    <E extends IRobotService> void registerService(Class<E> serviceType, E service);
}
