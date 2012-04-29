package org.cen.simulRobot.brain.gripper;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;

import org.cen.cup.cup2011.simulGameboard.SimulGameBoard2011;
import org.cen.cup.cup2011.simulGameboard.elements.SimulPawnElement;
import org.cen.cup.cup2011.simulRobot.GripperArea;
import org.cen.navigation.Location;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.simulRobot.device.gripper.DropSimulResult;
import org.cen.simulRobot.device.gripper.GripperSimulDevice;
import org.cen.simulRobot.device.gripper.TakeSimulResult;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.elements.ARobotAttributeElement;
import org.cen.ui.gameboard.elements.IMovableElement;
import org.cen.ui.gameboard.elements.SimulGripperElement;
import org.cen.ui.gameboard.elements.SimulRobotElement;

/**
 * Handle the actions of the gripper
 * 
 * @author Omar BENOUAMER
 */
public class GripperSimulHandler extends AbstractDeviceHandler {

    protected class TakenElementHandler extends Thread {

        public TakenElementHandler() {
        }

        private void drop() {
            drop = true;
        }

        @Override
        public void run() {
            while (!drop) {
                takenElement.setPosition(gripperAreaElement.getPosition());
                takenElement.setOrientation(gripperAreaElement.getOrientation());
            }
            takenElement.setPickable(true);
            takenElement = null;
        }
    }

    public static final String NAME = GripperSimulDevice.NAME;

    public boolean drop;

    private SimulGameBoard2011 gameboard;

    private ARobotAttributeElement gripperAreaElement;

    private ArrayList<IMovableElement> pieceElements;

    private IMovableElement takenElement;

    private TakenElementHandler takenElementHandler;

    /**
     * Constructor.
     * 
     * @param servicesProvider the services provider
     */
    public GripperSimulHandler(IRobotServiceProvider pServicesProvider) {
        super(pServicesProvider);

        gameboard = (SimulGameBoard2011) pServicesProvider.getService(IGameBoardService.class);
        pieceElements = new ArrayList<IMovableElement>();
        pieceElements.addAll(gameboard.getElements(SimulPawnElement.class));
        SimulRobotElement robotElement = gameboard.getElements(SimulRobotElement.class).get(0);
        GripperArea gripperArea = RobotUtils.getRobotAttribute(GripperArea.class, pServicesProvider);
        gripperAreaElement = new SimulGripperElement(gripperArea, robotElement);
        robotElement.setRobotAttributeElement(gripperAreaElement);
        drop = true;
    }

    private void dropTakenElement() {
        takenElementHandler.drop();
    }

    @Override
    public String getDeviceName() {
        return GripperSimulDevice.NAME;
    }

    @Override
    public void handleResult(RobotDeviceResult result) {
        if (result instanceof TakeSimulResult) {
            take();
        }
        if (result instanceof DropSimulResult) {
            dropTakenElement();
        }
    }

    @Override
    public void shutdown() {
        drop = true;
        super.shutdown();
    }

    private void take() {
        double currentDistance = Double.MAX_VALUE;
        Shape gripperShape = gripperAreaElement.getAbsoluteBounds();
        Location centerGripper =
                new Location("gripper", (int) gripperShape.getBounds().getCenterX(), (int) gripperShape.getBounds()
                        .getCenterY());

        // TODO effectuer la recherche de l'élément étant à proximité dans le ISimulGameBoard
        Iterator<IMovableElement> iterator = pieceElements.iterator();
        while (iterator.hasNext()) {
            IMovableElement element = iterator.next();

            if (gripperShape.intersects(element.getAbsoluteBounds().getBounds())) {
                Location centerElement =
                        new Location("element", (int) element.getAbsoluteBounds().getBounds().getCenterX(),
                                (int) element.getAbsoluteBounds().getBounds().getCenterY());
                int testDistance = centerElement.getDistance(centerGripper);
                if (testDistance < currentDistance) {
                    currentDistance = testDistance;
                    takenElement = element;
                }
            }
        }

        if (drop) {
            drop = false;
            takenElement.setPickable(false);
            takenElementHandler = new TakenElementHandler();
            takenElementHandler.start();
        }
    }
}
