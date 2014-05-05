package org.cen.simulRobot;

import java.util.Properties;

import org.cen.robot.IRobotAttribute;
import org.cen.utils.PropertiesUtils;

public class RobotSwitches implements IRobotAttribute {

    private static final String SWITCHES = "switches";

    protected int switches;

    public RobotSwitches(Properties properties) {
        switches = 0;
        int switche;
        for (int i = 1; i < 9; i++) {
            switche = (int) PropertiesUtils.getDouble(properties, SWITCHES + "." + i);
            switche = switche << (i - 1);
            switches += switche;
        }
    }

    public int getSwitches() {
        return switches;
    }

    public void setInterrupteurs(int interrupteurs) {
        this.switches = interrupteurs;
    }

}
