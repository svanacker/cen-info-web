package org.cen.cup.cup2011.robot.match;

import org.cen.cup.cup2011.device.vision2011.PawnPositionResult;
import org.cen.geom.Point2D;

// TODO à intégrer au device
public class FakeVisionHandler2011 {

    private static final int PAWN_SENDED = 1;

    private static final int PAWN_RADIUS = 100;

    private static final int Y_MIN = 550;

    private static final int X_MIN = 175;

    public static final int BOARD_HEIGHT = 3000;

    public static final int BOARD_WIDTH = 2100;

    // public static final int BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2;
    //
    // public static final int BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2;
    //
    // public static final int BORDER_WIDTH = 22;
    //
    // public static final int BAND_WIDTH = 50;
    //
    // public static final int BOX_SIZE = 350;
    //
    // public static final int RADIUS_SIZE = 50;
    //
    // public static final int ZONE_WIDTH = 120;
    //
    // public static final int ZONE_HEIGHT = 130;

    private final Vision2011Handler specifique2011;

    public FakeVisionHandler2011(Vision2011Handler pSpecific2011Handler) {
        this.specifique2011 = pSpecific2011Handler;
    }

    public void visionRequest() {
        PawnPositionResult result;
        int randomX;
        int randomY;
        int pawnsNumber = (int) (PAWN_SENDED * (Math.random() * 10 + 1) / 10);
        int compteur;
        for (compteur = 0; compteur < pawnsNumber; compteur++) {

            randomX = (int) (PAWN_RADIUS + X_MIN + Math.random() * (BOARD_WIDTH - 2 * (PAWN_RADIUS + X_MIN)));
            randomY = (int) (PAWN_RADIUS + Y_MIN + Math.random() * (BOARD_HEIGHT - 2 * (PAWN_RADIUS + Y_MIN)));

            result = new PawnPositionResult(null, new Point2D.Double(randomX, randomY));
            specifique2011.handleResult(result);
        }
        result = new PawnPositionResult(null, new Point2D.Double(-1, -1));
        specifique2011.handleResult(result);
    }
}
