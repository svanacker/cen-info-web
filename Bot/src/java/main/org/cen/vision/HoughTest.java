package org.cen.vision;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RIFRegistry;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Vector3d;

import org.cen.adapter.AwtAdapterUtils;
import org.cen.geom.Dimension;
import org.cen.geom.Point2D;
import org.cen.math.Size2D;
import org.cen.vision.coordinates.CoordinatesFactory;
import org.cen.vision.coordinates.CoordinatesTransform;
import org.cen.vision.dataobjects.WebCamProperties;
import org.cen.vision.filters.ColorFilterDescriptor;
import org.cen.vision.filters.HoughLine;
import org.cen.vision.filters.HoughTransformDescriptor;

public class HoughTest {
    private static final String DEFAULT_INPUT = "testR1.jpg";

    public static Image getImage(String name) {
        URL url = HoughTest.class.getClassLoader().getResource("org/cen/cup/cup2009/gameboard/configuration/" + name);
        Image srcImage = Toolkit.getDefaultToolkit().createImage(url);
        new ImageIcon(srcImage);
        BufferedImage inputImage = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = inputImage.getGraphics();
        g.drawImage(srcImage, 0, 0, null);
        return inputImage;
    }

    public static void main(String[] args) {
        registerOperators();

        new HoughTest();
    }

    public static void registerOperators() {
        String productName = "testjai";
        OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();

        ColorFilterDescriptor colorFilterDescriptor = new ColorFilterDescriptor();
        String operationName = "ColorFilter";
        RenderedImageFactory rif = colorFilterDescriptor;
        or.registerDescriptor(colorFilterDescriptor);
        RIFRegistry.register(or, operationName, productName, rif);

        HoughTransformDescriptor houghTransformDescriptor = new HoughTransformDescriptor();
        operationName = "HoughTransform";
        rif = houghTransformDescriptor;
        or.registerDescriptor(houghTransformDescriptor);
        RIFRegistry.register(or, operationName, productName, rif);
    }

    private double cameraInclination = Math.toRadians(21);

    private final double cameraVisionAngle = Math.toRadians(45);

    private double colorAngle;

    private CoordinatesTransform ct;

    private final JLabel filteredImage;

    private Image filterOutput;

    private RenderedOp houghTransform;

    private final JLabel image;

    private Image input;

    private final List<Point2D> intersections = new ArrayList<Point2D>();

    private final AffineTransform t = new AffineTransform();

    private final WebCamProperties wcp = new WebCamProperties();

    private int xPosition = 1050;

    private int yPosition = 1500;

    private double zAngle = Math.toRadians(220);

    private int zPosition = 400;

    private boolean equalize = false;

    private boolean normalize = false;

    private String inputName;

