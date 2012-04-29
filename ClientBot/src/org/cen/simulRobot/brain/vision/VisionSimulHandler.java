package org.cen.simulRobot.brain.vision;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cen.cup.cup2011.simulGameboard.elements.SimulPawnElement;
import org.cen.cup.cup2011.simulRobot.VisionArea;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.simulRobot.device.vision.VisionSimulDevice;
import org.cen.simulRobot.device.vision.VisionSimulPositionRequest;
import org.cen.simulRobot.device.vision.VisionSimulReadResult;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.ISimulGameBoard;
import org.cen.ui.gameboard.elements.ARobotAttributeElement;
import org.cen.ui.gameboard.elements.IMovableElement;
import org.cen.ui.gameboard.elements.SimulRobotElement;
import org.cen.ui.gameboard.elements.SimulVisionElement;

public class VisionSimulHandler extends AbstractDeviceHandler {
    private DeviceRequestDispatcher dispatcher;

    private ISimulGameBoard gameboard;

    private List<IMovableElement> pieceElements;

    private ARobotAttributeElement visionAreaElement;

    public VisionSimulHandler(IRobotServiceProvider pServicesProvider) {
        super(pServicesProvider);
        dispatcher = pServicesProvider.getService(IRobotDevicesHandler.class).getRequestDispatcher();
        gameboard = (ISimulGameBoard) pServicesProvider.getService(IGameBoardService.class);
        pieceElements = new ArrayList<IMovableElement>();
        pieceElements.addAll(gameboard.getElements(SimulPawnElement.class));
        SimulRobotElement robotElement = gameboard.getElements(SimulRobotElement.class).get(0);
        VisionArea visionArea = RobotUtils.getRobotAttribute(VisionArea.class, pServicesProvider);
        visionAreaElement = new SimulVisionElement(visionArea, robotElement);
        robotElement.setRobotAttributeElement(visionAreaElement);
    }

    private void checkPawns() {

        Shape visionShape = visionAreaElement.getAbsoluteBounds();
        // visualise les element dans le champ de vision
        Iterator<IMovableElement> iterator = pieceElements.iterator();
        while (iterator.hasNext()) {
            IMovableElement element = iterator.next();

            if (element.isPickable() && visionShape.intersects(element.getAbsoluteBounds().getBounds())) {
                dispatcher.sendRequest(new VisionSimulPositionRequest((int) element.getPosition().getX(), (int) element
                        .getPosition().getY()));
            }
        }
        dispatcher.sendRequest(new VisionSimulPositionRequest(-1, -1));
    }

    @Override
    public String getDeviceName() {
        return VisionSimulDevice.NAME;
    }

    @Override
    public void handleResult(RobotDeviceResult result) {
        if (result instanceof VisionSimulReadResult) {
            checkPawns();
        }
    }

    @Override
    public void shutdown() {
    }
}
