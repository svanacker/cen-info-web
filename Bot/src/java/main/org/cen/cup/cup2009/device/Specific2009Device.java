package org.cen.cup.cup2009.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.cup.cup2008.device.container.ContainerResult;
import org.cen.cup.cup2009.device.com.SleepOutData;
import org.cen.cup.cup2009.device.lift.Lift2009Request;
import org.cen.cup.cup2009.device.lift.com.LiftGotoBottomOutData;
import org.cen.cup.cup2009.device.lift.com.LiftMoveOutData;
import org.cen.cup.cup2009.device.lintel.Lintel2009Request;
import org.cen.cup.cup2009.device.lintel.com.LintelCloseOutData;
import org.cen.cup.cup2009.device.lintel.com.LintelDeployOutData;
import org.cen.cup.cup2009.device.lintel.com.LintelOpenOutData;
import org.cen.cup.cup2009.device.lintel.com.LintelUndeployOutData;
import org.cen.cup.cup2009.device.plier.Plier2009Request;
import org.cen.cup.cup2009.device.plier.com.PlierCloseOutData;
import org.cen.cup.cup2009.device.plier.com.PlierLeftOutData;
import org.cen.cup.cup2009.device.plier.com.PlierMiddleOutData;
import org.cen.cup.cup2009.device.plier.com.PlierOpenOutData;
import org.cen.cup.cup2009.device.plier.com.PlierRightOutData;
import org.cen.logging.LoggingUtils;
import org.cen.math.PropertiesMathUtils;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.collision.com.CollisionDetectionDisabledOutData;
import org.cen.robot.device.collision.com.CollisionDetectionEnabledOutData;
import org.cen.robot.device.motor.com.MotorOutData;
import org.cen.robot.device.navigation.com.MoveOutData;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.factory.IRobotFactory;
import org.cen.robot.services.IRobotServiceProvider;

public class Specific2009Device extends AbstractRobotDevice implements InDataListener {
    private static final Logger LOGGER = LoggingUtils.getClassLogger();
    public static final String NAME = "specific2009";
    private static final String PROPERTY_LIFT_MOVE_FACTOR = "liftMoveFactor";

    private double liftMoveFactor;

    public Specific2009Device() {
        super(NAME);
    }

    @Override
    public void debug(String debugAction) {
        IRobotDeviceRequest request = null;

        // Request for Lift
        if (debugAction.equals("up")) {
            request = new Lift2009Request(Lift2009Request.Action.UP);
        } else if (debugAction.equals("down")) {
            request = new Lift2009Request(Lift2009Request.Action.DOWN);
        } else if (debugAction.equals("smallUp")) {
            request = new Lift2009Request(Lift2009Request.Action.SMALL_UP);
        } else if (debugAction.equals("smallDown")) {
            request = new Lift2009Request(Lift2009Request.Action.SMALL_DOWN);
        } else if (debugAction.equals("gotoBottom")) {
            request = new Lift2009Request(Lift2009Request.Action.BOTTOM);
        }
        // Request for Plier
        else if (debugAction.equals("closePlier")) {
            request = new Plier2009Request(Plier2009Request.Action.CLOSE);
        } else if (debugAction.equals("openPlier")) {
            request = new Plier2009Request(Plier2009Request.Action.OPEN);
        } else if (debugAction.equals("leftPlier")) {
            request = new Plier2009Request(Plier2009Request.Action.LEFT);
        } else if (debugAction.equals("middlePlier")) {
            request = new Plier2009Request(Plier2009Request.Action.MIDDLE);
        } else if (debugAction.equals("rightPlier")) {
            request = new Plier2009Request(Plier2009Request.Action.RIGHT);
        }
        // Request for Lintel
        else if (debugAction.equals("closeLintel")) {
            request = new Lintel2009Request(Lintel2009Request.Action.CLOSE);
        } else if (debugAction.equals("openLintel")) {
            request = new Lintel2009Request(Lintel2009Request.Action.OPEN);
        } else if (debugAction.equals("deployLintel")) {
            request = new Lintel2009Request(Lintel2009Request.Action.DEPLOY);
        } else if (debugAction.equals("undeployLintel")) {
            request = new Lintel2009Request(Lintel2009Request.Action.UNDEPLOY);
        }
        if (request != null) {
            handleRequest(request);
        }
    }

