package org.cen.cup.cup2011.device.gripper2011;

import java.util.ArrayList;
import java.util.List;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.cup.cup2009.device.com.SleepOutData;
import org.cen.cup.cup2011.device.gripper2011.com.GetKingPresenceInData;
import org.cen.cup.cup2011.device.gripper2011.com.GetKingPresenceOutData;
import org.cen.cup.cup2011.device.gripper2011.com.GripperCloseOutData;
import org.cen.cup.cup2011.device.gripper2011.com.GripperMoveDownOutData;
import org.cen.cup.cup2011.device.gripper2011.com.GripperMoveUpOutData;
import org.cen.cup.cup2011.device.gripper2011.com.GripperOpenOutData;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.services.IRobotServiceProvider;

public class Gripper2011Device extends AbstractRobotDevice implements InDataListener {
    private static final int PAWN_PRESENCE_THRESHOLD = 1536;

    public static final String NAME = "gripper2011";

    private static final int DELAY_CLOSE = 1000;

    private static final int DELAY_OPEN = 1000;

    private static final int DELAY_MOVE = 1000;

    public Gripper2011Device() {
        super(NAME);
    }

    @Override
    public void debug(String debugAction) {

    }

    public List<OutData> getOutData(IRobotDeviceRequest request) {
        List<OutData> list = new ArrayList<OutData>();
        if (request instanceof PawnPickUpRequest2011 || request instanceof GripperCloseRequest2011) {
            list.add(new GripperCloseOutData());
            list.add(new SleepOutData(DELAY_CLOSE));
        } else if (request instanceof PawnDropRequest2011 || request instanceof GripperOpenRequest2011) {
            list.add(new GripperOpenOutData());
            list.add(new SleepOutData(DELAY_OPEN));
        } else if (request instanceof KingPickUpRequest2011) {
            list.add(new GripperCloseOutData());
            list.add(new SleepOutData(DELAY_CLOSE));
            list.add(new GripperMoveUpOutData());
            list.add(new SleepOutData(DELAY_MOVE));
        } else if (request instanceof GripperDownRequest2011) {
            list.add(new GripperMoveDownOutData());
        } else if (request instanceof GripperUpRequest2011) {
            list.add(new GripperMoveUpOutData());
            list.add(new SleepOutData(DELAY_MOVE));
        } else if (request instanceof GripperOpenNoDelayRequest2011) {
            list.add(new GripperOpenOutData());
        } else if (request instanceof GripperCloseNoDelayRequest2011) {
            list.add(new GripperCloseOutData());
        } else if (request instanceof GetKingPresenceRequest2011) {
            list.add(new GetKingPresenceOutData());
        }
        return list;
    }

    @Override
    public void initialize(IRobotServiceProvider servicesProvider) {
        super.initialize(servicesProvider);
        IComService comService = servicesProvider.getService(IComService.class);
        // comService.getDecodingService().registerDecoder(new
        // Gripper2011DataDecoder());
        comService.addInDataListener(this);
    }

    @Override
    protected void internalHandleRequest(IRobotDeviceRequest request) {
        // System.out.println("sending " + request);
        List<OutData> data = getOutData(request);
        for (OutData d : data) {
            if (d instanceof SleepOutData) {
                ((SleepOutData) d).sleep();
            } else {
                send(d);
            }
        }
    }

    private void notifyResult(RobotDeviceResult result) {
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        handler.sendResult(this, result);
    }

    @Override
    public void onInData(InData data) {
        if (data instanceof GetKingPresenceInData) {
            GetKingPresenceInData d = (GetKingPresenceInData) data;
            notifyResult(new GetKingPresenceResult2011(null, d.getValue() > PAWN_PRESENCE_THRESHOLD));
        }
    }

    private void send(OutData data) {
        System.out.println("sending data " + data.toString());
        IComService comService = servicesProvider.getService(IComService.class);
        comService.writeOutData(data);
    }
}
