package org.cen.robot;

/**
 * Interface used for initializing services once all services have been
 * registered.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IRobotServiceInitializable {
	/**
	 * Method called by the services provider once all services have been
	 * initialized and registered. This method can be used by a service to
	 * perform an initialization that depends on the other services available
	 * through the services provider.
	 */
	public void afterRegister();
}
