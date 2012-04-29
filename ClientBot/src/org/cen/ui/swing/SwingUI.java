package org.cen.ui.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.cen.robot.IRobotServiceProvider;
import org.cen.ui.gameboard.GameBoardClickEvent;
import org.cen.ui.gameboard.GameBoardPainter;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.ISimulGameBoard;

public class SwingUI {
	JFrame frame;

	private GameBoardPainter painter;

	private IRobotServiceProvider provider;

	private JLabel robotImageLabel;

	private JLabel robotTextLabel;

	private BufferedImage simulatorImage;

	private JLabel simulatorImageLabel;

	private JLabel simulatorTextLabel;

	private ISimulGameBoard simulGameboard;

	private TableInfoGame tableInfoGame;



	public SwingUI(IRobotServiceProvider provider) {
		super();
		this.provider = provider;
		painter = new GameBoardPainter(provider);
		buildFrame();
		initPainter();
		start();
	}

	private void buildFrame() {
		frame = new JFrame();
		frame.getContentPane().setPreferredSize(new Dimension(900, 600));
		//		frame.getContentPane().setPreferredSize(new Dimension(850, 500));
		Box gameBoardsPanel = Box.createHorizontalBox();
		Box simulatorPanel = Box.createVerticalBox();
		Box robotPanel = Box.createVerticalBox();
		Container c = frame.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

		simulatorTextLabel = new JLabel();
		simulatorTextLabel.setText("Simulator");
		simulatorPanel.add(simulatorTextLabel);
		simulatorPanel.add(Box.createVerticalStrut(25));

		simulatorImageLabel = new JLabel();
		simulatorPanel.add(simulatorImageLabel);

		robotTextLabel = new JLabel();
		robotTextLabel.setText("Informations of Robot");
		robotPanel.add(robotTextLabel);
		robotPanel.add(Box.createVerticalStrut(25));

		robotImageLabel = new JLabel();
		robotPanel.add(robotImageLabel);

		simulGameboard = (ISimulGameBoard)provider.getService(IGameBoardService.class);
		tableInfoGame = new TableInfoGame(simulGameboard);
		JScrollPane scrollTableInfoGame = new JScrollPane(tableInfoGame);

		gameBoardsPanel.add(simulatorPanel);
		gameBoardsPanel.add(robotPanel);
		c.add(gameBoardsPanel);
		c.add(Box.createHorizontalStrut(50));
		c.add(scrollTableInfoGame);
		c.add(Box.createHorizontalStrut(50));

		simulatorImageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point2D p = painter.getRealCoordinates(e.getPoint());
				GameBoardClickEvent event = new GameBoardClickEvent(p);
				IGameBoardService gameBoard = provider.getService(IGameBoardService.class);
				gameBoard.postEvent(event);
			}
		});

		frame.pack();
	}

	private void initPainter() {

		Dimension d = new Dimension(400, 500);

		painter = new GameBoardPainter(provider);
		painter.setSize(d);
		simulatorImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		simulatorImageLabel.setIcon(new ImageIcon(simulatorImage));
	}

	private void start() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				updateSimulator();
				updateRobot();
				updateInfoGame();
			}
		};

		new Timer().scheduleAtFixedRate(task, 0, 500);
	}

	public void toFront() {
		frame.setVisible(true);
		frame.toFront();
	}

	protected void updateInfoGame(){
		tableInfoGame.repaint();
	}
	protected void updateRobot() {
		try {
			URL url = new URL("http://localhost:8080/Robot/test.gameboard");
			BufferedImage image = ImageIO.read(url);
			robotImageLabel.setIcon(new ImageIcon(image));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateSimulator() {
		Graphics g = simulatorImage.getGraphics();
		g.setColor(frame.getBackground());
		g.fillRect(0, 0, simulatorImage.getWidth(), simulatorImage.getHeight());
		painter.paint(g);
		simulatorImageLabel.repaint();
	}
}
