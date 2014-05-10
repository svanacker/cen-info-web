package org.cen.cup.cup2012.robot.match;

import static org.cen.cup.cup2012.robot.match.ElementsName2012.AFTER_BULLION_LEFT_1;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.AFTER_BULLION_LEFT_2;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.BOTTLE_1;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.BOTTLE_2;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.BULLION_1;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.BULLION_RIGHT;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.CD_FIXED_RED;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.DROP_1;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.HOME;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.HOME_FRONT_1;
import static org.cen.cup.cup2012.robot.match.ElementsName2012.OUT_DROP_1;

import java.util.Map;
import java.util.logging.Logger;

import org.cen.cup.cup2012.device.arm2012.ArmDownRequest2012;
import org.cen.cup.cup2012.device.arm2012.ArmType2012;
import org.cen.cup.cup2012.device.arm2012.ArmUpRequest2012;
import org.cen.cup.cup2012.navigation.NavigationMap2012;
import org.cen.geom.Point2D;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.device.navigation.BezierMoveRequest;
import org.cen.robot.device.navigation.RotationRequest;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.device.timer.SleepRequest;
import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.ITargetActionItemList;
import org.cen.robot.match.strategy.ITargetActionList;
import org.cen.robot.match.strategy.TargetList;
import org.cen.robot.match.strategy.impl.DefaultTargetAction;
import org.cen.robot.match.strategy.impl.SimpleTarget;
import org.cen.robot.match.strategy.impl.SimpleTargetActionItem;
import org.cen.robot.services.IRobotServiceProvider;

public class TargetsFactory2012 {
    private static final Logger LOGGER = LoggingUtils.getClassLogger();

    private final IRobotServiceProvider servicesProvider;

    private final INavigationMap map;

    private TargetList targets;

    public TargetsFactory2012(IRobotServiceProvider servicesProvider) {
        super();
        this.servicesProvider = servicesProvider;
        ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
        map = trajectoryService.getNavigationMap();

        buildTargets();
    }

    private void addCloseArm(ITargetAction action, ArmType2012 type) {
        IRobotDeviceRequest request = new ArmUpRequest2012(type);
        SimpleTargetActionItem item = new SimpleTargetActionItem(request);
        ITargetActionItemList items = action.getItems();
        items.addTargetActionItem(item);
    }

    private void addOpenArm(ITargetAction action, ArmType2012 type) {
        IRobotDeviceRequest request = new ArmDownRequest2012(type);
        SimpleTargetActionItem item = new SimpleTargetActionItem(request);
        ITargetActionItemList items = action.getItems();
        items.addTargetActionItem(item);
    }

    private ITarget addSimpleTarget(String name, double gain) {
        Map<String, Location> locations = map.getLocationsMap();
        Location l = locations.get(name);
        if (l == null) {
            logInvalidLocation(name);
        }
        Point2D p = null;
        if (l != null) {
            p = l.getPosition();
        }

        SimpleTarget target = new SimpleTarget(name, p, gain);
        targets.registerTarget(target);
        return target;
    }

    private void addSleepTime(ITargetAction action, int delay) {
        IRobotDeviceRequest request = new SleepRequest(delay);
        SimpleTargetActionItem item = new SimpleTargetActionItem(request);
        ITargetActionItemList items = action.getItems();
        items.addTargetActionItem(item);
    }

    private void addSplineRequest(ITargetAction action, String location, int d1, int d2, double angle) {
        Map<String, Location> locations = this.map.getLocationsMap();
        Location l = locations.get(location);
        if (l == null) {
            logInvalidLocation(location);
        }

        Point2D p = l.getPosition();
        d1 = NavigationMap2012.toDistance(d1);
        d2 = NavigationMap2012.toDistance(d2);
        BezierMoveRequest request = new BezierMoveRequest(p, d1, d2, angle);
        SimpleTargetActionItem item = new SimpleTargetActionItem(request);
        ITargetActionItemList items = action.getItems();
        items.addTargetActionItem(item);
    }

    private void addSymmetricCloseArm(ITargetAction[] a, ArmType2012 type) {
        // violet
        addCloseArm(a[1], type);
        // red
        type = getOpposite(type);
        addCloseArm(a[0], type);
    }

    private void addSymmetricOpenArm(ITargetAction[] a, ArmType2012 type) {
        // violet
        addOpenArm(a[1], type);
        // red
        type = getOpposite(type);
        addOpenArm(a[0], type);
    }

    private ITarget[] addSymmetricSimpleTarget(String name, double gain) {
        // red
        ITarget t1 = addSimpleTarget(NavigationMap2012.getRedName(name), gain);
        // violet
        ITarget t2 = addSimpleTarget(NavigationMap2012.getVioletName(name), gain);
        return new ITarget[] { t1, t2 };
    }

    private void addSymmetricSleepTime(ITargetAction[] a, int delay) {
        // red
        addSleepTime(a[0], delay);
        // violet
        addSleepTime(a[1], delay);
    }

