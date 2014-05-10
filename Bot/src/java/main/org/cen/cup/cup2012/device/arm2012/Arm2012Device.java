package org.cen.cup.cup2012.device.arm2012;

import java.util.ArrayList;
import java.util.List;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.com.out.OutDataSender;
import org.cen.cup.cup2009.device.com.SleepOutData;
import org.cen.cup.cup2012.device.arm2012.com.Arm2012DownOutData;
import org.cen.cup.cup2012.device.arm2012.com.Arm2012UpOutData;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.services.IRobotServiceProvider;

@OutDataSender(classes = { Arm2012UpOutData.class, Arm2012DownOutData.class })
public class Arm2012Device extends AbstractRobotDevice implements InDataListener {

    public static final String NAME = "arm2012";

    public Arm2012Device() {
        super(NAME);
    }

    @Override
    public void debug(String debugAction) {

    }

    public List<OutData> getOutData(IRobotDeviceRequest request) {
        List<OutData> list = new ArrayList<OutData>();
        if (request instanceof ArmRequest2012) {
            ArmRequest2012 armRequest = (ArmRequest2012) request;
            ArmType2012 type = armRequest.getType();
            if (request instanceof ArmUpRequest2012) {
                list.add(new Arm2012UpOutData(type));
            } else if (request instanceof ArmDownRequest2012) {
                list.add(new Arm2012DownOutData(type));
            }
        }
        return list;
    }

    @Override
    public void initialize(IRobotServiceProvider servicesProvider) {
        super.initialize(servicesProvider);
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

    @Override
    public void onInData(InData data) {

    }

    private void send(OutData data) {
        System.out.println("sending data " + data.toString());
        IComService comService = servicesProvider.getService(IComService.class);
        comService.writeOutData(data);
    }
}
