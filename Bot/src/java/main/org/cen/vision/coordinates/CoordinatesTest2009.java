package org.cen.vision.coordinates;

import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_OFFSET_LAST_X;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_OFFSET_LAST_Y;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_OFFSET_X;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_OFFSET_Y;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_SPACING_X;
import static org.cen.cup.cup2009.gameboard.GameBoard2009.COLUMN_SPACING_Y;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Vector3d;

import org.cen.adapter.AwtAdapterUtils;
import org.cen.cup.cup2009.gameboard.GameBoard2009;
import org.cen.cup.cup2009.gameboard.configuration.GameboardConfigurationHandler2009;
import org.cen.cup.cup2009.robot.match.ConfigurationAnalyzer;
import org.cen.cup.cup2009.robot.match.Strategy2009;
import org.cen.geom.Dimension;
import org.cen.geom.Point2D;
import org.cen.math.Size2D;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.vision.Acquisition;
import org.cen.vision.dataobjects.WebCamProperties;
import org.cen.vision.filters.ColorFilterDescriptor;
import org.cen.vision.filters.GrowingRegionDescriptor;
import org.cen.vision.filters.GrowingRegionOpImage.Region;

public class CoordinatesTest2009 {
    public static void main(String[] args) {
        new CoordinatesTest2009();
    }

    private Acquisition a;

    private CoordinatesTransform c;

    private double cameraInclination;

    private double cameraVisionAngle;

    private double colorAngle = ConfigurationAnalyzer.COLOR_ANGLE_GREEN;

    private GameboardConfigurationHandler2009 handler;

    private Image im1;

    private Image im2;

    private Image im3;

    private JLabel image1;

    private JLabel image2;

    private JLabel image3;

    private boolean invertCoordinates;

    private double regionThreshold = 1500d;

    private WebCamProperties wcp;

    private double xPos;

    private double yPos;

    private double zAngle;

    private double zPos;

    private JSlider colorSlider;

    private JTextArea textArea;

    private JSlider angleSlider;

    private JSlider inclinaisonSlider;

    private JSlider orientationSlider;

    public CoordinatesTest2009() {
        super();

        registerOperators();
        initialize();
        initGUI();
    }

    private final JFrame f = new JFrame();

    private final Timer timer = new Timer();