    private LiftMoveOutData getLiftMoveOutData(int liftIndex) {
        return new LiftMoveOutData(liftMoveFactor, liftIndex);
    }

    public List<OutData> getOutData(IRobotDeviceRequest request) {
        List<OutData> list = new ArrayList<OutData>();
        // Lift Operation
        if (request instanceof Lift2009Request) {
            Lift2009Request liftRequest = (Lift2009Request) request;
            switch (liftRequest.getAction()) {
            case UP:
                list.add(getLiftMoveOutData(liftRequest.getData()));
                break;
            case DOWN:
                list.add(getLiftMoveOutData(liftRequest.getData()));
                break;
            case SMALL_UP:
                list.add(getLiftMoveOutData(liftRequest.getData()));
                break;
            case SMALL_DOWN:
                list.add(getLiftMoveOutData(liftRequest.getData()));
                break;
            case BOTTOM:
                list.add(new LiftGotoBottomOutData());
                break;
            }
        }

        // Pliers for Column Elements Operation
        else if (request instanceof Plier2009Request) {
            Plier2009Request plierRequest = (Plier2009Request) request;
            switch (plierRequest.getAction()) {
            case CLOSE:
                list.add(new PlierCloseOutData());
                break;
            case OPEN:
                list.add(new PlierOpenOutData());
                break;
            case LEFT:
                list.add(new PlierLeftOutData());
                break;
            case MIDDLE:
                list.add(new PlierMiddleOutData());
                break;
            case RIGHT:
                list.add(new PlierRightOutData());
                break;
            }
        }

        // Pliers for Lintel Operation
        else if (request instanceof Lintel2009Request) {
            Lintel2009Request lintelRequest = (Lintel2009Request) request;
            switch (lintelRequest.getAction()) {
            case CLOSE:
                list.add(new LintelCloseOutData());
                break;
            case OPEN:
                list.add(new LintelOpenOutData());
                break;
            case DEPLOY:
                list.add(new LintelDeployOutData());
                break;
            case UNDEPLOY:
                list.add(new LintelUndeployOutData());
                break;
            }
        }

        // Sequence Operation
        else if (request instanceof Sequence2009Request) {
            Sequence2009Request sequenceRequest = (Sequence2009Request) request;
            switch (sequenceRequest.getAction()) {

            case INIT:
                // Close the pliers
                list.add(new PlierCloseOutData());

                list.add(new PlierMiddleOutData());

                // Close the plier
                list.add(new PlierCloseOutData());
                break;

            case TIME:
                sleep(list, sequenceRequest.getValue() * 1000);
                break;

            case DISABLE_COLLISION_DETECTION:
                list.add(new CollisionDetectionDisabledOutData());
                break;
            case ENABLE_COLLISION_DETECTION:
                list.add(new CollisionDetectionEnabledOutData());
                break;
            case TAKE_COLUMN:
                list.add(new PlierMiddleOutData());

                // Close the plier
                list.add(new PlierCloseOutData());
                // Goto bottom
                list.add(new LiftGotoBottomOutData());
                // Go Up
                list.add(getLiftMoveOutData(-64));
                // Time to get the element
                sleep(list, 2000);
                break;
            case PREPARE_TO_BUILD_FIRST_CONSTRUCTION: {
                list.add(new StopOutData());

                // Move the servo to the right
                list.add(new PlierRightOutData());

                // Go Up
                list.add(getLiftMoveOutData(-70));

                break;
            }
            // Go Forward
            case FORWARD:
                list.add(new StopOutData());
                list.add(new MoveOutData(2144, 2144, 3, 1));
                break;

            // Build construction (2x2 columns)
            case FIRST_BUILD_COLUMNS_TYPE_1:
                list.add(new StopOutData());
                // Without slavery
                list.add(new MotorOutData(50, 50));
                sleep(list, 1000);
                list.add(new StopOutData());

                // On pose la pince sur la colline
                list.add(getLiftMoveOutData(40));

                // On ouvre la pince pour lib�rer 2 palets
                list.add(new PlierOpenOutData());
                sleep(list, 500);

                // On l�ve la pince pour r�cup�rer les 2 palets en haut
                list.add(getLiftMoveOutData(-55));
                sleep(list, 1000);

                // Ferme la pince pour r�cup�rer les 2 palets du haut
                list.add(new PlierCloseOutData());
                sleep(list, 500);

                // L�ve encore la pince pour ne pas les bousculer
                list.add(getLiftMoveOutData(-65));
                sleep(list, 300);

                // Met la pince � gauche
                list.add(new PlierLeftOutData());
                sleep(list, 2000);

                // Descend la pince
                list.add(getLiftMoveOutData(130));

                // Lib�re les deux palets
                list.add(new PlierOpenOutData());
                sleep(list, 1500);

                // On monte pour �viter les deux palets
                list.add(getLiftMoveOutData(-130));

                // Va au milieu
                list.add(new PlierMiddleOutData());

                // Ferme la pince pr�ventivement
                list.add(new PlierCloseOutData());
                break;
            case FIRST_BUILD_LINTEL_TYPE1:
                // Pour �viter de descendre en m�me temps qu'on recule
                sleep(list, 1000);
                // D�ploit la pince linteau
                list.add(new LintelDeployOutData());

                // Descend l'ascenseur pour �tre s�r d'�tre ok
                list.add(getLiftMoveOutData(128));
                sleep(list, 2000);

                // Remonte l'ascenseur pour pouvoir se retirer tranquillement
                list.add(getLiftMoveOutData(-40));
                sleep(list, 1000);
                list.add(new LintelUndeployOutData());

                // La suite est prise en charge par la navigation qui recule
                break;
            case PREPARE_TO_BUILD_SECOND_CONSTRUCTION:
                list.add(getLiftMoveOutData(-165));
                sleep(list, 3000);
                break;
            case BUILD_2_COLUMNS_ON_TOP:
                // Force un peu pour se caler
                list.add(new StopOutData());
                list.add(new MotorOutData(50, 50));
                sleep(list, 1000);
                list.add(new StopOutData());
                // Descend la pince pour �viter de larguer trop haut
                list.add(getLiftMoveOutData(15));
                list.add(new PlierOpenOutData());
                sleep(list, 1000);
                // Remonte la pince pour �viter de frotter
                list.add(getLiftMoveOutData(-20));

                list.add(new MoveOutData(-800, -800, 2, 1));
                break;

            // // Let the first Lintel on construction
            // case FIRST_BUILD_COLUMNS_AND_LINTEL_TYPE_2:
            // list.add(new StopOutData());
            // // Without slavery
            // list.add(new MotorOutData(50, 50));
            // sleep(list, 1000);
            // list.add(new StopOutData());
            //
            // // FIRST_LINE
            //
            // // Atteint la colline
            // list.add(getLiftMoveOutData(20));
            //
            // // Ouvre la pince pour lib�rer un palet
            // list.add(new PlierOpenOutData());
            //
            // // Soul�ve l�g�rement pour avoir les 3 palets
            // list.add(getLiftMoveOutData(-10));
            //
            // // Ferme la pince
            // list.add(new PlierCloseOutData());
            // sleep(list, 500);
            //
            // // L�ve l'ascenseur
            // list.add(getLiftMoveOutData(-60));
            // sleep(list, 300);
            //
            // // Va � gauche
            // list.add(new PlierLeftOutData());
            // sleep(list, 3000);
            //
            // // Descend la pince
            // list.add(getLiftMoveOutData(70));
            //
            // // Ouvre la pince pour lib�rer un palet
            // list.add(new PlierOpenOutData());
            // sleep(list, 800);
            //
            // // Ferme la pince pour r�cup�rer 2 palets
            // list.add(new PlierCloseOutData());
            // sleep(list, 800);
            //
            // // L�ve l'ascenseur pour reprendre 2 palets
            // list.add(getLiftMoveOutData(-80));
            //
            // // Va au milieu
            // list.add(new PlierMiddleOutData());
            // sleep(list, 2000);
            //
            // // LINTEL
            //
            // // Recule
            // list.add(new MoveOutData(-1250, -1250, 4, 1));
            // sleep(list, 1000);
            //
            // // D�ploit le linteau
            // list.add(new LintelDeployOutData());
            // sleep(list, 1000);
            //
            // // Baisse l'ascenseur
            // list.add(getLiftMoveOutData(165));
            // sleep(list, 4000);
            //
            // // On recule pour laisser passer l'ascenseur
            // list.add(new StopOutData());
            // list.add(new MoveOutData(-600, -600, 2, 1));
            // sleep(list, 2000);
            //
            // // SECOND LINE
            // // Met la pince � gauche
            // list.add(new PlierLeftOutData());
            // sleep(list, 3000);
            //
            // // On l�ve l'ascenseur
            // list.add(getLiftMoveOutData(-150));
            //
            // // Revient sur la colline
            // list.add(new StopOutData());
            // list.add(new MoveOutData(1850, 1850, 4, 1));
            // sleep(list, 3000);
            //
            // // Baisse pour atteindre le deuxi�me linteau
            // list.add(getLiftMoveOutData(20));
            //
            // // Ouvre la pince
            // list.add(new PlierOpenOutData());
            //
            // // On remonte d'un cran
            // list.add(getLiftMoveOutData(-30));
            //
            // // Ferme la pince
            // list.add(new PlierCloseOutData());
            // sleep(list, 500);
            //
            // // On remonte
            // list.add(getLiftMoveOutData(-40));
            // sleep(list, 300);
            //
            // // Va � droite
            // list.add(new PlierRightOutData());
            // sleep(list, 3000);
            //
            // // Baisse l'ascenseur
            // list.add(getLiftMoveOutData(70));
            //
            // // Ouvre la pince
            // list.add(new PlierOpenOutData());
            // sleep(list, 800);
            //
            // // Remonte l'ascenseur
            // list.add(getLiftMoveOutData(-80));
            //
            // // Met la pince au milieu
            // list.add(new PlierMiddleOutData());
            // sleep(list, 2000);
            //
            // break;
            // // Back very smooth
            // // list.add(new StopOutData());
            // // list.add(new MoveOutData(-600, -600, 2, 1));
            // // sleep(list, 3000);
            // case PREPARE_TO_LOAD_SECOND_LINTEL:
            // list.add(new PlierMiddleOutData());
            // // Deploy the lintel
            // list.add(new LintelDeployOutData());
            // // Goto to the bottom to have a good reference
            // list.add(new LiftGotoBottomOutData());
            // // Up the lift
            // list.add(getLiftMoveOutData(-25));
            // // TODO : Supprimer les Tempos qui ne servent � rien
            // sleep(list, 3000);
            // break;
            // // L�ve le linteau pour le mettre � la bonne hauteur directement
            // pour la d�pose
            // case LOAD_SECOND_LINTEL:
            // // Il faut arriver pince ferme lorsqu'on veut mettre le linteau
            // pour �carter
            // // l�g�rement les deux palets en arrivant
            // list.add(new PlierCloseOutData());
            // // Pour pr�server le linteau
            // list.add(new LintelUndeployOutData());
            // list.add(getLiftMoveOutData(-177));
            // sleep(list, 3000);
            // break;
            // case SECOND_LINTEL_CONSTRUCTION:
            // // Without slavery
            // list.add(new StopOutData());
            // list.add(new MotorOutData(50, 50));
            // sleep(list, 1500);
            // list.add(new StopOutData());
            // // Baisse le linteau
            // list.add(new LintelDeployOutData());
            // sleep(list, 2000);
            // list.add(getLiftMoveOutData(94));
            // sleep(list, 5000);
            // break;
            }
        }
        return list;
    }

    @Override
    public void initialize(IRobotServiceProvider servicesProvider) {
        super.initialize(servicesProvider);
        IRobotFactory factory = servicesProvider.getService(IRobotFactory.class);
        Properties properties = factory.getRobotConfiguration().getProperties();
        liftMoveFactor = PropertiesMathUtils.getDouble(properties, PROPERTY_LIFT_MOVE_FACTOR, 1);
        LOGGER.config("liftFactor: " + liftMoveFactor);
        // IComService comService =
        // servicesProvider.getService(IComService.class);
        // comService.getDecodingService().registerDecoder(new
        // ContainerDataDecoder());
        // comService.addInDataListener(this);
    }

    @Override
    protected void internalHandleRequest(IRobotDeviceRequest request) {
        List<OutData> data = getOutData(request);
        for (OutData d : data) {
            if (d instanceof SleepOutData) {
                ((SleepOutData) d).sleep();
            } else {
                send(d);
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
    }

    private void send(OutData data) {
        IComService comService = servicesProvider.getService(IComService.class);
        comService.writeOutData(data);
    }

    private void sleep(List<OutData> list, int delay) {
        list.add(new SleepOutData(delay));
    }
}