    public HoughTest() {
        final JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                super.windowClosed(e);
            }
        });
        Container c = frame.getContentPane();

        c.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.gridwidth = 2;

        image = new JLabel();
        c.add(image, gbc);

        filteredImage = new JLabel();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        c.add(filteredImage, gbc);

        addImageSelection(c, gbc);
        addCoordinatesLabel(c, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        addColorFilterControl(c, gbc);
        addCoordinatesSliders(c, gbc);
        addOrientationControl(c, gbc);
        addInclinationControl(c, gbc);
        addWhiteBalanceSelection(c, gbc);

        setInput(DEFAULT_INPUT);
        applyColorFilter();
        applyTransform();
        drawResults();

        frame.pack();
        frame.setVisible(true);
    }

    private void addColorFilterControl(Container c, GridBagConstraints gbc) {
        JLabel l = new JLabel("Couleur :");
        gbc.gridwidth = 1;
        c.add(l, gbc);

        final JSlider slider = new JSlider(0, 36000, (int) (Math.toDegrees(colorAngle) * 100));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        c.add(slider, gbc);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                colorAngle = Math.toRadians(slider.getValue() / 100d);
                slider.setToolTipText(Double.toString(colorAngle));
                applyColorFilter();
            }
        });
    }

    private void addWhiteBalanceSelection(Container c, GridBagConstraints gbc) {
        final JCheckBox normalizeCheckBox = new JCheckBox("Normaliser", normalize);
        final JCheckBox equalizeCheckBox = new JCheckBox("Egaliser", equalize);

        ChangeListener l = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (normalize != normalizeCheckBox.isSelected() || equalize != equalizeCheckBox.isSelected()) {
                    normalize = normalizeCheckBox.isSelected();
                    equalize = equalizeCheckBox.isSelected();
                    setInput(inputName);
                }
            }
        };

        normalizeCheckBox.addChangeListener(l);
        equalizeCheckBox.addChangeListener(l);

        gbc.gridwidth = 1;
        c.add(normalizeCheckBox, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        c.add(equalizeCheckBox, gbc);
    }

    private void addCoordinatesLabel(Container c, GridBagConstraints gbc) {
        final JLabel label = new JLabel();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        c.add(label, gbc);

        image.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                Point2D pt = ct.screenToGameBoard(p.x, p.y);
                t.transform(AwtAdapterUtils.toAwtPoint2D(pt), AwtAdapterUtils.toAwtPoint2D(pt));
                label.setText("x: " + pt.getX() + ", y: " + pt.getY());
            }
        });
    }

    private void addCoordinatesSliders(Container c, GridBagConstraints gbc) {
        JLabel label = new JLabel("x: ");
        gbc.gridwidth = 1;
        c.add(label, gbc);

        final JSlider xSlider = new JSlider(0, 2100, xPosition);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        c.add(xSlider, gbc);

        label = new JLabel("y: ");
        gbc.gridwidth = 1;
        c.add(label, gbc);

        final JSlider ySlider = new JSlider(0, 3000, yPosition);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        c.add(ySlider, gbc);

        label = new JLabel("z: ");
        gbc.gridwidth = 1;
        c.add(label, gbc);

        final JSlider zSlider = new JSlider(0, 1000, zPosition);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        c.add(zSlider, gbc);

        ChangeListener l = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() == xSlider) {
                    xPosition = xSlider.getValue();
                    xSlider.setToolTipText(String.valueOf(xPosition));
                } else if (e.getSource() == ySlider) {
                    yPosition = ySlider.getValue();
                    ySlider.setToolTipText(String.valueOf(yPosition));
                } else if (e.getSource() == zSlider) {
                    zPosition = zSlider.getValue();
                    zSlider.setToolTipText(String.valueOf(zPosition));
                }
                drawSource();
            }
        };
        xSlider.addChangeListener(l);
        ySlider.addChangeListener(l);
        zSlider.addChangeListener(l);
    }

    private void addImageSelection(Container c, GridBagConstraints gbc) {
        String[] items = { "TestG1.jpg", "TestG2.jpg", "TestG3.jpg", "TestG4.jpg", "TestG5.jpg", "TestR1.jpg",
                "TestR2.jpg", "TestR3.jpg", "TestR4.jpg", "TestR5.jpg" };
        final JComboBox cb = new JComboBox(items);
        gbc.gridwidth = 1;
        c.add(cb, gbc);

        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setInput((String) cb.getSelectedItem());
            }
        });
    }

    private void addInclinationControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Inclinaison webcam :");
        gbc.gridwidth = 1;
        cp.add(l, gbc);

        final JSlider slider = new JSlider(0, 9000, (int) (Math.toDegrees(cameraInclination) * 100));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(slider, gbc);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cameraInclination = Math.toRadians(slider.getValue() / 100d);
                slider.setToolTipText(Double.toString(Math.toDegrees(cameraInclination)));
                drawSource();
            }
        });
    }

    private void addOrientationControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Orientation du robot :");
        gbc.gridwidth = 1;
        cp.add(l, gbc);

        final JSlider slider = new JSlider(0, 36000, (int) (Math.toDegrees(zAngle) * 100));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(slider, gbc);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                zAngle = Math.toRadians(slider.getValue() / 100d);
                slider.setToolTipText(Double.toString(Math.toDegrees(zAngle)));
                drawSource();
            }
        });
    }

    private static RenderedOp getHistogram(Image img, int binCount) {
        // Get the band count.
        int numBands = 4;

        // Allocate histogram memory.
        int[] numBins = new int[numBands];
        double[] lowValue = new double[numBands];
        double[] highValue = new double[numBands];
        for (int i = 0; i < numBands; i++) {
            numBins[i] = binCount;
            lowValue[i] = 0.0;
            highValue[i] = 255.0;
        }

        // Create the Histogram object.
        Histogram hist = new Histogram(numBins, lowValue, highValue);

        // Create the histogram op.
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(img);
        RenderedOp histImage = JAI.create("histogram", pb);

        // Retrieve the histogram.
        hist = (Histogram) histImage.getProperty("histogram");

        return histImage;
    }

    private Image equalizeHistogram(Image img) {
        // Create an equalization CDF.
        int numBands = 3;
        int binCount = 256;
        float[][] CDFeq = new float[numBands][];
        for (int b = 0; b < numBands; b++) {
            CDFeq[b] = new float[binCount];
            for (int i = 0; i < binCount; i++) {
                CDFeq[b][i] = (float) (i + 1) / (float) binCount;
            }
        }

        RenderedOp op = getHistogram(img, binCount);
        // Create a histogram-equalized image.
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(op);
        pb.add(CDFeq);
        RenderedOp eq = JAI.create("matchcdf", pb);
        return eq.getAsBufferedImage();
    }

    private Image normalizeHistogram(Image img) {
        // Create a normalization CDF.
        int numBands = 3;
        int binCount = 256;
        double[] mean = new double[] { 128.0, 128.0, 128.0, 128.0 };
        double[] stDev = new double[] { 64.0, 64.0, 64.0, 64.0 };
        float[][] CDFnorm = new float[numBands][];
        for (int b = 0; b < numBands; b++) {
            CDFnorm[b] = new float[binCount];
            double mu = mean[b];
            double twoSigmaSquared = 2.0 * stDev[b] * stDev[b];
            CDFnorm[b][0] = (float) Math.exp(-mu * mu / twoSigmaSquared);
            for (int i = 1; i < binCount; i++) {
                double deviation = i - mu;
                CDFnorm[b][i] = CDFnorm[b][i - 1] + (float) Math.exp(-deviation * deviation / twoSigmaSquared);
            }
        }
        for (int b = 0; b < numBands; b++) {
            double CDFnormLast = CDFnorm[b][binCount - 1];
            for (int i = 0; i < binCount; i++) {
                CDFnorm[b][i] /= CDFnormLast;
            }
        }

        RenderedOp op = getHistogram(img, binCount);
        // Create a histogram-equalized image.
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(op);
        pb.add(CDFnorm);
        RenderedOp nm = JAI.create("matchcdf", pb);
        return nm.getAsBufferedImage();
    }

    private void applyColorFilter() {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(input);
        pb.add(5);
        pb.add(5);
        pb.add(2);
        pb.add(2);
        RenderedOp op = JAI.create("BoxFilter", pb);

        pb = new ParameterBlock();
        pb.addSource(op.getAsBufferedImage());
        pb.add(colorAngle);
        op = JAI.create("ColorFilter", pb);

        pb = new ParameterBlock();
        pb.addSource(op.getAsBufferedImage());
        pb.add(3);
        pb.add(3);
        pb.add(1);
        pb.add(1);
        op = JAI.create("BoxFilter", pb);

        filterOutput = op.getAsBufferedImage();
        filteredImage.setIcon(new ImageIcon(filterOutput));
    }

    private void applyTransform() {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(filterOutput);
        houghTransform = JAI.create("HoughTransform", pb);
    }

    private void drawResults() {
        List<HoughLine> lines = (List<HoughLine>) houghTransform.getProperty("lines");
        for (HoughLine line : lines) {
            System.out.println(line);
        }
        intersections.clear();
        int n = lines.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                HoughLine l1 = lines.get(i);
                HoughLine l2 = lines.get(j);
                intersections.add(l1.getIntersection(l2));
            }
        }
        drawSource();

        Point2D pos = new Point2D.Double(xPosition, yPosition);
        double d = 0;
        int i = 0;
        for (Point2D p : intersections) {
            Point2D pp = ct.screenToGameBoard((int) p.getX(), (int) p.getY());
            t.transform(AwtAdapterUtils.toAwtPoint2D(pp), AwtAdapterUtils.toAwtPoint2D(pp));
            if (pp.getX() > -100 && pp.getX() < 2200 && pp.getY() > -100 && pp.getY() < 3100) {
                i++;
                System.out.println("Point " + i + " : " + pp);
                d += pp.distance(pos);
            }
        }
        if (i > 0) {
            System.out.println("Distance moyenne : " + (d / i));
        }
    }

    private void drawSource() {
        // hauteur
        wcp.setPosition(new Vector3d(0, 0, zPosition));
        // angles de vision
        Size2D angles = new Size2D(cameraVisionAngle, cameraVisionAngle * .75);
        wcp.setVisionAngles(angles);
        // inclinaison
        wcp.setRotation(new Vector3d(cameraInclination, 0, 0));
        // wcp.setVisionLength(2600);
        // The dimension of the Camera
        wcp.setImageDimension(new Dimension(input.getWidth(null), input.getHeight(null)));

        ct = CoordinatesFactory.getCoordinates(wcp);

        t.setToIdentity();
        t.translate(xPosition, yPosition);
        // Le rep�re est indirect (x vers la gauche)
        t.scale(-1, 1);
        t.rotate(zAngle);

        BufferedImage bi = new BufferedImage(input.getWidth(null), input.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(input, 0, 0, null);
        AffineTransform ti;
        try {
            ti = t.createInverse();
            Point2D p1 = new Point2D.Double(0, 0);
            Point2D p2 = new Point2D.Double(500, 0);
            Point2D p3 = new Point2D.Double(500, 500);
            Point2D p4 = new Point2D.Double(0, 500);
            Point2D p5 = new Point2D.Double(250, 250);

            ti.transform(AwtAdapterUtils.toAwtPoint2D(p1), AwtAdapterUtils.toAwtPoint2D(p1));
            ti.transform(AwtAdapterUtils.toAwtPoint2D(p2), AwtAdapterUtils.toAwtPoint2D(p2));
            ti.transform(AwtAdapterUtils.toAwtPoint2D(p3), AwtAdapterUtils.toAwtPoint2D(p3));
            ti.transform(AwtAdapterUtils.toAwtPoint2D(p4), AwtAdapterUtils.toAwtPoint2D(p4));
            ti.transform(AwtAdapterUtils.toAwtPoint2D(p5), AwtAdapterUtils.toAwtPoint2D(p5));

            p1 = ct.gameBoardToScreen(p1.getX(), p1.getY());
            p2 = ct.gameBoardToScreen(p2.getX(), p2.getY());
            p3 = ct.gameBoardToScreen(p3.getX(), p3.getY());
            p4 = ct.gameBoardToScreen(p4.getX(), p4.getY());
            p5 = ct.gameBoardToScreen(p5.getX(), p5.getY());

            g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            g.drawLine((int) p2.getX(), (int) p2.getY(), (int) p3.getX(), (int) p3.getY());
            g.drawLine((int) p3.getX(), (int) p3.getY(), (int) p4.getX(), (int) p4.getY());
            g.drawLine((int) p4.getX(), (int) p4.getY(), (int) p1.getX(), (int) p1.getY());
            g.drawString("Rouge", (int) p5.getX(), (int) p5.getY());

            p1 = new Point2D.Double(0, 3000);
            p2 = new Point2D.Double(500, 3000);
            p3 = new Point2D.Double(500, 2500);
            p4 = new Point2D.Double(0, 2500);
            p5 = new Point2D.Double(250, 2750);

            ti.transform(AwtAdapterUtils.toAwtPoint2D(p1), AwtAdapterUtils.toAwtPoint2D(p1));
            ti.transform(AwtAdapterUtils.toAwtPoint2D(p2), AwtAdapterUtils.toAwtPoint2D(p2));
            ti.transform(AwtAdapterUtils.toAwtPoint2D(p3), AwtAdapterUtils.toAwtPoint2D(p3));
            ti.transform(AwtAdapterUtils.toAwtPoint2D(p4), AwtAdapterUtils.toAwtPoint2D(p4));
            ti.transform(AwtAdapterUtils.toAwtPoint2D(p5), AwtAdapterUtils.toAwtPoint2D(p5));

            p1 = ct.gameBoardToScreen(p1.getX(), p1.getY());
            p2 = ct.gameBoardToScreen(p2.getX(), p2.getY());
            p3 = ct.gameBoardToScreen(p3.getX(), p3.getY());
            p4 = ct.gameBoardToScreen(p4.getX(), p4.getY());
            p5 = ct.gameBoardToScreen(p5.getX(), p5.getY());

            g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            g.drawLine((int) p2.getX(), (int) p2.getY(), (int) p3.getX(), (int) p3.getY());
            g.drawLine((int) p3.getX(), (int) p3.getY(), (int) p4.getX(), (int) p4.getY());
            g.drawLine((int) p4.getX(), (int) p4.getY(), (int) p1.getX(), (int) p1.getY());
            g.drawString("Vert", (int) p5.getX(), (int) p5.getY());
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        g.setColor(Color.BLUE);
        for (Point2D p : intersections) {
            g.fillOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
        }

        image.setIcon(new ImageIcon(bi));
    }

    private void setInput(String name) {
        inputName = name;
        input = getImage(name);
        if (equalize) {
            input = equalizeHistogram(input);
        }
        if (normalize) {
            input = normalizeHistogram(input);
        }
        drawSource();
        applyColorFilter();
        applyTransform();
        drawResults();
    }
}
