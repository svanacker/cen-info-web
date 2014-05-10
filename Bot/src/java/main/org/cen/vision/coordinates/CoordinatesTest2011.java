package org.cen.vision.coordinates;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RIFRegistry;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Vector3d;

import org.cen.cup.cup2009.robot.match.Strategy2009;
import org.cen.cup.cup2011.gameboard.configuration.GameboardConfigurationHandler2011;
import org.cen.cup.cup2011.gameboard.configuration.PawnPosition;
import org.cen.geom.Dimension;
import org.cen.geom.Point2D;
import org.cen.math.Size2D;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.ui.rtp.PushImageDataSource;
import org.cen.ui.rtp.RTPServer;
import org.cen.vision.Acquisition;
import org.cen.vision.dataobjects.WebCamProperties;
import org.cen.vision.filters.ColorFilterDescriptor;
import org.cen.vision.filters.GrowingRegionDescriptor;
import org.cen.vision.filters.GrowingRegionOpImage.Region;

public class CoordinatesTest2011 {
    private static final int COLUMN_OFFSET_X = 0;

    private static final int COLUMN_OFFSET_Y = 450;

    private static final int COLUMN_SPACING_X = 350;

    private static final int COLUMN_SPACING_Y = 350;

    public static final int COLUMNS_X_COUNT = 7;

    public static final int COLUMNS_Y_COUNT = 7;

    public static final int COLUMN_OFFSET_LAST_X = COLUMN_OFFSET_X + COLUMN_SPACING_X * (COLUMNS_X_COUNT - 1);

    public static final int COLUMN_OFFSET_LAST_Y = COLUMN_OFFSET_Y + COLUMN_SPACING_Y * (COLUMNS_Y_COUNT - 1);

    public static void main(String[] args) {
        new CoordinatesTest2011();
    }

    private Acquisition a;

    private CoordinatesTransform c;

    private double cameraInclination;

    private double cameraVisionAngle;

    private double colorAngle = 1.03;

    private GameboardConfigurationHandler2011 handler;

    private Image im1;

    private Image im2;

    private Image im3;

    private JLabel image1;

    private JLabel image2;

    private JLabel image3;

    private double regionThreshold = 2500d;

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

    private RobotPosition robotPosition;

    private final JFrame f = new JFrame();

    private final Timer timer = new Timer();

    private long regionsFilterTime = 0;

    private long colorFilterTime = 0;

