package org.cen.robot.device;

import java.util.Map;

import org.cen.robot.IRobotService;

public interface IRobotDevicesHandler extends IRobotService {

    void addDeviceDebugListener(RobotDeviceDebugListener listener);

    void addDeviceListener(RobotDeviceListener listener);

    void notifyDebug(IRobotDevice device, RobotDeviceRequest request, RobotDeviceResult result);

    void notifyListeners(IRobotDevice device, RobotDeviceResult result);

    /**
     * Find a device with his name
     * 
     * @param name
     * @return
     */
    IRobotDevice getDevice(String name);

    /**
     * Get all devices in a map indexed by the name of the device.
     * 
     * @return
     */
    Map<String, IRobotDevice> getDevices();

    /**
     * Get a property of a device.
     * 
     * @param deviceName
     * @param propertyName
     * @return
     */
    Object getProperty(String deviceName, String propertyName);

    DeviceRequestDispatcher getRequestDispatcher();

    DeviceResultDispatcher getResultDispatcher();

    void registerDevice(IRobotDevice device);

    void removeDeviceDebugListener(RobotDeviceDebugListener listener);

    void removeDeviceListener(RobotDeviceListener listener);

    void sendRequest(RobotDeviceRequest request);

    void sendResult(IRobotDevice device, RobotDeviceResult result);

    void setProperty(String deviceName, String propertyName, Object value);

    void unregisterDevice(IRobotDevice device);
}