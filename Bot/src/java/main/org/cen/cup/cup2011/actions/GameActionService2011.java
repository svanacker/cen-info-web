package org.cen.cup.cup2011.actions;

import java.util.ArrayList;
import java.util.List;

import org.cen.actions.AbstractGameActionService;
import org.cen.actions.IGameAction;
import org.cen.actions.IGameActionHandler;
import org.cen.cup.cup2011.device.gripper2011.GetKingPresenceRequest2011;
import org.cen.cup.cup2011.device.gripper2011.GripperCloseRequest2011;
import org.cen.cup.cup2011.device.gripper2011.GripperDownRequest2011;
import org.cen.cup.cup2011.device.gripper2011.GripperOpenRequest2011;
import org.cen.cup.cup2011.device.gripper2011.KingPickUpRequest2011;
import org.cen.cup.cup2011.device.gripper2011.PawnDropRequest2011;
import org.cen.cup.cup2011.device.gripper2011.PawnPickUpRequest2011;
import org.cen.geom.Point2D;
import org.cen.math.MathUtils;
import org.cen.navigation.ITrajectoryService;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.services.IRobotServiceProvider;

public class GameActionService2011 extends AbstractGameActionService {
    // private static final int DELAY_PICKUP = 2000;

    // private static final int DELAY_DROP = 2000;

    private static final int DISTANCE_DROP_BACKWARD = 210;

    // fourchette d'angle pour privilégier la marche arrière
    private static final double EPSILON = Math.toRadians(15d);

    public GameActionService2011(IRobotServiceProvider servicesProvider) {
        super(servicesProvider);
    }

    @Override
    protected void handleAction(List<IRobotDeviceRequest> requests, Point2D position, IGameAction action,
            IGameActionHandler handler) {
        if (action instanceof PawnDropAction) {
            // dépose du pion
            requests.add(new PawnDropRequest2011());
            requests.add(new MoveRequest(-DISTANCE_DROP_BACKWARD));
            // fermeture de la pince
            // requests.add(new GripperCloseRequest2011());
        } else if (action instanceof PawnPickUpAction) {
            // insertion de l'ouverture de la pince avant le dernier mouvement
            requests.add(requests.size() - 1, new GripperOpenRequest2011());
            // ajout de la prise du pion
            requests.add(new PawnPickUpRequest2011());
        } else if (action instanceof KingPickUpAction) {
            requests.add(new KingPickUpRequest2011());
        } else if (action instanceof PickerCloseAction) {
            requests.add(new GripperCloseRequest2011());
        } else if (action instanceof PickerOpenAction) {
            requests.add(new GripperOpenRequest2011());
        } else if (action instanceof PawnPickUpAnalyzeAction) {
            // insertion de l'ouverture de la pince avant le dernier mouvement
            requests.add(requests.size() - 1, new GripperOpenRequest2011());
            // ajout de la prise du pion
            requests.add(new PawnPickUpRequest2011());
            requests.add(new GetKingPresenceRequest2011());
        } else if (action instanceof KingDropAction) {
            // dépose du pion
            requests.add(new PawnDropRequest2011());
            requests.add(new MoveRequest(-DISTANCE_DROP_BACKWARD));
            // fermeture de la pince
            requests.add(new GripperDownRequest2011());
            // requests.add(new GripperCloseRequest2011());
        }
    }

    @Override
    protected double handleMove(List<IRobotDeviceRequest> requests, Point2D start, Point2D end, double orientation) {
        List<Point2D> path = new ArrayList<Point2D>();
        path.add(start);
        path.add(end);
        int n = requests.size();
        ITrajectoryService trajectory = servicesProvider.getService(ITrajectoryService.class);
        double endOrientation = trajectory.buildTrajectoryRequests(path, orientation, requests, false);
        // différentiel d'angle
        double d = Math.abs(endOrientation - orientation);
        // utilise la marche arrière lorsque la fourchette d'angle (+/- EPSILON)
        // le permet
        if (MathUtils.isSameValue(d, Math.PI, EPSILON)) {
            while (n < requests.size()) {
                requests.remove(n);
            }
            endOrientation = trajectory.buildTrajectoryRequests(path, orientation, requests, true);
        }
        return endOrientation;
    }
}
