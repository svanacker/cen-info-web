package org.cen.cup.cup2008.device.launcher;

import java.util.logging.Logger;

import org.cen.com.IComService;
import org.cen.cup.cup2008.device.launcher.LauncherLoaderRequest.LoaderAction;
import org.cen.cup.cup2008.device.launcher.LauncherMoveRequest.LauncherMove;
import org.cen.logging.LoggingUtils;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.navigation.com.MoveOutData;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.device.request.IDeviceRequestDispatcher;
import org.cen.robot.services.IRobotServiceProvider;

public class LauncherHandler {
    private static final Logger LOGGER = LoggingUtils.getClassLogger();

    private static final int MAX_BALL_COUNT = 3;

    private static final long PAUSE = 2000;

    private final IComService comService;

    private final IDeviceRequestDispatcher dispatcher;

    private boolean locked;

    private final IRobotServiceProvider servicesProvider;

    public LauncherHandler(IRobotServiceProvider provider) {
        super();
        servicesProvider = provider;
        comService = servicesProvider.getService(IComService.class);
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        dispatcher = handler.getRequestDispatcher();
    }

    private void changeLock() {
        if (locked) {
            unlockBall();
        } else {
            lockBall();
        }
    }

    private void close() {
        dispatcher.sendRequest(new LauncherMoveRequest(LauncherMove.CLOSE));
    }

    private void launchBall() {
        dispatcher.sendRequest(new LauncherLaunchRequest());
    }

    private void launchBalls() throws InterruptedException {
        int count = 0;
        while (count++ < MAX_BALL_COUNT) {
            // une balle est en attente
            launchBall();
            Thread.sleep(PAUSE);
            // introduit la balle suivante
            unlockBall();
            // attend que la balle soit en place
            Thread.sleep(PAUSE);
            // verrouille le chargeur
            lockBall();
            Thread.sleep(PAUSE);
        }
    }

    private void liftDown() {
        dispatcher.sendRequest(new LauncherMoveRequest(LauncherMove.DOWN));
    }

    private void liftUp() {
        dispatcher.sendRequest(new LauncherMoveRequest(LauncherMove.UP));
    }

    private void loadBall() throws InterruptedException {
        // ferme les pinces
        close();
        Thread.sleep(PAUSE);
        // recule
        moveBack();
        // l�ve la balle
        liftUp();
        Thread.sleep(PAUSE);
        // ouvre les pinces
        open();
        Thread.sleep(PAUSE);
        // bascule le contr�leur d'admission des balles
        changeLock();
    }

    /**
     * Positionnement des balles et du contr�leur d'admission
     * 
     * <pre>
     * - Balle1 .   . | X 
     * - Balle2 . | X   X 
     * - Balle3 X   X | X
     * </pre>
     * 
     * @throws InterruptedException
     */
    private void loadBalls() throws InterruptedException {
        int count = 0;
        while (count++ < MAX_BALL_COUNT) {
            // abaisse la pince
            liftDown();
            Thread.sleep(PAUSE);
            // avance jusqu'� la balle
            moveForth();
            Thread.sleep(PAUSE);
            // charge la balle
            loadBall();
            Thread.sleep(PAUSE);
        }
    }

    private void lockBall() {
        locked = true;
        dispatcher.sendRequest(new LauncherLoaderRequest(LoaderAction.LOCK));
    }

    private void moveBack() throws InterruptedException {
        comService.writeOutData(new MoveOutData(-0x0200, -0x0200, 0x04, 0x02));
        Thread.sleep(PAUSE);
        comService.writeOutData(new StopOutData());
    }

    private void moveForth() throws InterruptedException {
        comService.writeOutData(new MoveOutData(0x0200, 0x0200, 0x04, 0x02));
        Thread.sleep(PAUSE);
        comService.writeOutData(new StopOutData());
    }

    private void moveToDispenser() {
    }

    private void moveToGoal() {
        // TODO Auto-generated method stub

    }

    private void open() {
        dispatcher.sendRequest(new LauncherMoveRequest(LauncherMove.OPEN));
    }

    public void sequence1() {
        try {
            // initialisation
            unlockBall();
            liftUp();
            open();
            Thread.sleep(PAUSE);
            // se met en place aupr�s du conteneur
            moveToDispenser();
            // charge toutes les balles
            loadBalls();
            // se met en place pour atteindre le but
            moveToGoal();
            // tire toutes les balles
            launchBalls();
        } catch (InterruptedException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    public void sequence2() {
        try {
            launchBalls();
        } catch (InterruptedException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    private void unlockBall() {
        locked = false;
        dispatcher.sendRequest(new LauncherLoaderRequest(LoaderAction.UNLOCK));
    }
}
