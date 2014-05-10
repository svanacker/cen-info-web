package org.cen.robot.device.collision;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.geom.Point2D;
import org.cen.robot.attributes.IRobotDimension;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.collision.com.CollisionDataDecoder;
import org.cen.robot.device.collision.com.CollisionReadInData;
import org.cen.robot.device.collision.com.CollisionReadOutData;
import org.cen.robot.device.collision.com.OpponentPositionInData;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class CollisionDetectionDevice extends AbstractRobotDevice implements InDataListener {
    public static final String NAME = "collision";

    private IRobotDeviceRequest request;

    public CollisionDetectionDevice() {
        super(NAME);
    }

    @Override
    public void initialize(IRobotServiceProvider servicesProvider) {
        this.servicesProvider = servicesProvider;
        IComService comService = servicesProvider.getService(IComService.class);
        comService.getDecodingService().registerDecoder(new CollisionDataDecoder());
        comService.addInDataListener(this);
    }

    @Override
    protected void internalHandleRequest(IRobotDeviceRequest request) {
        this.request = request;
        if (request instanceof CollisionReadRequest) {
            send(new CollisionReadOutData());
        }
    }

    private void notifyResult(CollisionResult result) {
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        handler.sendResult(this, result);
    }

    @Override
    public void onInData(InData data) {
        if (data instanceof OpponentPositionInData) {
            OpponentPositionInData d = (OpponentPositionInData) data;
            notifyResult(new OpponentMovedResult(null, d));
        } else if (data instanceof CollisionReadInData) {
            CollisionReadInData d = (CollisionReadInData) data;

            // Updates the current position from wheels informations
            RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
            IRobotDimension dimension = RobotUtils.getRobotAttribute(IRobotDimension.class, servicesProvider);
            position.updateFromPulses(d.getLeftPosition(), d.getRightPosition(), dimension);

            // Obstacle position
            Point2D p = position.getCentralPoint();
            double x = p.getX();
            double y = p.getY();
            double distance = d.getDistance();
            double angle = position.getAlpha();
            x += distance * Math.cos(angle);
            y += distance * Math.sin(angle);

            // Notify the collision
            notifyResult(new CollisionReadResult(request, new Point2D.Double(x, y)));
        }
    }

    private void send(OutData outData) {
        IComService comService = servicesProvider.getService(IComService.class);
        comService.writeOutData(outData);
    }
}
