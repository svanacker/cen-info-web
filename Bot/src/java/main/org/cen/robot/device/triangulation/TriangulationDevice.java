package org.cen.robot.device.triangulation;

import java.util.ArrayList;
import java.util.List;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.device.triangulation.TriangulationResult.TriangulationResultStatus;
import org.cen.robot.device.triangulation.com.ReadTriangulationInData;
import org.cen.robot.device.triangulation.com.ReadTriangulationOutData;
import org.cen.robot.device.triangulation.com.TriangulationInDataDecoder;
import org.cen.robot.services.IRobotServiceProvider;

/**
 * Object reprenting the triangulation device used for determining the absolute
 * position of the robot on the game board.
 * 
 * @author Emmanuel ZURMELY
 */
public class TriangulationDevice extends AbstractRobotDevice implements InDataListener {
    public final static String NAME = "triangulation";

    private final List<Double> dataList = new ArrayList<Double>();

    private final TriangulationDeviceContext fsm;

    private IRobotServiceProvider provider;

    private TriangulationRequest request;

    /**
     * Constructor.
     */
    public TriangulationDevice() {
        super(NAME);
        IComService comService = provider.getService(IComService.class);
        comService.getDecodingService().registerDecoder(new TriangulationInDataDecoder());
        comService.addInDataListener(this);
        fsm = new TriangulationDeviceContext(this);
    }

    public void clearHistory() {
        dataList.clear();
    }

    private double[] extractAngles() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasEnoughData() {
        return dataList.size() > 10;
    }

    @Override
    public void initialize(IRobotServiceProvider servicesProvider) {
        provider = servicesProvider;
    }

    @Override
    protected void internalHandleRequest(IRobotDeviceRequest request) {
        if (request instanceof ReadPositionRequest)
            fsm.Read((TriangulationRequest) request);
    }

    @Override
    public void onInData(InData data) {
        if (data instanceof ReadTriangulationInData) {
            ReadTriangulationInData d = (ReadTriangulationInData) data;
            fsm.DataReceived(new TriangulationData(d));
        }
    }

    /**
     * Sends the data over the serial port.
     */
    public void send() {
        IComService comService = provider.getService(IComService.class);
        comService.writeOutData(new ReadTriangulationOutData());
    }

    private void setPosition(double[] angles) {
        // TODO Auto-generated method stub
    }

    /**
     * Sets the currently handled request.
     * 
     * @param request
     *            the request currently handled
     */
    public void setRequest(TriangulationRequest request) {
        this.request = request;
    }

    /**
     * Stores the data received from the device.
     * 
     * @param data
     *            the data sent by the device
     */
    public void storeData(TriangulationData data) {
        for (double angle : data.getAngles(servicesProvider))
            dataList.add(angle);
    }

    public void unhandled() {
    }

    public void updatePosition() {
        double[] angles = extractAngles();
        if (angles != null)
            setPosition(angles);
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        handler.sendResult(this, new TriangulationResult(request, (angles == null) ? TriangulationResultStatus.FAILED
                : TriangulationResultStatus.SUCCEEDED));
    }
}
