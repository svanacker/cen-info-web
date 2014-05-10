package org.cen.vision;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cen.geom.Dimension;
import org.cen.geom.Point2D;
import org.cen.vision.coordinates.CoordinatesFactory;
import org.cen.vision.coordinates.CoordinatesTransform;
import org.cen.vision.filters.TargetStat;

/**
 * Panel which enables to see the webcam
 * 
 * @author svanacker
 * @version 13/03/2007
 * 
 */
@Deprecated
public class VisionPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** The image of the Webcam */
    protected JLabel inImage;

    /** The image which is filtered */
    protected JLabel outImage;

    /** The Webcam which takes images */
    protected WebCam webCam;

    /**
     * Constructor
     * 
     * @param webCam
     *            the webcam
     */
    public VisionPanel(WebCam webCam) {
        super();
        this.webCam = webCam;
        init();
    }

    private void init() {
        inImage = new JLabel();
        add(inImage);

        outImage = new JLabel();
        add(outImage);

        final CoordinatesTransform c = CoordinatesFactory.getCoordinates(webCam.getProperties());

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point2D p = c.screenToGameBoard(e.getX(), e.getY());
                setToolTipText("(" + (int) (p.getX()) + " mm, " + (int) (p.getY()) + " mm)");
            }
        });
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void run() {
        Dimension dimension = webCam.getProperties().getImageDimension();
        float[] f = new float[(int) (dimension.getHeight()) * (int) (dimension.getWidth()) * 3];

        while (true) {
            Image acquisitionImage = null;
            // Image acquisitionImage = webCam.getAcquisition().getImage();

            if (acquisitionImage != null) {
                ParameterBlock pb = new ParameterBlock();
                pb.addSource(acquisitionImage);
                if (webCam.getCalibrationDescriptor().getSelectedCalibration() != null)
                    pb.add(webCam.getCalibrationDescriptor().getSelectedCalibration().getExpected());
                else
                    pb.add(0);
                pb.add(f);
                PlanarImage target = JAI.create("ColorFilter", pb, null);
                RenderedOp s = JAI.create("TargetFilter", target, null);

                TargetStat stats = (TargetStat) s.getProperty("Magnitude");
                Point max = stats.getMaxRecursively().getLocation();

                drawTarget(acquisitionImage, max);
                inImage.setIcon(new ImageIcon(acquisitionImage));

            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawTarget(Image img, Point max) {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setColor(Color.red);
        g.fillOval(max.x * 10, (int) (max.y * 7.5), 10, 10);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public WebCam getWebCam() {
        return webCam;
    }

}