    private void addSymmetricSplineRequest(ITargetAction[] actions, String location, int d1, int d2, int angle) {
        double a = NavigationMap2012.toAngle(angle);
        // red
        addSplineRequest(actions[0], NavigationMap2012.getRedName(location), d1, d2,
                NavigationMap2012.getSymmetricAngle(a));
        // violet
        addSplineRequest(actions[1], NavigationMap2012.getVioletName(location), d1, d2, a);
    }

    private ITargetAction[] addSymmetricTargetAction(ITarget[] targets, String start, String end, int time) {
        // red
        ITargetAction a1 = addTargetAction(targets[0], NavigationMap2012.getRedName(start),
                NavigationMap2012.getRedName(end), time);
        // violet
        ITargetAction a2 = addTargetAction(targets[1], NavigationMap2012.getVioletName(start),
                NavigationMap2012.getVioletName(end), time);
        return new ITargetAction[] { a1, a2 };
    }

    private ITargetAction addTargetAction(ITarget target, String start, String end, int time) {
        Map<String, Location> locations = map.getLocationsMap();
        Location l1 = locations.get(start);
        Location l2 = locations.get(end);
        if (l1 == null) {
            logInvalidLocation(start);
        }
        if (l2 == null) {
            logInvalidLocation(end);
        }
        DefaultTargetAction action = new DefaultTargetAction(target, l1, l2, time);
        ITargetActionList list = target.getActionList();
        list.addTargetAction(action);
        return action;
    }

    private void buildTargets() {
        // ATTENTION :
        // la symétrie se base toujours sur les coordonnées du côté VIOLET

        targets = new TargetList();
        ITarget[] t = addSymmetricSimpleTarget(BULLION_1, 3);
        ITargetAction[] a = addSymmetricTargetAction(t, BULLION_1, BULLION_1, 0);

        t = addSymmetricSimpleTarget(BOTTLE_1, 5);
        a = addSymmetricTargetAction(t, BOTTLE_1, BOTTLE_1, 5);

        t = addSymmetricSimpleTarget(BOTTLE_2, 5);
        a = addSymmetricTargetAction(t, BOTTLE_2, BOTTLE_2, 5);

        t = addSymmetricSimpleTarget(BULLION_RIGHT, 3);
        a = addSymmetricTargetAction(t, BULLION_RIGHT, BULLION_RIGHT, 5);
        addSymmetricOpenArm(a, ArmType2012.RIGHT);
        addSymmetricSleepTime(a, 1000);
        addSymmetricSplineRequest(a, DROP_1, 0x64, 0x32, 0xFC7C);
        addSymmetricCloseArm(a, ArmType2012.RIGHT);

        t = addSymmetricSimpleTarget(AFTER_BULLION_LEFT_1, 3);
        a = addSymmetricTargetAction(t, OUT_DROP_1, HOME_FRONT_1, 5);
        addSymmetricOpenArm(a, ArmType2012.RIGHT);
        addSymmetricSleepTime(a, 1000);
        addSymmetricSplineRequest(a, AFTER_BULLION_LEFT_1, 0x0D, 0x46, 0x0384);
        addSymmetricRotation(a, 1800);
        addSymmetricSplineRequest(a, HOME, 0x33, 0x27, 0xFC7C);
        addSymmetricCloseArm(a, ArmType2012.RIGHT);
        addSymmetricSplineRequest(a, HOME_FRONT_1, 0xB6, 0xCB, 0x384);

        t = addSymmetricSimpleTarget(AFTER_BULLION_LEFT_2, 3);
        a = addSymmetricTargetAction(t, HOME_FRONT_1, DROP_1, 5);
        addSymmetricOpenArm(a, ArmType2012.RIGHT);
        addSymmetricSleepTime(a, 1000);
        addSymmetricSplineRequest(a, AFTER_BULLION_LEFT_2, 0x1E, 0x78, 0x0384);
        addSymmetricSplineRequest(a, CD_FIXED_RED, 0x3E, 0x53, 0xFC7C);
        addSymmetricSplineRequest(a, DROP_1, 0x3C, 0x1E, 0xFC7C);
        addSymmetricCloseArm(a, ArmType2012.RIGHT);
    }

    private void addSymmetricRotation(ITargetAction[] a, int angle) {
        // red
        addRotation(a[0], angle);
        // violet
        addRotation(a[1], angle);
    }

    private void addRotation(ITargetAction action, int angle) {
        double a = NavigationMap2012.toAngle(angle);
        IRobotDeviceRequest request = new RotationRequest(a);
        SimpleTargetActionItem item = new SimpleTargetActionItem(request);
        ITargetActionItemList items = action.getItems();
        items.addTargetActionItem(item);
    }

    private ArmType2012 getOpposite(ArmType2012 type) {
        if (type == ArmType2012.LEFT) {
            return ArmType2012.RIGHT;
        } else {
            return ArmType2012.LEFT;
        }
    }

    public TargetList getTargets() {
        return targets;
    }

    protected void logInvalidLocation(String s) {
        LOGGER.severe("Unknown location: " + s);
    }
}
