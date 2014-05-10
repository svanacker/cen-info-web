package org.cen.robot.device.console;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.cen.robot.IRobot;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class ConsoleService implements IRobotDeviceConsoleService {
    protected List<IRobotDeviceConsole> consoles;

    private IRobotServiceProvider provider;

    private void buildConsoles() {
        consoles = new ArrayList<IRobotDeviceConsole>();
        IRobot robot = RobotUtils.getRobot(provider);
        List<IRobotDevice> devices = robot.getDevices();
        for (IRobotDevice device : devices) {
            Class<? extends IRobotDevice> deviceClass = device.getClass();
            String packageName = deviceClass.getPackage().getName() + ".console";
            String className = deviceClass.getSimpleName() + "Console";
            try {
                Class<?> consoleClass = Class.forName(packageName + "." + className);
                Constructor<?> c = consoleClass.getConstructor(IRobotDevice.class);
                IRobotDeviceConsole console = (IRobotDeviceConsole) c.newInstance(device);
                consoles.add(console);
            } catch (ClassNotFoundException ex) {
                System.out.println("INFO : Pas de console trouvée pour le périphérique : " + device.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<IRobotDeviceConsole> getConsoles() {
        if (consoles == null) {
            buildConsoles();
        }
        return consoles;
    }

    @Override
    public void setServicesProvider(IRobotServiceProvider provider) {
        this.provider = provider;
        provider.registerService(IRobotDeviceConsoleService.class, this);
    }
}