    public CoordinatesTest2011() {
        super();

        registerOperators();
        initialize();
        initGUI();
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

    private void addButton(Container cp, GridBagConstraints gbc) {
        JButton button = new JButton(new AbstractAction("Calcul") {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                buildColorFilter();
                buildRegionFilter();
                showValues();
            }
        });
        cp.add(button, gbc);
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

        String[] values = new String[198];
        for (int i = 1; i <= values.length; i++) {
            values[i - 1] = "Picture " + i + ".jpg";
        }
        final JComboBox cb = new JComboBox(values);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cp.add(cb, gbc);
        gbc.gridwidth = 1;

        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setSource((String) cb.getSelectedItem());
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
                updateWebCam();
                drawSource();
            }
        });
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
                showValues();
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

    private void addXPositionControl(Container cp, GridBagConstraints gbc) {
        JLabel l = new JLabel("Position X :");
        cp.add(l, gbc);

        final JSlider slider = new JSlider(0, 2100, (int) xPos);
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

        final JSlider slider = new JSlider(0, 3000, (int) yPos);
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

    private Image applyZoneFilter(Image image) {
        if (!(image instanceof BufferedImage)) {
            return image;
        }
        BufferedImage im = (BufferedImage) image;
        Graphics g = im.getGraphics();
        g.fillRect(0, 0, image.getWidth(null), image.getHeight(null) / 4);
        return im;
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

        long t = System.currentTimeMillis();
        im2 = op.getAsBufferedImage();
        colorFilterTime = System.currentTimeMillis() - t;
        image2.setIcon(new ImageIcon(im2));
    }

    private void buildRegionFilter() {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(im2);
        pb.add(regionThreshold);
        pb.add(6);
        pb.add(6);
        RenderedOp op = JAI.create("GrowingRegionFilter", pb);

        long t = System.currentTimeMillis();
        im3 = op.getAsBufferedImage();
        regionsFilterTime = System.currentTimeMillis() - t;
        Graphics g = im3.getGraphics();

        List<Region> regions = new ArrayList<Region>();

        int n = (Integer) op.getProperty("regionCount");
        for (int i = 1; i <= n; i++) {
            Region r = (Region) op.getProperty("region" + i);
            boolean b = r.getMeanValue()[0] > 128 && r.getCount() > 20;
            if (b) {
                regions.add(r);
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            Point2D p = r.getLocation();
            g.drawLine((int) p.getX() - 2, (int) p.getY(), (int) p.getX() + 2, (int) p.getY());
            g.drawLine((int) p.getX(), (int) p.getY() - 2, (int) p.getX(), (int) p.getY() + 2);

            Rectangle bounds = r.getBounds();
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        image3.setIcon(new ImageIcon(im3));

        printConfiguration(g, regions);
    }

    private void drawSource() {
        c = CoordinatesFactory.getCoordinates(wcp);

        BufferedImage bi = new BufferedImage(im1.getWidth(null), im1.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(im1, 0, 0, null);

        for (int x = COLUMN_OFFSET_X; x < COLUMN_OFFSET_LAST_X; x += COLUMN_SPACING_X) {
            for (int y = COLUMN_OFFSET_Y; y < COLUMN_OFFSET_LAST_Y; y += COLUMN_SPACING_Y) {
                Point2D p1 = new Point2D.Double(x, y);
                p1 = c.absoluteToScreen(robotPosition, p1);

                Point2D p2 = new Point2D.Double(x + COLUMN_SPACING_X, y);
                p2 = c.absoluteToScreen(robotPosition, p2);
                g.setColor(Color.GREEN);
                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());

                p2 = new Point2D.Double(x, y + COLUMN_SPACING_Y);
                p2 = c.absoluteToScreen(robotPosition, p2);
                g.setColor(Color.BLUE);
                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            }
        }

        image1.setIcon(new ImageIcon(bi));
    }

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
            @Override
            public void mouseMoved(MouseEvent e) {
                Point2D p = c.screenToAbsolute(robotPosition, e.getX(), e.getY());
                System.out.println(p.toString());
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
        addInputSelector(cp, gbc);
        addTextArea(cp, gbc);
        addButton(cp, gbc);

        image3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // handler.execute();
                super.mouseClicked(e);
            }
        });

        setSource("picture 113.jpg");

        showValues();
        f.pack();
        f.setVisible(true);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Image i = a.getImage();
                if (i == null) {
                    return;
                }
                im1 = a.getImage();
                // saveImage(im1);
                // handler.setInput(im1);
                drawSource();
                applyZoneFilter(im1);
                buildColorFilter();
                buildRegionFilter();
            }
        };
        timer.schedule(task, 1000, 1000);

        PushImageDataSource dataSource = new PushImageDataSource();
        try {
            dataSource.setImage(im1);
            RTPServer rtpServer = new RTPServer(dataSource);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void initialize() {
        Logger logger = Logger.getLogger("org.cen");
        logger.setLevel(Level.FINEST);
        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.FINEST);
        logger.addHandler(h);

        handler = new GameboardConfigurationHandler2011(null);

        a = new Acquisition();
        try {
            a.open();
            a.waitForInput();
        } catch (Exception e) {
            e.printStackTrace();
        }

        xPos = 200;
        yPos = 200;
        zAngle = Math.toRadians(26);
        robotPosition = new RobotPosition(xPos, yPos, zAngle);

        wcp = new WebCamProperties();
        initializeWebCam();

        updatePosition();
    }

    private void initializeWebCam() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "org/cen/cup/cup2011/robot/robot2011.properties");
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
    }

    private boolean isElementPresent(Graphics g, List<Region> regions, int cx, int cy) {
        g.setColor(Color.GRAY);
        for (Region r : regions) {
            boolean b = r.getBounds().contains(cx, cy);
            if (b) {
                g.setColor(Color.BLUE);
                g.fillRect(cx - 2, cy - 2, 5, 5);
                return true;
            }
        }
        g.fillRect(cx - 2, cy - 2, 5, 5);
        return false;
    }

    private void printConfiguration(Graphics g, List<Region> regions) {
        c = CoordinatesFactory.getCoordinates(wcp);
        Point2D ap;
        List<PawnPosition> positions = handler.getPawnPositions();
        for (PawnPosition position : positions) {
            ap = position.getPosition();
            int x = (int) ap.getX();
            int y = (int) ap.getY();
            Point2D sp = c.absoluteToScreen(robotPosition, ap);
            int sx = (int) sp.getX();
            int sy = (int) sp.getY();
            System.out.println("Coordinates (" + x + ", " + y + ") -> (" + sx + ", " + sy + ") : "
                    + isElementPresent(g, regions, sx, sy));
        }
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

    private void saveImage(Image im) {
        try {
            ImageIO.write((RenderedImage) im, "png", new File("C:\\Users\\Emmanuel\\Documents\\image.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSource(String name) {
        ClassLoader l = getClass().getClassLoader();
        URL u = l.getResource("org/cen/cup/cup2011/gameboard/configuration/" + name);
        Image image = Toolkit.getDefaultToolkit().getImage(u);
        image1.setIcon(new ImageIcon(image));
        im1 = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = im1.getGraphics();
        g.drawImage(image, 0, 0, null);

        drawSource();
        applyZoneFilter(im1);
        buildColorFilter();
        buildRegionFilter();
    }

    private void showValues() {
        String text = String
                .format("orientation : %.2f\ncolor : %.2f\nSeuil : %.2f\nInclinaison WebCam : %.2f\nAngle de vision : %.2f\nxPos : %.2f\nyPos : %.2f\nzPos : %.2f\nFiltre couleur (ms) : %d\nFiltre régions (ms) : %d",
                        Math.toDegrees(zAngle), colorAngle, regionThreshold, Math.toDegrees(cameraInclination),
                        Math.toDegrees(cameraVisionAngle), xPos, yPos, zPos, colorFilterTime, regionsFilterTime);
        if (textArea != null) {
            textArea.setText(text);
        }
    }

    private void updatePosition() {
        robotPosition.set(xPos, yPos, zAngle);
        showValues();
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
    }
}
