package org.cen.cup.cup2011.device.vision2011;

import java.util.ArrayList;
import java.util.List;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.cup.cup2011.device.vision2011.com.LookForPawnOutData;
import org.cen.cup.cup2011.device.vision2011.com.PawnPositionInData;
import org.cen.cup.cup2011.device.vision2011.com.Vision2011InDataDecoder;
import org.cen.cup.cup2011.gameboard.configuration.GameboardConfigurationAnalyzer;
import org.cen.cup.cup2011.gameboard.configuration.IGameboardAnalysisListener;
import org.cen.geom.Point2D;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.device.vision.AbstractVisionDevice;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.vision.IVisionService;

public class Vision2011Device extends AbstractVisionDevice implements IGameboardAnalysisListener, InDataListener {

    private static final boolean SIMULATION = false;

    private GameboardConfigurationAnalyzer analyzer;

    private IRobotDeviceRequest request;

    private final Point2D finalPoint = new Point2D.Double(Double.NaN, Double.NaN);

    private void analyze(boolean initial) {
        if (analyzer != null) {
            analyzer.analyzeGameboard(this, initial);
        }
    }

    @Override
    public void finished(List<Point2D> points) {
        if (points != null) {
            for (Point2D point : points) {
                notifyResult(new PawnPositionResult(request, point));
            }
        }
        notifyResult(new PawnPositionResult(request, finalPoint));
    }

    public List<OutData> getOutData(IRobotDeviceRequest request) {
        List<OutData> list = new ArrayList<OutData>();
        if (request instanceof LookForPawnRequest) {
            list.add(new LookForPawnOutData());
        }
        return list;
    }

    @Override
    public void initialize(IRobotServiceProvider servicesProvider) {
        super.initialize(servicesProvider);
        IComService comService = servicesProvider.getService(IComService.class);
        comService.getDecodingService().registerDecoder(new Vision2011InDataDecoder());
        comService.addInDataListener(this);
    }

    @Override
    protected void initializeVision(IVisionService visionService) {
    }

    @Override
    protected void internalHandleRequest(IRobotDeviceRequest request) {
        this.request = request;
        if (SIMULATION) {
            System.out.println("sending " + request);
            List<OutData> data = getOutData(request);
            for (OutData d : data) {
                send(d);
            }
        } else {
            if (request instanceof LookForPawnRequest) {
                LookForPawnRequest r = (LookForPawnRequest) request;
                analyze(r.getInitial());
            } else if (request instanceof Vision2011InitializeRequest) {
                System.out.println("init");
                Vision2011InitializeRequest r = (Vision2011InitializeRequest) request;
                analyzer = r.getAnalyzer();
                analyzer.initializeVision();
            }
        }
    }

    private void notifyResult(RobotDeviceResult result) {
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        handler.sendResult(this, result);
    }

    @Override
    public void onInData(InData data) {
        if (data instanceof PawnPositionInData) {
            PawnPositionInData d = (PawnPositionInData) data;
            notifyResult(new PawnPositionResult(request, d.getPosition()));
        }
    }

    private void send(OutData data) {
        IComService comService = servicesProvider.getService(IComService.class);
        comService.writeOutData(data);
    }
}
