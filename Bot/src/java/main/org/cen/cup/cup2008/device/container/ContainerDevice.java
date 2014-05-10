package org.cen.cup.cup2008.device.container;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.cup.cup2008.device.container.ContainerRequest.Action;
import org.cen.cup.cup2008.device.container.com.CloseContainerOutData;
import org.cen.cup.cup2008.device.container.com.ContainerDataDecoder;
import org.cen.cup.cup2008.device.container.com.GetObjectCountInData;
import org.cen.cup.cup2008.device.container.com.MoveContainerOutData;
import org.cen.cup.cup2008.device.container.com.ObjectCollectedInData;
import org.cen.cup.cup2008.device.container.com.OpenContainerOutData;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.services.IRobotServiceProvider;

public final class ContainerDevice extends AbstractRobotDevice implements InDataListener {

    public static final String NAME = "container";

    private final ContainerDeviceContext fsm;

    private ContainerRequest request;

    public ContainerDevice() {
        super(NAME);
        fsm = new ContainerDeviceContext(this);
    }

    @Override
    public void debug(String debugAction) {
        ContainerRequest request = null;
        if (debugAction.equals("open")) {
            request = new ContainerRequest(ContainerRequest.Action.OPEN);
        } else if (debugAction.equals("close")) {
            request = new ContainerRequest(ContainerRequest.Action.CLOSE);
        } else if (debugAction.equals("move")) {
            request = new ContainerRequest(ContainerRequest.Action.MOVE, 1);
        }
        if (request != null) {
            handleRequest(request);
        }
    }

    @Override
    public void initialize(IRobotServiceProvider servicesProvider) {
        super.initialize(servicesProvider);
        IComService comService = servicesProvider.getService(IComService.class);
        comService.getDecodingService().registerDecoder(new ContainerDataDecoder());
        comService.addInDataListener(this);
    }

    @Override
    protected void internalHandleRequest(IRobotDeviceRequest request) {
        if (request instanceof ContainerRequest) {
            ContainerRequest r = (ContainerRequest) request;
            Action action = r.getAction();
            switch (action) {
            case CLOSE:
                send(new CloseContainerOutData());
                break;
            case OPEN:
                send(new OpenContainerOutData());
                break;
            case MOVE:
                send(new MoveContainerOutData(r.getData()));
                break;
            case DEPLOY:
                send(new MoveContainerOutData(r.getData()));
                break;
            case UNDEPLOY:
                send(new MoveContainerOutData(r.getData()));
                break;
            }
        }
    }

    public void notifyResult(ContainerResult result) {
        if (result != null) {
            IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
            handler.sendResult(this, result);
        }
    }

    @Override
    public void onInData(InData data) {
        if (data instanceof GetObjectCountInData) {
            fsm.Receive(new GetObjectCountResult(request, ((GetObjectCountInData) data).getCount()));
        }
        if (data instanceof ObjectCollectedInData) {
            fsm.Receive(new ObjectCollectedResult(null));
        }
    }

    private void send(OutData data) {
        IComService comService = servicesProvider.getService(IComService.class);
        comService.writeOutData(data);
    }

    public void sendData(ContainerRequest request) {
    }

    public void unhandled() {
    }
}