    private void initGUI() {
        Container cp = f.getContentPane();
        f.setLayout(new GridBagLayout());
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.cancel();
                a.close();
                f.dispose();
                super.windowClosed(e);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 15;

        image1 = new JLabel();
        cp.add(image1, gbc);

        image1.addMouseMotionListener(new MouseMotionAdapter() {
            private final Point2D dst = new Point2D.Double();

            @Override
            public void mouseMoved(MouseEvent e) {
                Point2D p = c.screenToGameBoard(e.getX(), e.getY());
                handler.getTransform().transform(AwtAdapterUtils.toAwtPoint2D(p), AwtAdapterUtils.toAwtPoint2D(dst));
                System.out.println(dst.toString());
            }
        });

        image2 = new JLabel();
        cp.add(image2, gbc);

        image3 = new JLabel();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(image3, gbc);

        gbc.gridwidth = 1;
        addOrientationControl(cp, gbc);
        addColorFilterControl(cp, gbc);
        addRegionThresholdControl(cp, gbc);
        addInclinationControl(cp, gbc);
        addAngleLabel(cp, gbc);
        addXPositionControl(cp, gbc);
        addYPositionControl(cp, gbc);
        addZPositionControl(cp, gbc);
        addMatchSideControl(cp, gbc);
        addInputSelector(cp, gbc);
        addTextArea(cp, gbc);

        image3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handler.execute();
                super.mouseClicked(e);
            }
        });

        setSource("CoordinatesTestG1.jpg");

        showValues();
        f.pack();
        f.setVisible(true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Image i = a.getImage();
                if (i == null) {
                    return;
                }
                im1 = a.getImage();
                handler.setInput(im1);
                drawSource();
                buildColorFilter();
                buildRegionFilter();
            }
        }, 1000, 1000);
    }

    private void initialize() {
        Logger logger = Logger.getLogger("org.cen");
        logger.setLevel(Level.FINEST);
        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.FINEST);
        logger.addHandler(h);

        handler = new GameboardConfigurationHandler2009();
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "org/cen/cup/cup2009/gameboard/elements.properties");
        Properties p = new Properties();
        try {
            p.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.setElements(p);

        a = new Acquisition();
        try {
            a.open();
            a.waitForInput();
        } catch (Exception e) {
            e.printStackTrace();
        }

        wcp = new WebCamProperties();
        initializeWebCam();

        updatePosition();
    }

    private void addAngleLabel(Container cp, GridBagConstraints gbc) {
        JLabel angleLabel = new JLabel("Angle de vision :");
        cp.add(angleLabel, gbc);

        angleSlider = new JSlider(0, 36000, (int) (Math.toDegrees(cameraVisionAngle) * 100));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(angleSlider, gbc);
        gbc.gridwidth = 1;

        angleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cameraVisionAngle = Math.toRadians(angleSlider.getValue() / 100d);
                angleSlider.setToolTipText(Double.toString(Math.toDegrees(cameraVisionAngle)));
                updateWebCam();
                drawSource();
            }
        });
    }

    private void addColorFilterControl(Container cp, GridBagConstraints gbc) {
        JLabel colorLabel = new JLabel("Couleur :");
        cp.add(colorLabel, gbc);

        colorSlider = new JSlider(0, 36000, (int) (Math.toDegrees(colorAngle) * 100));
        cp.add(colorSlider, gbc);
        colorSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                colorAngle = Math.toRadians(colorSlider.getValue() / 100d);
                colorSlider.setToolTipText(Double.toString(colorAngle));
                buildColorFilter();
                showValues();
            }
        });
    }

    private void addInclinationControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Inclinaison webcam :");
        cp.add(l, gbc);

        inclinaisonSlider = new JSlider(0, 9000, (int) (Math.toDegrees(cameraInclination) * 100));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(inclinaisonSlider, gbc);
        gbc.gridwidth = 1;

        inclinaisonSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cameraInclination = Math.toRadians(inclinaisonSlider.getValue() / 100d);
                inclinaisonSlider.setToolTipText(Double.toString(Math.toDegrees(cameraInclination)));
                updateWebCam();
                drawSource();
                showValues();
            }
        });
    }

    private void addInputSelector(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Configuration :");
        cp.add(l, gbc);

        String[] values = new String[] { "CoordinatesTestG1.jpg", "CoordinatesTestG2.jpg", "CoordinatesTestG3.jpg",
                "CoordinatesTestG4.jpg", "CoordinatesTestG5.jpg", "CoordinatesTestG6.jpg", "CoordinatesTestG7.jpg",
                "CoordinatesTestG8.jpg", "CoordinatesTestG9.jpg", "CoordinatesTestG10.jpg", "CoordinatesTestR1.jpg",
                "CoordinatesTestR2.jpg", "CoordinatesTestR3.jpg", "CoordinatesTestR4.jpg", "CoordinatesTestR5.jpg",
                "CoordinatesTestR6.jpg", "CoordinatesTestR7.jpg", "CoordinatesTestR8.jpg", "CoordinatesTestR9.jpg",
                "CoordinatesTestR10.jpg", };
        final JComboBox cb = new JComboBox(values);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(cb, gbc);
        gbc.gridwidth = 1;

        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSource((String) cb.getSelectedItem());
            }
        });
    }

    private void addMatchSideControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("C�t� :");
        cp.add(l, gbc);

        final JCheckBox cb = new JCheckBox("rouge");
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(cb, gbc);
        gbc.gridwidth = 1;

        cb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                invertCoordinates = cb.isSelected();
                colorAngle = (cb.isSelected()) ? ConfigurationAnalyzer.COLOR_ANGLE_RED
                        : ConfigurationAnalyzer.COLOR_ANGLE_GREEN;

                updatePosition();
                updateWebCam();
                drawSource();
            }
        });
    }

    private void addOrientationControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Orientation du robot :");
        cp.add(l, gbc);

        orientationSlider = new JSlider(-360, 360, (int) (Math.toDegrees(zAngle)));
        cp.add(orientationSlider, gbc);
        orientationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                zAngle = Math.toRadians(orientationSlider.getValue());
                orientationSlider.setToolTipText(Double.toString(Math.toDegrees(zAngle)));
                updatePosition();
                drawSource();
            }
        });
    }

    private void addTextArea(Container cp, GridBagConstraints gbc) {

        JLabel l = new JLabel("Valeurs :");
        cp.add(l, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        textArea = new JTextArea(10, 80);
        textArea.setEditable(false);
        gbc.gridwidth = 1;
        cp.add(textArea, gbc);

    }

    private void updatePosition() {
        if (invertCoordinates) {
            handler.setPosition(xPos, GameBoard2009.BOARD_HEIGHT - yPos, zAngle);
        } else {
            handler.setPosition(xPos, yPos, zAngle);
        }
        handler.setMirror(invertCoordinates);
        showValues();
    }

    private void showValues() {
        StringBuilder text = new StringBuilder();
        text.append("orientation : ").append(Math.toDegrees(zAngle)).append("\n");
        text.append("color : ").append(colorAngle).append("\n");
        text.append("Seuil : ").append(regionThreshold).append("\n");
        text.append("Inclinaison WebCam : ").append(Math.toDegrees(cameraInclination)).append("\n");
        text.append("Angle de vision : ").append(Math.toDegrees(cameraVisionAngle)).append("\n");
        text.append("xPos : ").append(xPos).append("\n");
        text.append("yPos : ").append(yPos).append("\n");
        text.append("zPos : ").append(zPos).append("\n");
        if (textArea != null) {
            textArea.setText(text.toString());
        }
    }

    private void addRegionThresholdControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Seuil :");
        cp.add(l, gbc);

        final JSlider slider = new JSlider(100, 10000, (int) regionThreshold);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(slider, gbc);
        gbc.gridwidth = 1;

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                regionThreshold = slider.getValue();
                buildRegionFilter();
            }
        });
    }

    private void addXPositionControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Position X :");
        cp.add(l, gbc);

        final JSlider slider = new JSlider(0, 500, (int) xPos);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(slider, gbc);
        gbc.gridwidth = 1;

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                xPos = slider.getValue();
                slider.setToolTipText(Double.toString(xPos));
                updatePosition();
                drawSource();
            }
        });
    }

    private void addYPositionControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Position Y :");
        cp.add(l, gbc);

        final JSlider slider = new JSlider(0, 500, (int) yPos);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(slider, gbc);
        gbc.gridwidth = 1;

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                yPos = slider.getValue();
                slider.setToolTipText(Double.toString(yPos));
                updatePosition();
                drawSource();
            }
        });
    }

    private void addZPositionControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Position Z :");
        cp.add(l, gbc);

        final JSlider slider = new JSlider(0, 500, (int) zPos);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(slider, gbc);
        gbc.gridwidth = 1;

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                zPos = slider.getValue();
                slider.setToolTipText(Double.toString(zPos));
                updateWebCam();
                showValues();
                drawSource();
            }
        });
    }

    private void buildColorFilter() {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(im1);
        pb.add(5);
        pb.add(5);
        pb.add(2);
        pb.add(2);
        RenderedOp op = JAI.create("BoxFilter", pb);

        pb = new ParameterBlock();
        pb.addSource(op.getAsBufferedImage());
        pb.add(colorAngle);
        op = JAI.create("ColorFilter", pb);

        im2 = op.getAsBufferedImage();
        image2.setIcon(new ImageIcon(im2));
    }

    private void buildRegionFilter() {
        // Image im = a.getImage();
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(im2);
        pb.add(regionThreshold);
        RenderedOp op = JAI.create("GrowingRegionFilter", pb);

        im3 = op.getAsBufferedImage();
        Graphics g = im3.getGraphics();

        List<Region> regions = handler.getRegions();

        int n = (Integer) op.getProperty("regionCount");
        for (int i = 1; i <= n; i++) {
            Region r = (Region) op.getProperty("region" + i);
            if (r.getMeanValue()[0] > 128 && r.getCount() > 20) {
                regions.add(r);
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            Point2D p = r.getLocation();
            g.drawLine((int) p.getX() - 2, (int) p.getY(), (int) p.getX() + 2, (int) p.getY());
            g.drawLine((int) p.getX(), (int) p.getY() - 2, (int) p.getX(), (int) p.getY() + 2);
        }

        image3.setIcon(new ImageIcon(im3));
        handler.execute();

        System.out.println("card: " + handler.getColumnElementsCard());
    }

    private void updateWebCam() {
        // hauteur
        wcp.setPosition(new Vector3d(0, 0, zPos));
        // angles de vision
        Size2D angles = new Size2D(cameraVisionAngle, cameraVisionAngle * .75);
        wcp.setVisionAngles(angles);
        // inclinaison
        wcp.setRotation(new Vector3d(cameraInclination, 0, 0));
        // The dimension of the Camera
        wcp.setImageDimension(new Dimension(im1.getWidth(null), im1.getHeight(null)));

        handler.setWebCamProperties(wcp);
    }

    private void initializeWebCam() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "org/cen/cup/cup2009/robot/robot2009.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RobotPosition robotPosition = new RobotPosition(0, 0, 0);
        robotPosition.setFromProperties(properties, Strategy2009.PROPERTY_INITIAL_POSITION + ".BLUE.");
        Point2D cp = robotPosition.getCentralPoint();
        xPos = cp.getX();
        yPos = cp.getY();
        zAngle = robotPosition.getAlpha();

        wcp.set(properties);

        Vector3d webCamPosition = wcp.getPosition();
        zPos = webCamPosition.z;
        cameraInclination = wcp.getRotation().x;
        Size2D angles = wcp.getVisionAngles();
        cameraVisionAngle = angles.getWidth();

        handler.setWebCamProperties(wcp);
    }

    private void drawSource() {
        c = CoordinatesFactory.getCoordinates(wcp);

        AffineTransform t = handler.getTransform();

        BufferedImage bi = new BufferedImage(im1.getWidth(null), im1.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(im1, 0, 0, null);
        AffineTransform ti;
        try {
            ti = t.createInverse();

            for (int x = COLUMN_OFFSET_X; x <= COLUMN_OFFSET_LAST_X; x += COLUMN_SPACING_X) {
                Point2D p1 = new Point2D.Double(x, COLUMN_OFFSET_Y);
                Point2D p2 = new Point2D.Double(x, COLUMN_OFFSET_LAST_Y);

                ti.transform(AwtAdapterUtils.toAwtPoint2D(p1), AwtAdapterUtils.toAwtPoint2D(p1));
                ti.transform(AwtAdapterUtils.toAwtPoint2D(p2), AwtAdapterUtils.toAwtPoint2D(p2));

                p1 = c.gameBoardToScreen(p1.getX(), p1.getY());
                p2 = c.gameBoardToScreen(p2.getX(), p2.getY());
                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            }
            for (int y = COLUMN_OFFSET_Y; y <= COLUMN_OFFSET_LAST_Y; y += COLUMN_SPACING_Y) {
                Point2D p1 = new Point2D.Double(COLUMN_OFFSET_X, y);
                Point2D p2 = new Point2D.Double(COLUMN_OFFSET_LAST_X, y);

                ti.transform(AwtAdapterUtils.toAwtPoint2D(p1), AwtAdapterUtils.toAwtPoint2D(p1));
                ti.transform(AwtAdapterUtils.toAwtPoint2D(p2), AwtAdapterUtils.toAwtPoint2D(p2));

                p1 = c.gameBoardToScreen(p1.getX(), p1.getY());
                p2 = c.gameBoardToScreen(p2.getX(), p2.getY());
                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            }
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        image1.setIcon(new ImageIcon(bi));
    }

    private void registerOperators() {
        String productName = "testjai";
        OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();

        ColorFilterDescriptor colorFilterDescriptor = new ColorFilterDescriptor();
        String operationName = "ColorFilter";
        RenderedImageFactory rif = colorFilterDescriptor;
        or.registerDescriptor(colorFilterDescriptor);
        RIFRegistry.register(or, operationName, productName, rif);

        GrowingRegionDescriptor targetFilterDescriptor = new GrowingRegionDescriptor();
        operationName = "GrowingRegionFilter";
        rif = targetFilterDescriptor;
        or.registerDescriptor(targetFilterDescriptor);
        RIFRegistry.register(or, operationName, productName, rif);
    }

    private void setSource(String name) {
        ClassLoader l = getClass().getClassLoader();
        URL u = l.getResource("org/cen/cup/cup2009/gameboard/configuration/" + name);
        Image image = Toolkit.getDefaultToolkit().getImage(u);
        image1.setIcon(new ImageIcon(image));
        im1 = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = im1.getGraphics();
        g.drawImage(image, 0, 0, null);

        handler.setInput(im1);
        drawSource();
        buildColorFilter();
        buildRegionFilter();
    }
}
