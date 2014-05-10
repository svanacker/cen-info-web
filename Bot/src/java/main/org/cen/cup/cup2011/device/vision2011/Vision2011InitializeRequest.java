package org.cen.cup.cup2011.device.vision2011;

import org.cen.cup.cup2011.gameboard.configuration.GameboardConfigurationAnalyzer;
import org.cen.robot.device.request.impl.RobotDeviceRequest;

public class Vision2011InitializeRequest extends RobotDeviceRequest {

    private final GameboardConfigurationAnalyzer analyzer;

    public Vision2011InitializeRequest(GameboardConfigurationAnalyzer analyzer) {
        super(Vision2011Device.NAME);
        this.analyzer = analyzer;
    }

    public GameboardConfigurationAnalyzer getAnalyzer() {
        return analyzer;
    }
}
